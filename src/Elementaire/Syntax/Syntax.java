package Elementaire.Syntax;

import java.util.ArrayList;
import java.util.Stack;


/* TODO: Refactor the existing code */

public class Syntax implements ISyntax {


    public AST Parsing(ArrayList<String> Units) {
        Stack<String> operators = new Stack<>();
        Stack<AST> operands = new Stack<>();
        AST left;
        AST right;
        String unit;
        AST ast = null;
        String unitOO;

        boolean ver = true, com = true;
        /*if (getPriority(Units.get(0)) == 1) {
            ver = false;
        } else if (getPriority(Units.get(0)) != -1) {
            return ast;
        }*/

        for (int i = 0; i < Units.size(); i++) {
            unitOO = Units.get(i);
            //System.out.println(unitOO);
            if (isOperands(unitOO)) {//isOperands
                ///////////v/////////////
                com = false;
                ver = !ver;
                if (ver) {
                    return ast;
                }
                ////////////////////////
                AST node = new AST(unitOO, null, null);
                operands.push(node);
            } else if (getPriority(unitOO) != -1) {
                ////////////v/////////////
                if (getPriority(unitOO) == 1 && !com) {
                    ver = true;
                }
                ver = !ver;
                if (!ver) {
                    return ast;
                }
                ///////////////////////////
                if (!operators.isEmpty()) {
                    if (Priority(unitOO, operators.peek()) == 0) {
                        //right=operands.pop();
                        right = operands.pop();
                        // if (!operands.isEmpty())
                        //left=operands.pop();
                        left = operands.pop();
                        // else return ast;
                        unit = operators.pop();
                        operands.add(new AST(unit, left, right));
                    }
                }
                operators.push(unitOO);
            }
        }
        while (!operators.isEmpty()) {
            //right=operands.pop();
            right = operands.pop();
            if (!operands.isEmpty() && !operators.peek().matches("(?i)hide [a-z][a-z0-9]*(,[a-z][a-z0-9]*)* in")) //left=operands.pop();
            {
                left = operands.pop();
            } else if (operators.peek().matches("(?i)hide [a-z][a-z0-9]*(,[a-z][a-z0-9]*)* in")) {
                left = null;
            } else {
                return ast;
            }
            unit = operators.pop();
            operands.add(new AST(unit, left, right));
        }
        if (verifierAST(operands.peek())) {
            return operands.peek();
        } else {
            System.out.println("entered");
            return ast;
        }
    }

    public boolean verifierAST(AST ast) {
        if (getPriority(ast.getUnit()) == 1) {
            if (ast.right != null) {
                return verifierAST(ast.right);
            } else {
                return false;
            }
        }
        if (getPriority(ast.getUnit()) == 4) {
            if (ast.getNodeLeft().getUnit().equals("exit") ||
                    ast.getNodeLeft().getUnit().matches("exit\\{[0-9]*\\}")) {
                return false;
            } else {
                return verifierAST(ast.right);
            }
        } else if (ast.left != null && ast.right != null) {
            return verifierAST(ast.left) && verifierAST(ast.right);
        } else if (ast.ocur() == 1) {
            return true;
        } else {
            return false;
        }

    }

    /*public boolean verifierASTHide(AST ast) {
        if (ast.ocur("(?i)hide [a-z][a-z0-9]*(,[a-z]+[a-z0-9]*)* in") == 1) {
            return true;
        } else {
            return false;
        }
    }*/

    @Override
    public int Priority(String Op1, String Op2) {
        if (getPriority(Op1) >= getPriority(Op2)) {
            return 1;
        } else {
            return 0;
        }
    }

    public int getPriority(String Op) {
        if (Op.equals(";")) {
            return 4;
        } else if (Op.equals("||") || Op.equals("|||") || Op.equals("[]") || Op.matches("(?i)\\|\\[[a-z][a-z0-9]*(,[a-z][a-z0-9]*)*\\]\\|")) {
            return 3;
        } else if (Op.equals(">>") || Op.equals("[>")) {
            return 2;
        } else if (Op.matches("(?i)hide [a-z][a-z0-9]*(,[a-z][a-z0-9]*)* in")) {
            return 1;
        } else if (Op.matches("delta\\([0-9]*\\)")) {
            return 0;
        }
        return -1;
    }

    public boolean isOperands(String s) {
        if (s.matches("(?i)[a-z][a-z0-9]*") ||
                s.matches("(?i)[a-z][a-z0-9]*@\\w\\d\\[[0-9](<|<=)\\w\\d(<|<=)[0-9]\\]") ||
                s.matches("(?i)move\\([a-z][a-z0-9]*\\)") ||
                s.matches("[a-z][a-z0-9]*\\{[0-9]*\\}")) {
            return true;
        }

        return false;
    }

}
