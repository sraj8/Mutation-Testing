package eclipse.ast.parser.Dao;

import java.util.HashMap;
import java.util.HashSet;

public class ExpressionParametersDaoImpl {
    private HashSet<String> operatorList = new HashSet<>();
    private HashSet <String> operandList = new HashSet<>();
    private HashSet <String> methodCall = new HashSet<>();
    private HashSet <String> expressions = new HashSet<>();
    private HashMap<String, String> bindingMap = new HashMap<>();

    public HashMap<String, String> getBindingMap() {
        return bindingMap;
    }

    public void setBindingMap(HashMap<String, String> bindingMap) {
        this.bindingMap = bindingMap;
    }

    public HashSet<String> getExpressions() {
        return expressions;
    }

    public void setExpressions(HashSet<String> expressions) {
        expressions = expressions;
    }

    public StringBuilder getNewExpression() {
        return newExpression;
    }

    public void setNewExpression(StringBuilder newExpression) {
        this.newExpression = newExpression;
    }

    private StringBuilder newExpression= new StringBuilder();

    public HashSet<String> getOperatorList() {
        return operatorList;
    }

    public void setOperatorList(HashSet<String> operatorList) {
        this.operatorList = operatorList;
    }

    public HashSet<String> getOperandList() {
        return operandList;
    }

    public void setOperandList(HashSet<String> operandList) {
        this.operandList = operandList;
    }

    public HashSet<String> getMethodCall() {
        return methodCall;
    }

    public void setMethodCall(HashSet<String> methodCall) {
        this.methodCall = methodCall;
    }
}
