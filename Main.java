import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
class Expression {
    public static String[] getExpression() throws Exception {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Вам необходимо ввести выражение, разделяя пробелом все знаки, дроби, скобки, в качестве знака деления используйте ':', формат записи дробей = 'a/b' ");
        String userInput = scanner.nextLine();
        if(Objects.equals(userInput, "quit")){
            throw new Exception("Вы закончили ввод!");
        }
        return userInput.split(" ");
    }
}
class Calculator {
    public static Object[] ExpressionToRPN(String[] Expr) {
        ArrayList currentResult = new ArrayList();;
        Stack<String> stack = new Stack<>();

        int priority;
        for (int i =0; i < Expr.length; i++) {
            priority = getPriority(Expr[i]);
            if (priority ==0) currentResult.add(Expr[i]);
            if (priority ==1) stack.push(Expr[i]);
            if (priority > 1) {
                while(!stack.empty()) {
                    if(getPriority(stack.peek()) >= priority)currentResult.add(stack.pop());
                    else break;

                }
                stack.push(Expr[i]);
            }
            if (priority == -1) {
                while (getPriority(stack.peek()) != 1) currentResult.add(stack.pop());
                stack.pop();
            }
        }
        while(!stack.empty())currentResult.add(stack.pop());
        return currentResult.toArray();
    }
    public static String RPNtoANSWER(Object[] RPN) throws Exception {
        String operand = new String();
        Stack<String> stack = new Stack<>();
        for(int i = 0; i < RPN.length; i++) {
            if (getPriority(String.valueOf(RPN[i])) == 0){
                operand = (String) RPN[i];
                stack.push(operand);
            }
            if(getPriority(String.valueOf(RPN[i])) > 1){
                fraction dr1 = new fraction(stack.pop());
                fraction dr2 = new fraction(stack.pop());
                if (RPN[i].toString().equals("+")) stack.push(fraction.summa(dr1,dr2));
                if (RPN[i].toString().equals("-")) stack.push(fraction.diff(dr1,dr2));
                if (RPN[i].toString().equals("*")) stack.push(fraction.prod(dr1,dr2));
                if (RPN[i].toString().equals(":")) stack.push(fraction.quot(dr1,dr2));
            }
        }
        return stack.pop();
    }
    public static int getPriority(String token) {
        if (Objects.equals(token, "*" ) || Objects.equals(token, ":")) return 3;
        else if (Objects.equals(token, "+" ) || Objects.equals(token, "-")) return 2;
        else if (Objects.equals(token, "(" )) return 1;
        else if (Objects.equals(token, ")" )) return -1;
        else return 0;
    }
}
class fraction {
    public int chislitel, znamenatel;
    public String drob;
    public fraction(String drob)throws Exception {
        int len = drob.length();
        StringBuilder chislitel0 = new StringBuilder();
        StringBuilder znamenatel0 = new StringBuilder();
        String pattern="[0-9]+/[1-9]+|-[0-9]+/[1-9]+";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(drob);
        int index = drob.indexOf('/');
        if (m.matches()) {
            for (int i = 0; i < index; i++) {
                chislitel0.append(drob.charAt(i));
            }
            for (int i = index + 1; i < len; i++) {
                znamenatel0.append(drob.charAt(i));
            }
        } else { throw new Exception("Дробь не распознана");
        }
        this.znamenatel = Integer.parseInt(znamenatel0.toString());
        this.chislitel = Integer.parseInt(chislitel0.toString());
    }
    public static String summa(fraction fraction0, fraction fraction1) {
        return (fraction0.znamenatel * fraction1.chislitel + fraction0.chislitel * fraction1.znamenatel) + "/" + fraction0.znamenatel * fraction1.znamenatel;
    }
    public static String diff(fraction fraction0, fraction fraction1) {
        return (fraction0.znamenatel * fraction1.chislitel - fraction0.chislitel * fraction1.znamenatel) + "/" + fraction0.znamenatel * fraction1.znamenatel;
    }
    public static String prod(fraction fraction0, fraction fraction1) {
        return fraction0.chislitel * fraction1.chislitel + "/" + fraction0.znamenatel * fraction1.znamenatel;
    }
    public static String quot(fraction fraction0, fraction fraction1) {
        return fraction0.chislitel * fraction1.znamenatel + "/" + fraction0.znamenatel * fraction1.chislitel;
    }
}
public class Main {
    public static void main(String[] args) throws Exception {
        while (true) {
            System.out.println(Calculator.RPNtoANSWER(Calculator.ExpressionToRPN(Expression.getExpression())));
        }
    }
}