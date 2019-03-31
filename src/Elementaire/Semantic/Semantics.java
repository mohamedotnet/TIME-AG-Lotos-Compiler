package Elementaire.Semantic;

import Elementaire.STE.STE;
import Elementaire.STE.Transition;
import Elementaire.Syntax.AST;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* TODO: Refactor the code */

public class Semantics implements ISemantics {

    AST tree;
    STE graph = new STE();
    int succIndex = 1;
    ArrayList<Action> act = new ArrayList<Action>();


    public STE generate(AST tree, String localite) {
        ArrayList<Transition> listT = generateTransitions(tree, 0, localite);
        for (int i = 0; i < listT.size(); i++) {
            graph.addTransition(listT.get(i));
            graph.addAction(listT.get(i).getAct());
            graph.addState(listT.get(i).getSrc());
            graph.addState(listT.get(i).getDest());
        }
        graph.setS0(0);

        return graph;
    }

    private String getNextLocalite(String move){
        Pattern pattern  = Pattern.compile("move\\((.*?)\\)");
        Matcher matcher = pattern.matcher(move);
        matcher.matches();
        return matcher.group(1);
    }

    private int createAddTransition(ArrayList<Transition> listT, int currentIndex,
                                     String unit, int succIndex, String localite){
        Transition tr;
        if (tree.getNodeLeft().getUnit().matches("move\\((.*?)\\)")){
            String nextLocalite = getNextLocalite(tree.getNodeLeft().getUnit());
            System.out.println("next Localite" + nextLocalite);
            tr = new Transition(currentIndex, tree.getNodeLeft().getUnit(), succIndex, nextLocalite);
        }else {
            tr = new Transition(currentIndex, tree.getNodeLeft().getUnit(), succIndex, localite);
        }
        currentIndex = succIndex;
        succIndex++;
        listT.add(tr);
        return currentIndex;
    }

    public ArrayList<Transition> generateTransitions(AST tree, int currentIndex, String localite) {
        ArrayList<Transition> listT = new ArrayList<Transition>();

        if (tree.getUnit().matches("delta\\([0-9]*\\)")){
            Transition tr = new Transition(currentIndex, "Delta", succIndex, localite);
            currentIndex = succIndex;
            succIndex++;
            listT.add(tr);
            listT.addAll(generateTransitions(tree.getNodeRight(), currentIndex, localite));
        }
        /* Add transitions when unit is exit */
        if (tree.isLeaf() && (tree.getUnit().matches("exit") || tree.getUnit().matches("exit\\{[0-9]*\\}"))) {
            int succI = isExist(tree);
            Transition tr;

            if (tree.getUnit().matches("exit")){
                if (succI != -1) {
                    tr = new Transition(currentIndex, "exit", succI, localite);
                    succIndex++;
                } else {
                    tr = new Transition(currentIndex, "exit", succIndex, localite);
                    currentIndex = succIndex;
                    succIndex++;
                }
                listT.add(tr);
            }else {
                if (succI != -1) {
                    listT.add(new Transition(currentIndex, "T", succI, localite));
                    currentIndex = succIndex++;
                    listT.add(new Transition(currentIndex, "exit", ++succIndex, localite));
                    succIndex++;
                } else {
                    listT.add(new Transition(currentIndex, "T", succIndex, localite));
                    succIndex++;
                    listT.add(new Transition(currentIndex, "exit", succIndex, localite));
                    currentIndex = succIndex;
                    succIndex++;
                }
            }

        } else if (tree.getUnit().equals(";")) {
            if (tree.getNodeRight().isLeaf() && !tree.getNodeRight().getUnit().matches("exit\\{[0-9]*\\}")
                && !tree.getNodeRight().getUnit().equals("exit")) {
                Transition tr;
                if (tree.getNodeLeft().getUnit().matches("move\\((.*?)\\)")){
                    localite = getNextLocalite(tree.getNodeLeft().getUnit());
                }
                tr = new Transition(currentIndex, tree.getNodeLeft().getUnit(), succIndex, localite);
                currentIndex = succIndex;
                succIndex++;
                listT.add(tr);
            } else {
                Transition tr;
                if (tree.getNodeLeft().getUnit().matches("move\\([a-z][a-z0-9]*\\)")){
                    localite = getNextLocalite(tree.getNodeLeft().getUnit());
                }
                tr = new Transition(currentIndex, tree.getNodeLeft().getUnit(), succIndex, localite);
                currentIndex = succIndex;
                succIndex++;
                listT.add(tr);
                act.add(new Action(succIndex, tree.getNodeRight()));
                listT.addAll(generateTransitions(tree.getNodeRight(), currentIndex, localite));
            }
        }

        /* Add transitions when unit is [] */
        else if (tree.getUnit().equals("[]")) {
            if(tree.getNodeLeft().equalsALL(tree.getNodeRight())){
                listT.addAll(generateTransitions(tree.getNodeLeft(), currentIndex, localite));
            }else{
                listT.addAll(generateTransitions(tree.getNodeLeft(), currentIndex, localite));
                listT.addAll(generateTransitions(tree.getNodeRight(), currentIndex, localite));
            }
        }

        /* Add transitions when unit is >> */
        else if (tree.getUnit().equals(">>")) {
            listT.addAll(generateTransitions(tree.getNodeLeft(), currentIndex, localite));
            int size = listT.size();
            if (listT.get(size - 1).getAct().equals("exit")) {
                currentIndex = listT.get(size - 1).getDest();
                listT.get(size - 1).setAct("I");
                listT.addAll(generateTransitions(tree.getNodeRight(), currentIndex, localite));
            }
        }

        /* Add transitions when unit is [> */
        else if (tree.getUnit().equals("[>")) {
            if (!tree.getNodeLeft().isLeaf()) {
                listT.addAll(generateTransitions(tree.getNodeRight(), currentIndex, localite));
                if (tree.getNodeLeft().getNodeLeft().getUnit().matches("move\\([a-z][a-z0-9]*\\)")){
                    localite = getNextLocalite(tree.getNodeLeft().getNodeLeft().getUnit());
                }
                Transition tr = new Transition(currentIndex, tree.getNodeLeft().getNodeLeft().getUnit(), succIndex, localite);
                currentIndex = succIndex;
                succIndex++;
                listT.add(tr);
                AST ast = tree.clone();
                ast.setLeft(tree.getNodeLeft().getNodeRight());
                listT.addAll(generateTransitions(ast, currentIndex, localite));
            } else {
                listT.addAll(generateTransitions(tree.getNodeRight(), currentIndex, localite));
                listT.addAll(generateTransitions(tree.getNodeLeft(), currentIndex, localite));
            }
        }

        /* Add transitions when unit is || */
        else if (tree.getUnit().equals("||")) {
            if (tree.getNodeLeft().equalsALL(tree.getNodeRight())) {
                listT.addAll(generateTransitions(tree.getNodeLeft(), currentIndex, localite));
            } else if (!tree.getNodeLeft().isLeaf() && tree.getNodeLeft().getNodeLeft().getUnit().equals(tree.getNodeRight().getNodeLeft().getUnit())) {
                if (tree.getNodeLeft().getNodeLeft().getUnit().matches("move\\([a-z][a-z0-9]*\\)")){
                    localite = getNextLocalite(tree.getNodeLeft().getNodeLeft().getUnit());
                }
                Transition tr = new Transition(currentIndex, tree.getNodeLeft().getNodeLeft().getUnit(), succIndex, localite);
                currentIndex = succIndex;
                succIndex++;
                listT.add(tr);
            }

        }

        /* Add transitions when unit is ||| */
        else if (tree.getUnit().equals("|||")) {
            /* Watch out CONDITIONS !! */
            if ((tree.getNodeLeft().getUnit().equals("exit") && tree.getNodeRight().getUnit().equals("exit")) ||
                (tree.getNodeLeft().getUnit().equals("exit") && tree.getNodeRight().getUnit().matches("exit\\{[0-9]*\\}")) ||
                (tree.getNodeLeft().getUnit().matches("exit\\{[0-9]*\\}") && tree.getNodeRight().getUnit().equals("exit")) ||
                (tree.getNodeLeft().getUnit().matches("exit\\{[0-9]*\\}") && tree.getNodeRight().getUnit().matches("exit\\{[0-9]*\\}"))
            ) {
                int succI = isExist(tree);
                Transition tr;

                if (succI != -1) {
                    tr = new Transition(currentIndex, "exit", succI, localite);
                    succIndex++;
                } else {
                    tr = new Transition(currentIndex, "exit", succIndex, localite);
                    currentIndex = succIndex;
                    succIndex++;
                }
                listT.add(tr);
            }
            else if (tree.getNodeLeft().isLeaf()) {
                if (!tree.getNodeRight().isLeaf()) {
                    if (tree.getNodeRight().getNodeLeft().getUnit().matches("move\\([a-z][a-z0-9]*\\)")){
                        localite = getNextLocalite(tree.getNodeRight().getNodeLeft().getUnit());
                    }
                    Transition tr = new Transition(currentIndex, tree.getNodeRight().getNodeLeft().getUnit(), succIndex, localite);
                    currentIndex = succIndex;
                    succIndex++;
                    listT.add(tr);
                    AST ast = tree.clone();
                    ast.setRight(tree.getNodeRight().getNodeRight());
                    listT.addAll(generateTransitions(ast, currentIndex, localite));
                }
            }

            else if (tree.getNodeRight().isLeaf()) {
                if (!tree.getNodeLeft().isLeaf()) {
                    if (tree.getNodeLeft().getNodeLeft().getUnit().matches("move\\([a-z][a-z0-9]*\\)")){
                        localite = getNextLocalite(tree.getNodeLeft().getNodeLeft().getUnit());
                    }
                    Transition tr = new Transition(currentIndex, tree.getNodeLeft().getNodeLeft().getUnit(), succIndex, localite);
                    currentIndex = succIndex;
                    succIndex++;
                    listT.add(tr);
                    AST ast = tree.clone();
                    ast.setLeft(tree.getNodeLeft().getNodeRight());
                    listT.addAll(generateTransitions(ast, currentIndex, localite));
                }
            }

            else {
                AST astL = tree.clone();
                AST astR = tree.clone();
                int cur1 = currentIndex;
                int cur2 = currentIndex;
                if (tree.getNodeLeft().getNodeLeft().getUnit().matches("move\\([a-z][a-z0-9]*\\)")){
                    localite = getNextLocalite(tree.getNodeLeft().getNodeLeft().getUnit());
                }
                Transition tr1 = new Transition(currentIndex, tree.getNodeLeft().getNodeLeft().getUnit(), succIndex, localite);
                cur1 = succIndex;
                succIndex++;

                listT.add(tr1);
                astL.setLeft(tree.getNodeLeft().getNodeRight());
                listT.addAll(generateTransitions(astL, cur1, localite));

                if (tree.getNodeRight().getNodeLeft().getUnit().matches("move\\([a-z][a-z0-9]*\\)")){
                    localite = getNextLocalite(tree.getNodeRight().getNodeLeft().getUnit());
                }
                Transition tr = new Transition(currentIndex, tree.getNodeRight().getNodeLeft().getUnit(), succIndex, localite);
                cur2 = succIndex;
                succIndex++;
                listT.add(tr);
                astR.setRight(tree.getNodeRight().getNodeRight());
                listT.addAll(generateTransitions(astR, cur2, localite));

            }

        }


        else if (tree.getUnit().matches("(?i)|\\[[a-z][a-z0-9]*(,[a-z][a-z0-9]*)*\\]")) {
            ArrayList<String> units = new ArrayList<>();//[a-z][a-z0-9]*\\[[a-z][a-z0-9]*(,[a-z0-9]+)*\\]||
            Pattern p = Pattern.compile("(?i)[a-z][a-z0-9]*");
            Matcher m = p.matcher(tree.getUnit());
            while (m.find()) {
                units.add(m.group());
            }
            if (tree.getNodeLeft().equalsALL(tree.getNodeRight())) {
                listT.addAll(generateTransitions(tree.getNodeLeft(), currentIndex, localite));
            } else if (!tree.getNodeLeft().isLeaf() && tree.getNodeLeft().getNodeLeft().equalsANY(units)) {
                if (!tree.getNodeRight().isLeaf() && tree.getNodeRight().getNodeLeft().equalsANY(units)) {
                    if (tree.getNodeLeft().getNodeLeft().getUnit().equals(tree.getNodeRight().getNodeLeft().getUnit())) {
                        if (tree.getNodeLeft().getNodeLeft().getUnit().matches("move\\([a-z][a-z0-9]*\\)")){
                            localite = getNextLocalite(tree.getNodeLeft().getNodeLeft().getUnit());
                        }
                        Transition tr = new Transition(currentIndex, tree.getNodeLeft().getNodeLeft().getUnit(), succIndex, localite);
                        currentIndex = succIndex;
                        succIndex++;
                        listT.add(tr);
                    }//else interblocage
                } else if (!tree.getNodeRight().isLeaf()) {
                    if (tree.getNodeRight().getNodeLeft().getUnit().matches("move\\([a-z][a-z0-9]*\\)")){
                        localite = getNextLocalite(tree.getNodeRight().getNodeLeft().getUnit());
                    }
                    Transition tr = new Transition(currentIndex, tree.getNodeRight().getNodeLeft().getUnit(), succIndex, localite);
                    currentIndex = succIndex;
                    succIndex++;
                    listT.add(tr);
                    AST ast = tree.clone();
                    ast.setRight(tree.getNodeRight().getNodeRight());
                    listT.addAll(generateTransitions(ast, currentIndex, localite));
                }//else tree.getNodeRight().isLeaf() alors == stop or exit

            } else if (!tree.getNodeRight().isLeaf() && tree.getNodeRight().getNodeLeft().equalsANY(units)) {
                if (!tree.getNodeLeft().isLeaf()) {
                    if (tree.getNodeLeft().getNodeLeft().getUnit().matches("move\\([a-z][a-z0-9]*\\)")){
                        localite = getNextLocalite(tree.getNodeLeft().getNodeLeft().getUnit());
                    }
                    Transition tr = new Transition(currentIndex, tree.getNodeLeft().getNodeLeft().getUnit(), succIndex, localite);
                    currentIndex = succIndex;
                    succIndex++;
                    listT.add(tr);
                    AST ast = tree.clone();
                    ast.setRight(tree.getNodeLeft().getNodeRight());
                    listT.addAll(generateTransitions(ast, currentIndex, localite));
                }//else tree.getNodeLeft().isLeaf() alors == stop or exit
            } else if (tree.getNodeLeft().isLeaf()) {
                if (!tree.getNodeRight().isLeaf()) {
                    if (tree.getNodeRight().getNodeLeft().getUnit().matches("move\\([a-z][a-z0-9]*\\)")){
                        localite = getNextLocalite(tree.getNodeRight().getNodeLeft().getUnit());
                    }
                    Transition tr = new Transition(currentIndex, tree.getNodeRight().getNodeLeft().getUnit(), succIndex, localite);
                    currentIndex = succIndex;
                    succIndex++;
                    listT.add(tr);
                    AST ast = tree.clone();
                    ast.setRight(tree.getNodeRight().getNodeRight());
                    listT.addAll(generateTransitions(ast, currentIndex, localite));
                }
            } else if (tree.getNodeRight().isLeaf()) {
                if (!tree.getNodeLeft().isLeaf()) {
                    if (tree.getNodeLeft().getNodeLeft().getUnit().matches("move\\([a-z][a-z0-9]*\\)")){
                        localite = getNextLocalite(tree.getNodeLeft().getNodeLeft().getUnit());
                    }
                    Transition tr = new Transition(currentIndex, tree.getNodeLeft().getNodeLeft().getUnit(), succIndex, localite);
                    currentIndex = succIndex;
                    succIndex++;
                    listT.add(tr);
                    AST ast = tree.clone();
                    ast.setLeft(tree.getNodeLeft().getNodeRight());
                    listT.addAll(generateTransitions(ast, currentIndex, localite));
                }
            } else {
                AST astL = tree.clone();
                AST astR = tree.clone();
                int cur1 = currentIndex;
                int cur2 = currentIndex;

                if (tree.getNodeLeft().getNodeLeft().getUnit().matches("move\\([a-z][a-z0-9]*\\)")){
                    localite = getNextLocalite(tree.getNodeLeft().getNodeLeft().getUnit());
                }
                Transition tr1 = new Transition(currentIndex, tree.getNodeLeft().getNodeLeft().getUnit(), succIndex, localite);
                cur1 = succIndex;
                succIndex++;

                listT.add(tr1);
                astL.setLeft(tree.getNodeLeft().getNodeRight());
                listT.addAll(generateTransitions(astL, cur1, localite));

                if (tree.getNodeRight().getNodeLeft().getUnit().matches("move\\([a-z][a-z0-9]*\\)")){
                    localite = getNextLocalite(tree.getNodeRight().getNodeLeft().getUnit());
                }

                Transition tr = new Transition(currentIndex, tree.getNodeRight().getNodeLeft().getUnit(), succIndex, localite);
                cur2 = succIndex;
                succIndex++;
                listT.add(tr);
                astR.setRight(tree.getNodeRight().getNodeRight());
                listT.addAll(generateTransitions(astR, cur2, localite));

            }

        }

        /* Add transitions when unit is hide */
        else if (tree.getUnit().matches("(?i)hide [a-z][a-z0-9]*(,[a-z][a-z0-9]*)* in")) {
            //ArrayList<String> units = new ArrayList<>();//[a-z][a-z0-9]*\\[[a-z][a-z0-9]*(,[a-z0-9]+)*\\]||
            Pattern p = Pattern.compile("(?i)[a-z][a-z0-9]*");
            Matcher m = p.matcher(tree.getUnit());
            while (m.find()) {
                if (!m.group().equals("hide") && !m.group().equals("in")) {
                    tree.replaceALL(m.group());
                }
            }
            listT.addAll(generateTransitions(tree.getNodeRight(), currentIndex, localite));
        }

        return listT;
    }

    private int isExist(AST ast) {
        for (Action action:
             act) {
            if (ast.equalsALL(action.getAst())){
                return action.getSucIndex();
            }

        }
        return -1;
    }

}