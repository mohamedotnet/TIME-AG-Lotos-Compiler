import Agent.Plan;
import Elementaire.ECompiler;
import org.graphstream.graph.Graph;

import Graph.GraphStyle;
import org.graphstream.graph.implementations.Graphs;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Compiler implements Runnable{

    public String localite, expression;
    public Compiler(String localite, String expression){
        this.localite = localite;
        this.expression = expression;
    }

    private String getExpression(String input) {
        Pattern pattern = Pattern.compile("\\((.*?)\\)");
        Matcher matcher = pattern.matcher(input);
        matcher.matches();
        return matcher.group(1);
    }

    private Graph generateTotalGraph(Graph result, Graph elementary, Graph output, int index){
        result = Graphs.merge(elementary, result);
        result.addEdge(output.getNode(index).getId() + elementary.getNode(0).getId(), output.getNode(index).getId(), elementary.getNode(0).getId(), true);

        return result;
    }

    public Graph completeGraph(){

        /* TODO: Verify Syntax of expression */

        Plan plan = new Plan(expression);
        Graph output = plan.generatePlanGraph();
        Graph elementary, result = output;

        for (int i = 0; i < output.getNodeCount(); i++) {
            if (!output.getNode(i).getId().matches("(?i)[a-z][a-z0-9]*\\Q|\\E")) {
                String idNode = output.getNode(i).getAttribute("ui.label").toString().replaceAll("(?i)[a-z][a-z0-9]*\\^", "");

                /* Extract intentions */
                String[] intensions = getExpression(output.getNode(i).getId()).split("<>");
                if (intensions.length > 1) {
                    int j = 0;
                    do {
                        elementary = new ECompiler().generateEGraphe(intensions[j], localite, idNode + j);
                        //Graph generated = new ECompiler().generateEGraphe(intensions[j + 1], localite, idNode + j + 1);
                        result = generateTotalGraph(result, elementary, output, i);
                        j++;
                    } while (j < intensions.length);
                } else {
                    elementary = new ECompiler().generateEGraphe(getExpression(output.getNode(i).getId()), localite, idNode);
                    result = generateTotalGraph(result, elementary, output, i);
                }

                //elementary.getNode(0)
            }
        }

        return result;
    }

    @Override
    public void run() {
        Graph complete = completeGraph();
        GraphStyle.style(complete);
        complete.display();
    }

    public static void main(String[] args){
        Compiler compiler = new Compiler(new Scanner(System.in).nextLine(), new Scanner(System.in).nextLine());
        Graph complete = compiler.completeGraph();
        GraphStyle.style(complete);
        complete.display();

    }
}
