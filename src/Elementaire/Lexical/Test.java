package Elementaire.Lexical;

import java.util.Scanner;


public class Test {
    public static void main(String args[]) {//regex
        Lexical mssd = new Lexical();
        Scanner lire = new Scanner(System.in);
        System.out.println("Entrez votre Programme:");
        String s;

        while (lire.hasNextLine()) {
            //exemple: PROCESS P1[a,b,c]:=a;b;exit|[f,g]|a;c;nil>>P1[ a , b , c ] hide a in
            s = lire.nextLine();
            /*System.out.println("\n");
            System.out.println("Méthode 1: Utilisation du 'split': \n");
            mssd.getTokensS(s);

            System.out.println("\nMéthode 2: Utilisation des expressions régulières: \n");
            mssd.getTokensER(s);
            System.out.println(mssd.getTokensER(s).toString());*/
            System.out.println("\nResultat: ");
            if (mssd.Accepte(s)) {
                System.out.println("\nToutes les unités sont acceptées \n");
            } else {
                System.err.println("Erreur: il y'a des parties de code qui n'appartient pas au langage LOTOS !!!");
            }
        }
    }
}