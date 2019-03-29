import Elementaire.STE.Transition;

import java.util.Scanner;

public class Graphs {

    /*
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
    }*/
    public static void main(String[] args) {
        System.out.println(new Scanner(System.in).nextLine().matches("exit\\{[0-9]*\\}"));
    }
}

