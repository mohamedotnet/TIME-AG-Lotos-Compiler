package Elementaire.Syntax;

import Elementaire.Lexical.Lexical;

import java.util.ArrayList;
import java.util.Scanner;


public class Test {
    public static void main(String args[]) {
        ArrayList<String> units;
        Lexical mssd = new Lexical();
        Scanner lire = new Scanner(System.in);
        System.out.println("l'expression LOTOS :");
        String s;
        //exemple: a ; b ; exit |[a,b]| a ; c hide a in a;stop[]b;exit
        s = lire.nextLine();
        System.out.println("\n_____________________________les unités lexicales _______________________________\n");
        units = mssd.getTokensER(s);
        System.out.println("\n_______________________________l'analyseur lexicale_______________________________\n");
        if (mssd.Accepte(s)) {
            System.out.println("\nLES UNITES LEXICALES SONT ACCEPTEES\n");
            Syntax syn = new Syntax();
            AST ast = syn.Parsing(units);
            System.out.println("\n_______________________________l'analyseur sytaxique_______________________________\n");
            if (ast == null) {
                System.err.println("ERREUR SYNTAXIQUE\n");
            } else {
                System.out.println("\nPAS D'ERREUR SYNTAXIQUE");
                System.out.println("\n_______________________________Affichage du AST_______________________________\n");
                ast.traverse();
            }

        } else {
            System.err.println("ERREUR: il y a des parties de code qui n'appartiennent pas au langage LOTOS");
        }

    }
}
