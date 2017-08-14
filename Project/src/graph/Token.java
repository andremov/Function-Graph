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
	
	String tokenValue;

	public Token(String value) {
		this.tokenValue = value;
	}

	@Override
	public String toString() {
		return tokenValue;
	}
	
	public boolean isHigher(Token comparison) {
		return operatorValue() >= comparison.operatorValue();
	}
	
	public boolean isOperator() {
		return operatorValue()!=0 && operatorValue()!=2;
	}
	
	public boolean isParentheses() {
		return operatorValue() == 2;
	}
	
	public boolean isOpenParentheses() {
		return tokenValue.equals("(");
	}
	
	public boolean isClosedParentheses() {
		return !isOpenParentheses();
	}
	
	public boolean isNumber() {
		boolean isNumber = false;
		
		try {
			Double.parseDouble(tokenValue);
			isNumber = true;
		} catch (Exception e) { }
		
		return isNumber;
	}
	
	public boolean isVariable() {
		return tokenValue.equals("x");
	}
	
	public boolean isAdvancedOperator() {
		return (tokenValue.equals("cos") || tokenValue.equals("sin") || tokenValue.equals("abs") || tokenValue.equals("tan"));
	}
	
	public int operatorValue() {
		int value = 0;
		
		if (tokenValue.equals("(") || tokenValue.equals(")")) {
			value = 1;
		}
		
		if (tokenValue.equals("^") || tokenValue.equals("pow")) {
			value = 2;
		}
		
		if (tokenValue.equals("*") || tokenValue.equals("/")) {
			value = 3;
		}
		
		if (tokenValue.equals("+") || tokenValue.equals("-")) {
			value = 4;
		}
		
		return value;
	}
	
}
