package Elementaire.Semantic;

import Elementaire.Syntax.AST;

public class Action {
    private AST ast;
    private int sucIndex;
    public Action(int suc,AST ast) {
        sucIndex=suc;
        this.ast=ast;
    }


    public AST getAst() {
        return ast;
    }

    public int getSucIndex() {
        return sucIndex;
    }

}