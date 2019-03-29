package Elementaire.Syntax;

import java.util.ArrayList;

public class AST {

    AST left;
    AST right;
    String unit;

    public AST(String unit, AST left, AST right) {
        this.left = left;
        this.right = right;
        this.unit = unit;
    }


    public AST getNodeLeft() {
        return left;
    }

    public AST getNodeRight() {
        return right;
    }

    public String getUnit() {
        return unit;
    }

    public void setLeft(AST left) {
        this.left = left;
    }

    public void setRight(AST right) {
        this.right = right;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String toString(AST n) {
        return (unit + " " + n.getNodeLeft().unit + " " + n.getNodeRight().unit);
    }

    public boolean equivalent(AST n) {
        return (n.getNodeLeft().unit.equals(n.getNodeRight().unit));
    }

    /**
     * Affiche l'arbre selon un parcours préfixé
     */
    public void traverse() {
        System.out.println(getUnit());
        if (getNodeLeft() != null) {
            getNodeLeft().traverse();
        }
        if (getNodeRight() != null) {
            getNodeRight().traverse();
        }
    }

    public int ocur() {
        String regex = "exit\\{[0-9]*\\}|exit";
        if (getUnit().matches(regex)) {
            if ((getNodeLeft() != null) && (getNodeRight() != null)) {
                return 1 + getNodeLeft().ocur() + getNodeRight().ocur();
            } else if (getNodeLeft() != null) {
                return (1 + getNodeLeft().ocur());
            } else if (getNodeRight() != null) {
                return (1 + getNodeRight().ocur());
            } else {
                return 1;
            }
        } else if ((getNodeLeft() != null) && (getNodeRight() != null)) {
            return getNodeLeft().ocur() + getNodeRight().ocur();
        } else if (getNodeLeft() != null) {
            return (getNodeLeft().ocur());
        } else if (getNodeRight() != null) {
            return (getNodeRight().ocur());
        } else {
            return 0;
        }
    }

    public boolean find(String value) {
        if (value.equals(getUnit())) {
            return true;
        }
        if (getNodeLeft() != null) {
            return (getNodeLeft().find(value));
        }
        if (getNodeRight() != null) {
            return (getNodeRight().find(value));
        }
        return false;
    }

    public boolean findL(String value) {
        if (value.equals(getUnit())) {
            return true;
        }
        if (getNodeRight() != null) {
            return (getNodeRight().findL(value));
        }
        return false;
    }

    public void replaceALL(String value) {
        if (value.equals(getUnit())) {
            this.setUnit("I_" + getUnit());
        }
        if (getNodeLeft() != null) {
            getNodeLeft().replaceALL(value);
        }
        if (getNodeRight() != null) {
            getNodeRight().replaceALL(value);
        }
    }

    public AST insert(String[] value) {
        AST n = null;

        return n;
    }

    public String toString() {
        return ("");
    }

    public AST clone() {
        return new AST(unit, left, right);
    }

    public boolean equals1(AST ast) {
        if (this == ast) {
            return true;
        } else if ((unit != null) && (ast.unit != null)) {
            return unit.equals(ast.unit);
        } else {
            return false;
        }
    }

    public boolean equalsALL(AST ast) {
        Boolean v=true;
        if (!ast.getUnit().equals(getUnit())) {
            v= false;
        }
        if (getNodeLeft() != null&&v) {
            if (ast.getNodeLeft() != null) {
                v= (getNodeLeft().equalsALL(ast.getNodeLeft()));
            }else
                v= false;
        }
        if (getNodeRight() != null&&v) {
            if (ast.getNodeRight() != null) {
                v= (getNodeRight().equalsALL(ast.getNodeRight()));
            }else
                v= false;
        }
        return v;
    }

    public boolean isLeaf() {
        if ((left == null) && (right == null)) {
            return true;
        } else {
            return false;
        }
    }
    public boolean equalsANY(ArrayList<String> units){
        for (int i = 0; i < units.size(); i++) {
            if(unit.equals(units.get(i)))
                return true;
        }
        return false;
    }
    //insert(Object obj){

    //}
    //traverse(){
    //}
    //delate(){
    //}
}


