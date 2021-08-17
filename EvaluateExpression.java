import java.util.ArrayList;
import java.util.Stack;

class EvaluateExpression {

    static Double evaluateExpression(ArrayList<String> expression) {
        // Create operandStack to store operands
        Stack<Double> operandStack = new Stack<>();

        // Create operatorStack to store operators
        Stack<Character> operatorStack = new Stack<>();

        // Phase 1: Scan tokens
        for (String token : expression) {
            switch (token) {
                case "+":
                case "-":
                    // Process all +, -, *, /, % in the top of the operator stack
                    while (!operatorStack.isEmpty() && 
                    	  (operatorStack.peek() == '+' ||
                    	   operatorStack.peek() == '-' || 
                    	   operatorStack.peek() == '*' || 
                    	   operatorStack.peek() == '/' || 
                    	   operatorStack.peek() == '%')) {
                        processAnOperator(operandStack, operatorStack);
                    }
                    // Push the + or - operator into the operator stack
                    operatorStack.push(token.charAt(0));
                    break;
                case "*":
                case "/":
                case "%":
                    // Process all *, /, % in the top of the operator stack
                    while (!operatorStack.isEmpty() && (operatorStack.peek() == '*' || operatorStack.peek() == '/'
                            || operatorStack.peek() == '%')) {
                        processAnOperator(operandStack, operatorStack);
                    }
                    // Push the * or / or % operator into the operator stack
                    operatorStack.push(token.charAt(0));
                    break;
                case "(":
                    operatorStack.push('('); // Push '(' to stack
                    break;
                case ")":
                    // Process all the operators in the stack until seeing '('
                    while (operatorStack.peek() != '(') {
                        processAnOperator(operandStack, operatorStack);
                    }
                    operatorStack.pop(); // Pop the '(' symbol from the stack and discard it
                    break;
                default: // An operand scanned
                    // Push an operand to the stack
                    operandStack.push(new Double(token));
                    break;
            }
        }
        // Phase 2: process all the remaining operators in the stack
        while (!operatorStack.isEmpty()) {
            processAnOperator(operandStack, operatorStack);
        }
        // Return the result
        return operandStack.pop();
    }


    private static void processAnOperator(Stack<Double> operandStack, Stack<Character> operatorStack) {
        char op = operatorStack.pop();
        Double op1 = operandStack.pop();
        Double op2 = operandStack.pop();
        
        if (op == '+') {
            operandStack.push(op2 + op1);
        }
        else if (op == '-') {
            operandStack.push(op2 - op1);
        }
        else if (op == '*') {
            operandStack.push(op2 * op1);
        }
        else if (op == '/') {
            operandStack.push(op2 / op1);
        }
        else if (op == '%') {
            operandStack.push(op2 % op1);
        }
        else if (op == '(') {
        	operandStack.push(op2 * op1);
        }
        
    }
}