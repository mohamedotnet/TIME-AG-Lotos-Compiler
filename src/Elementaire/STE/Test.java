package Elementaire.STE;

import java.util.ArrayList;


public class Test {
    /*public static void main(String args[]) {
        ArrayList<Integer> s = new ArrayList<Integer>();
        ArrayList<String> act = new ArrayList<String>();
        STE ste;
        int s0;
        for (int i = 0; i < 6; i++) {
            s.add(i);
        }
        act.add("code");
        act.add("retrait");
        act.add("autre");
        s0 = s.get(0);
        ste = new STE(s, s0, act);
        ste.addTransition(new Transition(s0, act.get(0), s.get(1)));
        ste.addTransition(new Transition(s0, act.get(0), s.get(2)));
        ste.addTransition(new Transition(s.get(1), act.get(1), s.get(3)));
        ste.addTransition(new Transition(s.get(1), act.get(2), s.get(4)));
        ste.addTransition(new Transition(s.get(2), act.get(2), s.get(5)));
        System.out.println("Les transitions:");
        Transition tr = new Transition();
        for (int i = 0; i < ste.gettr().size(); i++) {
            tr = ste.gettr().get(i);
            System.out.println("[" + tr.getSrc() + "]" + " ----" + tr.getAct() + "----> " + "[" + tr.getDest() + "]");
        }
        System.out.println("Les États finaux:");
        ArrayList<Integer> ss = ste.getFinalStates();
        for (int i = 0; i < ss.size(); i++) {
            System.out.print(" [" + ss.get(i) + "] ");
        }

    }*/
}
