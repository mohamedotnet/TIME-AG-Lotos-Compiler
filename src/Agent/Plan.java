package Agent;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.SingleGraph;
import Graph.GraphStyle;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Plan {
    private String expression;

    public Plan(String input) {
        expression = input;
    }

    public String[] generateGraph() {
        Graph plan = new SingleGraph("Plan");
        String[] nds = expression.split("\\s?=\\s?", 2);
        String[] node = nds[0].split("\\s?>>\\s?|\\s?\\Q|||\\E\\s?");

        return nds;
    }


    public char randomChar() {
        int rand = ThreadLocalRandom.current().nextInt(97, 122);
        System.out.println(rand);
        return (char) (rand);
    }

    public Graph generatePlanGraph() {
        /* Create Graph */
        Graph plan = new SingleGraph("Plan");
        String[] nds = expression.split("\\s?=\\s?", 2);
        String[] nodes = nds[1].split("\\s?>>\\s?|\\s?\\Q|||\\E\\s?");

        Node nodeT;
        for (int i = 0; i < nodes.length; i++) {
            char id = randomChar();
            nodeT = plan.addNode(nodes[i]);
            nodeT.addAttribute("ui.label", "P^" + id);

        }
        /* Adjust Positions */
        int positionX = 8, positionY = 3;
        for (Node node : plan) {
            node.setAttribute("x", positionX);
            node.setAttribute("y", positionY);
            positionX += 1;
        }
        /* Calculating Xs of nodes */
        int sumOfAbsNodes = 0;
        for (Node node : plan) {
            sumOfAbsNodes += Integer.parseInt(node.getAttribute("x").toString());
            System.out.println("node: " + Integer.parseInt(node.getAttribute("x").toString()));
        }

        /* Adding the root */
        plan.addNode(nds[0]);
        Node root = plan.getNode(nds[0]);
        root.addAttribute("ui.label", nds[0]);
        root.setAttribute("y", 4);
        root.setAttribute("x", (double) sumOfAbsNodes / (double) (plan.getNodeCount() - 1));

        System.out.println("Root x:" + root.getAttribute("x").toString());
        System.out.println();
        /* Adding Edges */


        for (Node node : plan) {
            if (!node.getId().equals(nds[0])){
                plan.addEdge(nds[0] + node.getId(), nds[0], node.getId(), true);
            }
        }
        //System.out.println(plan.getNode(0).getId());
        GraphStyle.style(plan);
        return plan;
    }


    public static void main(String args[]) {
        Plan plan = new Plan("P0| = (move(l2){2}; get_copies(l2){1}; move(l2){2}; exit<>" +
                "Ali!(get_copies(l2)){1}; exit) |||(meet(Ali)@t[4<=t<=7]; exit)" +
                "|||(move(l1);exit)>> (a; b; exit)");

        Graph graph = plan.generatePlanGraph();

        /*String planExpression = "(meet(Ali)@t[4<=t<=7]; exit)|||(move(l1);exit)>> a; b; exit";
        Pattern pattern  = Pattern.compile("\\((.?)*\\;exit\\)");
        Matcher matcher = pattern.matcher(planExpression);
        matcher.matches();
        matcher.group(1);*/

        for (Node node:
             graph) {
            System.out.println(node.getIndex());
        }
        GraphStyle.style(graph);
        graph.display(false);

        /*for (String node : plan.generateGraph()){
            System.out.println(node);
        }*/


    }
}
