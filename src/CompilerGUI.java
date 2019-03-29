import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CompilerGUI implements Runnable{

    private JFrame frame;
    private JTextField localite, expression;
    private JLabel labelLocalite, labelExpression;
    private JButton compile;


    @Override
    public void run() {
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
        compile.setBounds(300, 127, 100,40);

        frame.add(compile);
        frame.add(localite);
        frame.add(labelLocalite);
        frame.add(labelExpression);
        frame.add(expression);

        compile.addActionListener((ActionEvent e)->{
            new Thread(new Compiler(localite.getText(), expression.getText())).start();
        });
        frame.setSize(700, 220);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    public static void main(String[] args){
        new Thread(new CompilerGUI()).start();
    }
}
