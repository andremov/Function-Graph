/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

/**
 *
 * @author Andres
 */
public class Token {

    private String tokenValue;

    public Token(String value) {
	this.tokenValue = value;
    }

    @Override
    public String toString() {
	return getTokenValue();
    }

    public boolean isHigher(Token comparison) {
	return operatorValue() < comparison.operatorValue();
    }

    public boolean isOperator() {
	return "*+/-^".contains(tokenValue);
    }

    public boolean isParentheses() {
	return "()".contains(tokenValue);
    }

    public boolean isOpenParentheses() {
	return getTokenValue().equals("(");
    }

    public boolean isClosedParentheses() {
	return getTokenValue().equals(")");
    }

    public boolean isNumber() {
	boolean isNumber = false;

	try {
	    Double.parseDouble(getTokenValue());
	    isNumber = true;
	} catch (Exception e) { }

	return isNumber;
    }

    public boolean isVariable() {
	return getTokenValue().equals("x");
    }

    public boolean isAdvancedOperator() {
	return (getTokenValue().equals("cos") ||
		getTokenValue().equals("sin") ||
		getTokenValue().equals("abs") ||
		getTokenValue().equals("pow") ||
		getTokenValue().equals("tan"));
    }

    public int operatorValue() {
	int value = 0;

	if (getTokenValue().equals("^")) {
	    value = 1;
	}

	if (getTokenValue().equals("*") || getTokenValue().equals("/")) {
	    value = 2;
	}

	if (getTokenValue().equals("+") || getTokenValue().equals("-")) {
	    value = 3;
	}

	return value;
    }

    /**
     * @return the tokenValue
     */
    public String getTokenValue() {
	return tokenValue;
    }

    public double getNumericalValue(double x) throws Exception {
	double value;

	if (isVariable()) {
	    value = x;
	} else if (isNumber()) {
	    value = Double.parseDouble(tokenValue);
	} else {
	    throw new Exception("Wrong type.");
	}

	return value;
    }

    /**
     * @param tokenValue the tokenValue to set
     */
    public void setTokenValue(String tokenValue) {
	this.tokenValue = tokenValue;
    }
}
