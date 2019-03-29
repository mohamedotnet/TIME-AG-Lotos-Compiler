package Elementaire.STE;

import java.util.ArrayList;


public class STE {
    private ArrayList<Integer> s = new ArrayList<Integer>();
    private ArrayList<String> act = new ArrayList<String>();
    private int s0;
    private ArrayList<Transition> tr = new ArrayList<Transition>();

    public STE(ArrayList<Integer> s, int s0, ArrayList<String> act) {
        this.s = s;
        this.s0 = s0;
        this.act = act;
    }

    public STE() {

    }

    public ArrayList<Integer> getS() {
        return s;
    }

    public int getS0() {
        return s0;
    }

    public ArrayList<String> getAct() {
        return act;
    }

    public ArrayList<Transition> gettr() {
        return tr;
    }

    public void setS(ArrayList<Integer> s) {
        this.s = s;
    }

    public void setS0(int s0) {
        this.s0 = s0;
    }

    public void setAct(ArrayList<String> act) {
        this.act = act;
    }

    public void addState(int s) {
        if (!this.s.contains(s))
            this.s.add(s);
    }

    public void addTransition(Transition tr) {
        if (!this.tr.contains(tr))
            this.tr.add(tr);
    }

    public void addAction(String a) {
        if (!this.act.contains(a))
            this.act.add(a);
    }

    public ArrayList<Integer> succ(int s) {
        ArrayList<Integer> listA = null;
        for (int i = 0; i < tr.size(); i++) {
            if (tr.get(i).getSrc() == s) {
                listA.add(tr.get(i).getDest());
            }
        }
        return listA;
    }

    public ArrayList<String> out(int s) {
        ArrayList<String> listA = null;
        for (int i = 0; i < tr.size(); i++) {
            if (tr.get(i).getSrc() == s) {
                listA.add(tr.get(i).getAct());
            }
        }
        return listA;
    }

    public String Lambda(Transition tr) {
        return tr.getAct();
    }

    public ArrayList<Integer> getFinalStates() {
        ArrayList<Integer> s = new ArrayList<Integer>();
        boolean v;
        for (int i = 0; i < tr.size(); i++) {
            v = false;
            for (int j = 1; j < tr.size(); j++) {
                if (tr.get(i).getDest() == tr.get(j).getSrc()) {
                    v = true;
                    break;
                }
            }
            if (v == false && !s.contains(tr.get(i).getDest())) {
                s.add(tr.get(i).getDest());
            }
        }
        return s;
    }

    ///////////////pile S////////////
    public void empiler(int n) {
        s.add(n);
    }

    public int depiler() {
        if (estVide()) ;
        return s.remove(s.size() - 1);
    }

    public boolean estVide() {
        return s.size() == 0;
    }

}