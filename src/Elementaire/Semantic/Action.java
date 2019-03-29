package Elementaire.Semantic;

import Elementaire.Syntax.AST;

public class Action {
    AST ast;
    int sucIndex;
    public Action(int suc,AST ast) {
        sucIndex=suc;
        this.ast=ast;
    }

    public void setAst(AST ast) {
        this.ast = ast;
    }

    public void setSucIndex(int sucIndex) {
        this.sucIndex = sucIndex;
    }

    public AST getAst() {
        return ast;
    }

    public int getSucIndex() {
        return sucIndex;
    }

}