package Elementaire.Syntax;


import java.util.ArrayList;


public interface ISyntax {
    AST Parsing(ArrayList<String> Units);
    int Priority(String Op1, String Op2);
}
