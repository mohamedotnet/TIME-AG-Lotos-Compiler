package Elementaire.Syntax;


import java.util.ArrayList;


public interface ISyntax {
    public AST Parsing(ArrayList<String> Units);
    public int Priority(String Op1, String Op2);
}
