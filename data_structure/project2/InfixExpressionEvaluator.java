package cs445.a2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;

/**
 * This class uses two stacks to evaluate an infix arithmetic expression from an
 * InputStream.
 */
public class InfixExpressionEvaluator {
    // Tokenizer to break up our input into tokens
    StreamTokenizer tokenizer;

    // Stacks for operators (for converting to postfix) and operands (for
    // evaluating)
    StackInterface<Character> operators;
    StackInterface<Double> operands;
    char topOperator;
    double topOperand;

    /**
     * Initializes the solver to read an infix expression from input.
     */
    public InfixExpressionEvaluator(InputStream input) {
        // Initialize the tokenizer to read from the given InputStream
        tokenizer = new StreamTokenizer(new BufferedReader(
                        new InputStreamReader(input)));

        // Declare that - and / are regular characters (ignore their regex
        // meaning)
        tokenizer.ordinaryChar('-');
        tokenizer.ordinaryChar('/');

        // Allow the tokenizer to recognize end-of-line
        tokenizer.eolIsSignificant(true);

        // Initialize the stacks
        operators = new ArrayStack<Character>();
        operands = new ArrayStack<Double>();
    }
    
    //peek on the nextToken
    int peekToken() {
        try {
            tokenizer.nextToken();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tokenizer.pushBack();
        return tokenizer.ttype;
    }

    /**
     * A type of runtime exception thrown when the given expression is found to
     * be invalid
     */
    class ExpressionError extends RuntimeException {
        ExpressionError(String msg) {
            super(msg);
        }
    }

    /**
     * Creates an InfixExpressionEvaluator object to read from System.in, then
     * evaluates its input and prints the result.
     */
    public static void main(String[] args) {
        InfixExpressionEvaluator solver =
                        new InfixExpressionEvaluator(System.in);
        Double value = solver.evaluate();
        if (value != null) {
            System.out.println(value);
        }
    }

    /**
     * Evaluates the expression parsed by the tokenizer and returns the
     * resulting value.
     */
    public Double evaluate() throws ExpressionError {
        // Get the first token. If an IO exception occurs, replace it with a
        // runtime exception, causing an immediate crash.
        try {
            tokenizer.nextToken();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Continue processing tokens until we find end-of-line
        while (tokenizer.ttype != StreamTokenizer.TT_EOL) {
            // Consider possible token types
            switch (tokenizer.ttype) {
                case StreamTokenizer.TT_NUMBER:
                    // If the token is a number, process it as a double-valued
                    // operand
                    processOperand((double)tokenizer.nval);
                    break;
                case '+':
                case '-':
                case '*':
                case '/':
                case '^':
                case '!':
                case '%':
                    // If the token is any of the above characters, process it
                    // is an operator
                    processOperator((char)tokenizer.ttype);
                    break;
                case '(':
                case '[':
                    // If the token is open bracket, process it as such. Forms
                    // of bracket are interchangeable but must nest properly.
                    processOpenBracket((char)tokenizer.ttype);
                    break;
                case ')':
                case ']':
                    // If the token is close bracket, process it as such. Forms
                    // of bracket are interchangeable but must nest properly.
                    processCloseBracket((char)tokenizer.ttype);
                    break;
                case StreamTokenizer.TT_WORD:
                    // If the token is a "word", throw an expression error
                    throw new ExpressionError("Unrecognized token: " +
                                    tokenizer.sval);
                default:
                    // If the token is any other type or value, throw an
                    // expression error
                    throw new ExpressionError("Unrecognized token: " +
                                    String.valueOf((char)tokenizer.ttype));
            }

            // Read the next token, again converting any potential IO exception
            try {
                tokenizer.nextToken();
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Almost done now, but we may have to process remaining operators in
        // the operators stack
        processRemainingOperators();

        // Return the result of the evaluation
        return operands.pop();
    }

    /**
     * Processes an operand.
     */
    void processOperand(double operand) {
       if (peekToken() == StreamTokenizer.TT_NUMBER){
           throw new ExpressionError("Operand followed by an operand");
       }
        operands.push(operand);
    }

    /**
     * Processes an operator.
     */
    void processOperator(char operator) {
        if (operands.isEmpty())
            throw new ExpressionError("Expression begins with an operator");
        if (operator == '!'){
            if (peekToken()==StreamTokenizer.TT_NUMBER)
                throw new ExpressionError ("Factorial followed by an operand");
            operands.push(factorial(operands.pop()));
        }
        else{
            if (peekToken()=='+' || peekToken()=='-' || peekToken()=='*' || 
                    peekToken()=='/' || peekToken()=='^' || peekToken()=='%')
                throw new ExpressionError("Operator followed by another operator");
            if (peekToken()==')' || peekToken()==']')
                throw new ExpressionError("Operator followed by a closed bracket");
            if (operator == '^')
                   operators.push(operator);
            else {
                while (!operators.isEmpty() && 
                        lowerPrecedence(operator, operators.peek())) {
                    topOperand = operands.pop();
                    if (operator=='/' && topOperand==0)
                        throw new ExpressionError("Divide by zero");
                    operands.push(calculate(operators.pop(), topOperand, operands.pop()));
                }
                operators.push(operator);
            }
        }
    }

    /**
     * Processes an open bracket.
     */
    void processOpenBracket(char openBracket) {
        if (peekToken()=='+' || peekToken()=='-' || peekToken()=='*' || 
                peekToken()=='/' || peekToken()=='^' || peekToken()=='%')
            throw new ExpressionError("Open bracket followed by an operator");
        else   
            operators.push(openBracket);
    }

    /**
     * Processes a close bracket.
     */
    void processCloseBracket(char closeBracket) {
        topOperator = operators.pop();
        while (topOperator != '(' && topOperator != '[') {
            operands.push(calculate(topOperator, operands.pop(), operands.pop()));
            topOperator = operators.pop();               
        }
        if ((closeBracket == ')' && topOperator == '[') || 
                (closeBracket == ']' && topOperator == '('))
            throw new ExpressionError("Brackets not nested properly");
            
    }

    /**
     * Processes any remaining operators leftover on the operators stack
     */
    void processRemainingOperators() {
       while (!operators.isEmpty()){
           topOperand = operands.pop();
           if (topOperand==0)
               throw new ExpressionError("Divide by zero");
           operands.push(calculate(operators.pop(), topOperand, operands.pop()));
       }
    }
    
    //check if precedence of current operator <= precedence of top operator
    static boolean lowerPrecedence (char one, char two)  {
        if ((one=='*' || one=='/' || one=='%') && (two=='+' || two=='-'))
            return false;
        if(two=='(' || two==')' || two=='[' || two==']')
            return false;
        else
            return true;
    }
    
    //calculate value
    static double calculate (char op, double two, double one){
       double result = 0.0;
        switch (op) {
            case '+':
                result = one + two;
                break;
            case '-':
                result = one - two;
                break;
            case '*':
                result = one * two;
                break;
            case '/':
                result = one / two;
                break;
            case '^':
                result = Math.pow(one, two); 
                break;
            case '%':
                result = one % two;
                break;
        }
        return result;
    }
    
    static double factorial(double n){
        if (n <= 1)
            return 1.0;
        else
            return n*factorial(n-1);
    }
}

