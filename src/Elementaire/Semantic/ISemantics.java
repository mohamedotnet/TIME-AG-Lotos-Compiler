package Elementaire.Semantic;

import Elementaire.STE.STE;
import Elementaire.STE.Transition;
import Elementaire.Syntax.AST;

import java.util.ArrayList;

public interface ISemantics {
    STE generate(AST tree, String localite);
    ArrayList<Transition> generateTransitions(AST tree, int currentIndex, String localite);
}
