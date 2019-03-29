package Elementaire.STE;

public class Node {
    private String valeur;
    private Node gauche, droit;

    public Node(String x) {
        valeur = x;
    }

    public Node(String x, Node g, Node d) {
        valeur = x;
        gauche = g;
        droit = d;
    }

    public String getValeur() {
        return (valeur);
    }

    public Node getSousNodeGauche() {
        return (gauche);
    }

    public Node getSousNodeDroit() {
        return (droit);
    }

    public void ajouterNodeGauche(String v) {
        gauche = new Node(v, null, null);
    }

    public void ajouterNodeDroit(String v) {
        droit = new Node(v, null, null);
    }

    public String toString(Node n) {
        return (valeur + " " + n.getSousNodeGauche().valeur + " " + n.getSousNodeDroit().valeur);
    }

    public boolean equivalent(Node n) {
        return (n.getSousNodeGauche().valeur.equals(n.getSousNodeDroit().valeur));
    }

    /**
     * Affiche l'arbre selon un parcours prefixe
     */
    public void ParcoursPrefixe() {
        System.out.println(getValeur());
        if (getSousNodeGauche() != null)
            getSousNodeGauche().ParcoursPrefixe();
        if (getSousNodeDroit() != null)
            getSousNodeDroit().ParcoursPrefixe();
    }

    /**
     * Affiche l'arbre selon un parcours infixe
     */
    public void ParcoursInfixe() {
        if (getSousNodeGauche() != null)
            getSousNodeGauche().ParcoursInfixe();
        System.out.println(getValeur());
        if (getSousNodeDroit() != null)
            getSousNodeDroit().ParcoursInfixe();
    }

    /**
     * Affiche l'arbre selon un parcours postfixe
     */
    public void ParcoursPostfixe() {
        if (getSousNodeGauche() != null)
            getSousNodeGauche().ParcoursPostfixe();
        if (getSousNodeDroit() != null)
            getSousNodeDroit().ParcoursPostfixe();
        System.out.println(getValeur());
    }

    public boolean recherche(String value) {
        if (value.equals(getValeur()))
            return true;
        if (getSousNodeGauche() != null)
            return (getSousNodeGauche().recherche(value));
        if (getSousNodeDroit() != null)
            return (getSousNodeDroit().recherche(value));
        return false;
    }

    public Node insert(String[] value) {
        Node n = null;
        return n;
    }

}
