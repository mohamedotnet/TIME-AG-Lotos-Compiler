import Agent.Plan;
import org.graphstream.graph.Graph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import Graph.GraphStyle;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CompilerGUI implements Runnable{

    /* TODO: handle thread exit */

    private JTextField localite, expression;

    @Override
    public void run() {
        JFrame frame;
        JLabel labelLocalite, labelExpression;
        JButton compile, saveAgent, saveElementary;

        frame = new JFrame("Time-AG LOTOS Compiler");


        /* Locality Group */
        labelLocalite = new JLabel("Localité: ");
        labelLocalite.setBounds(30, 10, 100, 28);

        localite = new JTextField();
        localite.setBounds(30, 35, 630, 28);
        localite.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1,true));

        /* Expression Group */

        labelExpression = new JLabel("Expression Time-AG LOTOS: ");
        labelExpression.setBounds(30, 60, 100, 28);

        expression = new JTextField();
        expression.setBounds(30, 85, 630, 28);
        expression.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1,true));

        compile = new JButton("Compile");
        compile.setBounds(80, 127, 100,40);

        /* Save Agent Plan Expression Button */
        saveAgent = new JButton("Save Global Expression");
        saveAgent.setBounds(200, 127, 200,40);

        /* Save Elementary Plan Expressions Button */
        saveElementary = new JButton("Save Elem. Expression");
        saveElementary.setBounds(420, 127, 200,40);

        frame.add(compile);
        frame.add(saveAgent);
        frame.add(saveElementary);
        frame.add(localite);
        frame.add(labelLocalite);
        frame.add(labelExpression);
        frame.add(expression);

        /* Handle saveAgent Button Event */
        saveAgent.addActionListener((ActionEvent e) ->{
            if (expression.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Please enter an expression!");
            }else {
                if (new Compiler(localite.getText(), expression.getText()).isVerified()) {
                    /*
                    FileOutputStream fop;
                    File file;
                    try {
                        String entry = "{\"" + Plan.extractGeneralExpression(expression.getText()) + "\": null}";
                        file = new File("global_expression.txt");
                        fop = new FileOutputStream(file, true);

                        byte[] contentInBytes = Plan.extractGeneralExpression(expression.getText()+"\n").getBytes();

                        fop.write(contentInBytes, 0, contentInBytes.length);
                        fop.close();
                        JOptionPane.showMessageDialog(null, "Expression successfully saved!");
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }*/
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(Plan.getExpressionName(expression.getText()), Plan.extractGeneralExpression(expression.getText()));

                    JSONArray expressionList = new JSONArray();
                    expressionList.put(jsonObject);

                    FileWriter file;
                    try {
                        file = new FileWriter("global_expressions.txt", true);
                        file.append(expressionList.toString()+"\n");
                        file.flush();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                }else {
                    JOptionPane.showMessageDialog(null, "Syntax Error!");
                }
            }
        });
        // P0|=(delta(2)a;b;exit<>c;d;exit{4})|||(move(l1);get;exit<>e;f;exit)
        // P0|=(a;exit|||b;exit{1})>>(c;exit)
        // P0|=(a;b;exit<>c;d;exit{4})|||(move(l1);get;exit<>e;f;exit)
        // P0|=(a;b;exit<>c;d;exit)|||(move(l1);get;exit<>e;f;exit)


        /* Handle compile Button Event */
        compile.addActionListener((ActionEvent e)-> {
            if (expression.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Please enter an expression!");
            }else {
                if (new Compiler(localite.getText(), expression.getText()).isVerified()) {
                    Graph graph = new Compiler(localite.getText(), expression.getText()).completeGraph();
                    GraphStyle.style(graph);
                    Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
                    viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
                    viewer.addDefaultView(true);
                    viewer.enableAutoLayout();
                    //PO|=(a;b;exit)>>(c;d;exit{1})

                }else {
                    JOptionPane.showMessageDialog(null, "Syntax Error!");
                }
            }
        });
        frame.setSize(700, 220);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    public static void main(String[] args){
        new Thread(new CompilerGUI()).start();
    }
}
