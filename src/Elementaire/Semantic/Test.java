package Elementaire.Semantic;

import Elementaire.Lexical.Lexical;
import Elementaire.STE.STE;
import Elementaire.Syntax.AST;
import Elementaire.Syntax.Syntax;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;

import java.util.ArrayList;
import java.util.Scanner;

public class Test {

    public static boolean nodeExists(Graph graph, String id) {
        for (Node nd :
                graph) {
            if (nd.getId().equals(id)) return true;
        }
        return false;
    }

    public static void main(String args[]) {
        ArrayList<String> units;
        Semantics sem = new Semantics();
        Lexical mssd = new Lexical();
        Scanner lire = new Scanner(System.in);
        System.out.println("L'expression LOTOS:");
        String s;
        //exemple: a ; b ; exit |[a,b]| a ; c;stop[]b;exit
        s = lire.nextLine();
        System.out.println("Localité: ");
        String localite = lire.nextLine();
        System.out.println("\n_______________________les unités lexical_______________________\n");
        units = mssd.getTokensER(s);
        System.out.println("\n_______________________l'analyseur lexical_______________________\n");
        if (mssd.Accepte(s)) {
            System.out.println("LES UNITES LEXICALE SONT ACCEPTER");
            Syntax syn = new Syntax();
            AST ast = syn.Parsing(units);
            System.out.println("\n_______________________l'analyseur sytaxique_______________________\n");
            if (ast == null) {
                System.out.println("ERREUR SYNTAXIQUE");
            } else {
                System.out.println("SYNTAXE CORRECTE");
                System.out.println("\n_______________________Affichage du AST_______________________\n");
                ast.traverse();
                STE ste = sem.generate(ast, localite);
                System.out.println("\n_______________________Affichage du STE_______________________\n");

                /* Graph graph = new SingleGraph("STE");

                Node nodeSrc, nodeDest;
                for (int i = 0; i < ste.gettr().size(); i++) {
                    *//* Adding Source and Destination nodes *//*
                    if (nodeExists(graph, Integer.toString(ste.gettr().get(i).getSrc()))) {
                        nodeDest = graph.addNode(Integer.toString(ste.gettr().get(i).getDest()));
                        nodeDest.addAttribute("ui.label", "S" + nodeDest.getId());

                        graph.addEdge(ste.gettr().get(i).getSrc() + nodeDest.getId(),
                                graph.getNode(Integer.toString(ste.gettr().get(i).getSrc())),
                                nodeDest, true);
                        graph.getEdge(ste.gettr().get(i).getSrc() + nodeDest.getId()).addAttribute("ui.label",
                                ste.gettr().get(i).getAct());
                    } else {
                        nodeSrc = graph.addNode(Integer.toString(ste.gettr().get(i).getSrc()));
                        nodeSrc.addAttribute("ui.label", "S" + nodeSrc.getId());
                        nodeDest = graph.addNode(Integer.toString(ste.gettr().get(i).getDest()));
                        nodeDest.addAttribute("ui.label", "S" + nodeDest.getId());

                        graph.addEdge(nodeSrc.getId() + nodeDest.getId(), nodeSrc, nodeDest, true);
                        graph.getEdge(nodeSrc.getId() + nodeDest.getId()).addAttribute("ui.label",
                                ste.gettr().get(i).getAct());
                    }

                    *//* Adding edges between them *//*

                    //System.out.println(ste.gettr().get(i).toString1());
                }
                for (Node no :
                        graph) {
                    System.out.println(no.getId());
                }
                *//* Display *//*
                System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

                String stylesheet = "node { fill-color: red; size : 40px; }" +
                        "edge { shape: line; arrow-size : 10px, 4px; }";

                graph.addAttribute("ui.stylesheet", stylesheet);

                graph.addAttribute("ui.antialias");
                graph.addAttribute("ui.quality");

                graph.display();*/
                System.out.print("STE = ( {");
                for (int i = 0; i < ste.getS().size() - 1; i++) {
                    System.out.print(ste.getS().get(i) + ",");
                }
                if (ste.getS().size() - 1 >= 0) {
                    System.out.print(ste.getS().get(ste.getS().size() - 1) + "} ," + ste.getS0() + ", {");
                } else {
                    System.out.print("} ," + ste.getS0() + ", {");
                }
                for (int i = 0; i < ste.gettr().size() - 1; i++) {
                    System.out.print(ste.gettr().get(i).toString() + ",");
                }
                if (ste.gettr().size() - 1 >= 0) {
                    System.out.print(ste.gettr().get(ste.gettr().size() - 1).toString() + "} , {");
                } else {
                    System.out.print("} , {");
                }
                for (int i = 0; i < ste.getAct().size() - 1; i++) {
                    System.out.print(ste.getAct().get(i) + ",");
                }
                if (ste.getAct().size() - 1 >= 0) {
                    System.out.print(ste.getAct().get(ste.getAct().size() - 1) + "} )");
                } else {
                    System.out.print("} )");
                }
                System.out.println("\nLES TRANSITIONS :");
                for (int i = 0; i < ste.gettr().size(); i++) {
                    System.out.println(ste.gettr().get(i).toString1());
                }
                System.out.println("Les États finaux:");
                ArrayList<Integer> ss = ste.getFinalStates();
                for (int i = 0; i < ss.size(); i++) {
                    System.out.println("[" + ss.get(i) + "]");
                }

            }
        } else {
            System.err.println("ERREUR: il y a des parties de code qui n'appartiennent pas au langage LOTOS");
        }
    }
}
