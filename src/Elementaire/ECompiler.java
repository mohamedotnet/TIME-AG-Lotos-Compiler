package Elementaire;

import Elementaire.Lexical.Lexical;
import Elementaire.STE.STE;
import Elementaire.Semantic.Semantics;
import Elementaire.Syntax.AST;
import Elementaire.Syntax.Syntax;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import Graph.GraphStyle;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ECompiler {

    private boolean isLexicalValid(String expression) {
        return new Lexical().Accepte(expression);
    }

    private boolean isSyntaxValid(ArrayList<String> units) {
        return new Syntax().Parsing(units) != null;
    }

    private ArrayList<String> getUnits(String expression) {
        return new Lexical().getTokensER(expression);
    }

    private AST generateAST(ArrayList<String> units) {
        return new Syntax().Parsing(units);
    }

    private STE generateSTE(AST tree, String localite) {
        return new Semantics().generate(tree, localite);
    }

    private static boolean nodeExists(Graph graph, String id) {
        for (Node nd :
                graph) {
            if (nd.getId().equals(id)) return true;
        }
        return false;
    }

    private String label(String id){
        Pattern pattern = Pattern.compile("(?i)[a-z]\\d");
        Matcher matcher = pattern.matcher(id);
        matcher.find();
        return matcher.group();
    }

    private Graph generateElementaryGraph(STE ste, String idNode) {
        Graph graph = new SingleGraph("STE");

        Node nodeSrc, nodeDest;
        for (int i = 0; i < ste.gettr().size(); i++) {
            /* Adding Source and Destination nodes */
            if (nodeExists(graph, ste.gettr().get(i).getSrc() + idNode)) {
                nodeDest = graph.addNode(ste.gettr().get(i).getDest() + idNode);
                /* TODO: Use regex to extract number from ID */

                nodeDest.addAttribute("ui.label", label("S" + nodeDest.getId()));

                graph.addEdge(ste.gettr().get(i).getSrc() + idNode + nodeDest.getId(),
                        graph.getNode(ste.gettr().get(i).getSrc() + idNode),
                        nodeDest, true);
                graph.getEdge(ste.gettr().get(i).getSrc() + idNode + nodeDest.getId()).addAttribute("ui.label",
                        ste.gettr().get(i).getAct() + " " + ste.gettr().get(i).getLocalite());
            } else {
                nodeSrc = graph.addNode(ste.gettr().get(i).getSrc() + idNode);
                /* TODO: change label */
                nodeSrc.addAttribute("ui.label", label("S" + nodeSrc.getId()));
                nodeDest = graph.addNode(ste.gettr().get(i).getDest() + idNode);

                /* TODO: change label */
                nodeDest.addAttribute("ui.label", label("S" + nodeDest.getId()));

                graph.addEdge(nodeSrc.getId() + nodeDest.getId(), nodeSrc, nodeDest, true);
                graph.getEdge(nodeSrc.getId() + nodeDest.getId()).addAttribute("ui.label",
                        ste.gettr().get(i).getAct() + " " + ste.gettr().get(i).getLocalite());
            }
        }

        GraphStyle.style(graph);
        return graph;

    }

    private void printAST(AST ast){
        if (ast != null){
            System.out.println(ast.getUnit());
            printAST(ast.getNodeLeft());
            printAST(ast.getNodeRight());
        }
    }
    public Graph generateEGraphe(String expression, String localite, String idNode) {
        if (isLexicalValid(expression)) {
            ArrayList<String> units = getUnits(expression);

            if (units.get(0).matches("delta\\([0-9]*\\)")){
                for (int i = 1; i < units.size()-1; i++){
                    if (units.get(i).matches("delta\\([0-9]*\\)")) {
                        return null;
                    }
                }
                AST ast = new AST(units.get(0), null, null);
                units.remove(0);
                if (isSyntaxValid(units)){
                    ast.setRight(generateAST(units));

                    printAST(ast);

                    return generateElementaryGraph(
                            generateSTE(ast, localite),
                            idNode
                    );
                }
            }
            else {
                if (isSyntaxValid(units)) {
                    return generateElementaryGraph(
                            generateSTE(generateAST(units), localite),
                            idNode
                    );
                }
            }
        }
        return null;
    }

    /* This main is used to test the Elementary Compiler */
    public static void main(String[] args) {
        System.out.println("Expression LOTOS: ");
        String expression = new Scanner(System.in).nextLine();
        System.out.println("Localité initiale: ");
        String localite = new Scanner(System.in).nextLine();
        ECompiler compiler = new ECompiler();

        Graph graph = compiler.generateEGraphe(expression, localite,
                Character.toString((char) (ThreadLocalRandom.current().nextInt(97, 122))));
        graph.display();



        /*if (compiler.isLexicalValid(expression)) {
            ArrayList<String> units = compiler.getUnits(expression);
            if (compiler.isSyntaxValid(units)) {
                Graph graph = compiler.generateElementaryGraph(
                        compiler.generateSTE(
                                compiler.generateAST(units),
                                localite
                        ), Character.toString((char) (ThreadLocalRandom.current().nextInt(97, 122)))
                );
                GraphStyle.style(graph);
                graph.display();
            } else {
                System.err.println("SYNTAX ERROR");
            }
        } else {
            System.err.println("LEXICAL ERROR");
        }*/

    }
}

