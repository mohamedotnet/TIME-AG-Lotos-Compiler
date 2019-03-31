package Elementaire.Lexical;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Lexical {

    public ArrayList<String> getTokensER(String s) {
        ArrayList<String> units = new ArrayList<>();// [a-z][a-z0-9]*\\[[a-z][a-z0-9]*(,[a-z0-9]+)*\\]||

        Pattern p = Pattern.compile(
                "(?i)hide [a-z][a-z0-9]*(,[a-z][a-z0-9]*)* in |\\[[a-z][a-z0-9]*(,[a-z][a-z0-9]*)*\\]\\|\\[\\]|\\[>|\\[\\]|::=|;|\\(|\\)|>>|\\Q|||\\E|\\Q||\\E|\\s|move\\([a-z][a-z0-9]*\\)|[a-z][a-z0-9]*@\\w\\d\\[[0-9](<|<=)\\w\\d(<|<=)[0-9]\\]|[a-z][a-z0-9]*\\{[0-9]*\\}|delta\\([0-9]*\\)|[a-z][a-z0-9]*");
        Matcher m = p.matcher(s);
        while (m.find()) {
            units.add(m.group());
            System.out.println(m.group() + " ----------> " + identifieur(m.group()));
        }
        return units;
    }


    public boolean Accepte(String s) {
        s = s.replaceAll(
                "(?i)hide [a-z][a-z0-9]*(,[a-z][a-z0-9]*)* in |\\[[a-z][a-z0-9]*(,[a-z][a-z0-9]*)*\\]\\||(.*)|\\[\\]|\\[>|\\[\\]|::=|\\s;\\s|\\(|\\)|>>|\\Q|||\\E|\\Q||\\E|\\s|@\\w\\d\\[[0-9](<|<=)\\w\\d(<|<=)[0-9]\\]|move\\([a-z][a-z0-9]*\\)|delta\\([0-9]*\\)|[a-z][a-z0-9]*\\{[0-9]*\\}",
                "");
        return s.equals("");
    }

    private String identifieur(String s) {
        String unite;
        if (s.matches("(?i)exit|exit\\{[0-9]*\\}|stop|exit|hide|in")) {
            unite = "Mot clé";
            // System.err.println(s);
        } else if (s.matches("(\\s)*::=(\\s)*") || s.matches("(\\s)*;(\\s)*") || s.matches("(?i)hide [a-z][a-z0-9]*(,[a-z][a-z0-9]*)* in ")
                || s.matches("(\\s)*\\Q|||\\E(\\s)*") || s.matches("(\\s)*\\Q||\\E(\\s)*") || s.matches("(\\s)*\\[\\](\\s)*")
                || s.matches("(?i)\\|\\[[a-z][a-z0-9]*(,[a-z][a-z0-9]*)*\\]\\|") || s.matches("(\\s)*>>(\\s)*") || s.matches("(\\s)*\\[>(\\s)*")
                || s.matches("delta\\([0-9]*\\)")) {
            unite = "Operateur";
        } else if (s.matches("move\\([a-z][a-z0-9]*\\)")) {
            unite = "Operands";
        } else if (s.equals(",") || s.equals("[") || s.equals("]") || s.equals("(") || s.equals(")")) {
            unite = "Caractéres Spéciaux";
        } else if (s.matches("(?i)(.*)@\\w\\d\\[[0-9](<|<=)\\w\\d(<|<=)[0-9]\\]") || s.matches("[a-z][a-z0-9]*\\{[0-9]*\\}")) {
            unite = "Operands";
        } else if (s.matches("(?i)(\\s)*(.*)(\\s)*")) {
            unite = "Operands";
        } else {
            unite = "Caractére Inconnu ";
        }
        return unite;
    }
}