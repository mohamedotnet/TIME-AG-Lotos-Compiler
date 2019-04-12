package Agent;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import Graph.GraphStyle;

import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;


public class Plan {
    private String expression;

    public Plan(String input) {
        expression = input;
    }

    /*
    public String[] generateGraph() {
        Graph plan = new SingleGraph("Plan");
        String[] nds = expression.split("\\s?=\\s?", 2);
        String[] node = nds[0].split("\\s?>>\\s?|\\s?\\Q|||\\E\\s?");

        return nds;
    }*/



    private char randomChar() {
        int rand = ThreadLocalRandom.current().nextInt(97, 122);
        System.out.println(rand);
        return (char) (rand);
    }

    public static String extractGeneralExpression(String expression){
        String[] nds = expression.split("\\s?=\\s?", 2);
        if (nds.length <= 1){
            return null;
        }
        return nds[1];
    }

    public static String getExpressionName(String expression){
        String[] nds = expression.split("\\s?=\\s?", 2);
        if (nds.length <= 1){
            return null;
        }
        return nds[0];
    }

    /* TODO: Turing machine method to test syntax */
    public static int isBalanced(String expression) {
        Stack<Character> stackLeft = new Stack<>();
        Stack<Character> stackRight = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '(')
                stackLeft.push(expression.charAt(i));
            else if (expression.charAt(i) == ')') {
                stackRight.push(expression.charAt(i));
            }
        }
        if (stackLeft.size() == stackRight.size()) {
            return 1;
        }else if (stackLeft.size() < stackRight.size()){
            return 0;
        }else {
            return -1;
        }

    }

    private static String buildString(char c, char d, char ... args){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(c);
        stringBuilder.append(d);

        if (args.length == 0){
            stringBuilder.append(args[0]);
        }

        return stringBuilder.toString();
    }
    public static String[] getElementaryExpressions(String expression){
        ArrayList<String> expressions = new ArrayList<>();
        String exp = "";

        for (int i = 0; i < expression.length(); i++){
            if ((expression.charAt(i) == '|' && expression.charAt(i+1) == '|' & expression.charAt(i+2) == '|')){
                if (Plan.isBalanced(exp) == 1) {
                    expressions.add(exp);
                    exp = "";
                    i += 3;
                }else {
                    if (Plan.isBalanced(exp) == 0){
                        return null;
                    }

                    exp += Plan.buildString(expression.charAt(i), expression.charAt(i+1), expression.charAt(i+2));
                    i += 3;
                }
            }else if ((expression.charAt(i) == '>' && expression.charAt(i+1) == '>')){
                if (Plan.isBalanced(exp) == 1) {
                    expressions.add(exp);
                    exp = "";
                    i += 2;
                }else {
                    if (Plan.isBalanced(exp) == 0){
                        return null;
                    }
                    exp += Plan.buildString(expression.charAt(i), expression.charAt(i+1));
                    i += 2;
                }
            }
            exp += expression.charAt(i);
            if (i == expression.length()-1){
                if (Plan.isBalanced(exp) == 1){
                    expressions.add(exp);
                }else {
                    return null;
                }
            }
        }
        //return expression.split("\\s?>>\\s?|\\s?\\Q|||\\E\\s?");
        return expressions.toArray(new String[0]);
    }

    public Graph generatePlanGraph() {
        /* Create Graph */
        Graph plan = new SingleGraph("Plan");
        String[] nds = expression.split("\\s?=\\s?", 2);
        if (nds.length <= 1){
            return null;
        }
        String[] nodes = getElementaryExpressions(nds[1]);

        for (String s:
            nodes){
            if (Plan.isBalanced(s) != 1){
                System.out.println("Unbalanced");
                return null;
            }
        }
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
        System.out.println(plan.toString());
        //System.out.println(plan.getNode(0).getId());
        GraphStyle.style(plan);
        return plan;
    }

}
