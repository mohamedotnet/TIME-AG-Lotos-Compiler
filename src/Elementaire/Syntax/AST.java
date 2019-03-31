package Elementaire.Syntax;

import java.util.ArrayList;

/* TODO: refactor */

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

    private void setUnit(String unit) {
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

    private boolean findL(String value) {
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


    public String toString() {
        return ("");
    }

    /* TODO: refactor */
    public AST clone() {
        return new AST(unit, left, right);
    }

    public boolean equalsALL(AST ast) {
        boolean v = true;
        if (!ast.getUnit().equals(getUnit())) {
            v = false;
        }
        if (getNodeLeft() != null && v) {
            if (ast.getNodeLeft() != null) {
                v = (getNodeLeft().equalsALL(ast.getNodeLeft()));
            } else
                v = false;
        }
        if (getNodeRight() != null && v) {
            if (ast.getNodeRight() != null) {
                v = (getNodeRight().equalsALL(ast.getNodeRight()));
            } else
                v = false;
        }
        return v;
    }

    public boolean isLeaf() {
        return (left == null) && (right == null);
    }

    public boolean equalsANY(ArrayList<String> units) {
        for (String unitL:
             units) {
            if (unit.equals(unitL)) return true;
        }
        return false;
    }
}


