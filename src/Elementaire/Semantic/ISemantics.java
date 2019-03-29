package Elementaire.Semantic;

import Elementaire.STE.STE;
import Elementaire.STE.Transition;
import Elementaire.Syntax.AST;

import java.util.ArrayList;

public interface ISemantics {
    public STE generate(AST tree, String localite);
    public ArrayList<Transition> generateTransitions(AST tree, int currentIndex, String localite);
}
