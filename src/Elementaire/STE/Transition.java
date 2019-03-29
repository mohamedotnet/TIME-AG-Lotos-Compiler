package Elementaire.STE;


public class Transition {
    private int src;
    private String act;
    private int dest;
    private String localite;

    public Transition(int src, String act, int dest, String localite) {
        this.src = src;
        this.act = act;
        this.dest = dest;
        this.localite = localite;
    }

    public Transition() {
    }

    public int getSrc() {
        return src;
    }

    public String getAct() {
        return act;
    }

    public int getDest() {
        return dest;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public void setDest(int dest) {
        this.dest = dest;
    }

    public String getLocalite() { return localite; }

    public void setLocalite(String localite) { this.localite = localite; }

    public String toString() {
        return ("(" + src + "," + act + "," + dest + "," + localite + ")");
    }

    public String toString1() {
        return ("[" + src + "]" + " -" + act + "-> " + "[" + dest + " @ " + localite + "]");
    }

    public Transition clone() {
        return new Transition(src, act, dest, localite);
    }

    public boolean equals(Transition t1, Transition t2) {
        return t1.toString().equals(t2.toString());
    }
}
