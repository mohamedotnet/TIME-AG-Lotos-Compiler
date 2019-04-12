import Agent.Plan;

public class Test {

    public static void main(String[] args){
        String expression = "(a;exit|||b;exit{1})>>(c;exit)";
        String expressions[] = Plan.getElementaryExpressions(expression);

        if (expressions == null){
            System.out.println("wow!");
        }else {
            for (String s:
                expressions){
                System.out.println(s);
            }
        }
    }
}
