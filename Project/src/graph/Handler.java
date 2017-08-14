/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author Andres
 */
public class Handler {
	
	public static int SCREEN_SIZE = 600;
	public static int EXTRA_X = 8;
	public static int EXTRA_Y = 31;
	public static int WINDOW_Y = 60;
	private static String[] operations = { "*", "/", "+", "-" };
	private static ArrayList<Point> points;
	public static void init() {
		new Window();
		points = new ArrayList<>();
//		shuntingYard("3 + 4 * 2 / ( 1 − 5 ) ^ 2 ^ 3");
	}
	
	/**
	 * devuelve el color dado por parametros en HSB
	 * @param h
	 * @param s
	 * @param b
	 * @return 
	 */
	public static Color color(double h, double s, double b) {
		return Color.getHSBColor((float)(h/360f),(float)(s/100f),(float)(b/100f));
	}
	
	public static void graph(String text) {
		try {
			solve(shuntingYard(interpret(text)));
		} catch(Exception e) { }
	}
	
	public static ArrayList<Token> interpret(String eq) throws Exception {
		ArrayList<Token> tokens;
		try {
			tokens = new ArrayList<>();

			int charNumber = 0;
			while (charNumber < eq.length()) {
				String tokenValue = ""+eq.charAt(charNumber);
				Token token;
				try {
	//				GET ALL DIGITS OF NUMBER
					if (tokenValue.equals(".")) {
						tokenValue = "0.";
					}
					Double.parseDouble(tokenValue);
					while(true) {
						Double.parseDouble(tokenValue+eq.charAt(charNumber+1));
						charNumber++;
						tokenValue = tokenValue+eq.charAt(charNumber);
					}
				} catch (Exception e1) {
					try {
	//					CREATE NUMBER TOKEN
						Double.parseDouble(tokenValue);
						token = new Token(tokenValue);
					} catch (Exception e2) {
	//					CHECK LITERAL ARGUMENT
						String normalOperators = "x*/+-^";
						if (normalOperators.contains(tokenValue)) {
							token = new Token(tokenValue);
						} else {
							String threeLong = ""+eq.charAt(charNumber)+eq.charAt(charNumber+1)+eq.charAt(charNumber+2);
							if (threeLong.equals("cos")){
								charNumber+=2;
								token = new Token(threeLong);
							} else if (threeLong.equals("tan")) {
								charNumber+=2;
								token = new Token(threeLong);
							} else if (threeLong.equals("sin")) {
								charNumber+=2;
								token = new Token(threeLong);
							} else if (threeLong.equals("abs")) {
								charNumber+=2;
								token = new Token(threeLong);
							} else {
								throw new Exception("Couldn't interpret '"+threeLong+"' or '"+tokenValue+"'");
							}
						}
					}
				}
				System.out.println(tokens.size()+1 + ": "+token.getTokenValue() + " ...");
				if (tokens.size()>0) {
					Token lastToken = tokens.get(tokens.size()-1);
					if (token.isVariable() || token.isNumber()) {
						if (!lastToken.isOperator()&&!lastToken.isParentheses()&&!lastToken.isAdvancedOperator()) {
							tokens.add(new Token("*"));
						} else if (lastToken.isParentheses()) {
							if (lastToken.isClosedParentheses()) {
								tokens.add(new Token("*"));
							}
						}
						tokens.add(token);
					} else if (token.isOperator()) {
						if (lastToken.isOperator()) {
							throw new Exception("Duplicate operators.");
						} else {
							tokens.add(token);
						}
					} else if (token.isAdvancedOperator()) {
						tokens.add(token);
					} else if (token.isParentheses()) {
						if (!lastToken.isOperator()){
							tokens.add(new Token("*"));
						}
						tokens.add(token);
					} else {
						throw new Exception("Unreported token: "+token);
					}
				} else {
					if (token.isVariable() || token.isNumber()) {
						tokens.add(token);
					} else if (token.isOperator()) {
						throw new Exception("Operation factors missing.");
					} else if (token.isAdvancedOperator()) {
						tokens.add(token);
					} else if (token.isParentheses()) {
						if (token.isOpenParentheses()) {
							tokens.add(token);
						} else {
							throw new Exception("Operation factors missing.");
						}
					} else {
						throw new Exception("Unreported token: "+token);
					}
				}

				charNumber++;
			}
			System.out.print("Interpreted as: ");
			for (int i = 0; i < tokens.size(); i++) {
				if (i != 0) {
					System.out.print("_");
				}
				System.out.print(tokens.get(i));
				
			}
			System.out.println("");
		} catch (Exception e) {
			System.out.println("Interpreting error: "+e.getMessage());
			throw e;
		}
		return tokens;
	}
	
	public static ArrayList<Token> shuntingYard(ArrayList<Token> eq) {
		
//		while there are tokens to be read:
//			read a token.
//			if the token is a number, then push it to the output queue.
//			if the token is an operator, then:
//				while there is an operator at the top of the operator stack with
//					greater than or equal to precedence and the operator is left associative:
//						pop operators from the operator stack, onto the output queue.
//				push the read operator onto the operator stack.
//			if the token is a left bracket (i.e. "("), then:
//				push it onto the operator stack.
//			if the token is a right bracket (i.e. ")"), then:
//				while the operator at the top of the operator stack is not a left bracket:
//					pop operators from the operator stack onto the output queue.
//				pop the left bracket from the stack.
//				/* if the stack runs out without finding a left bracket, then there are
//				mismatched parentheses. */
//		if there are no more tokens to read:
//			while there are still operator tokens on the stack:
//				/* if the operator token on the top of the stack is a bracket, then
//				there are mismatched parentheses. */
//				pop the operator onto the output queue.
//		exit.
		ArrayList<Token> outputQueue = new ArrayList<>();
		ArrayList<Token> operatorStack = new ArrayList<>();
		for (int i = 0; i < eq.size(); i++) {
			Token token = eq.get(i);
//			if (token.isVariable() || token.isNumber()) {
//				outputQueue.add(token);
//			} else {
//				System.out.println("unknown token: "+token);
//			}

			if (token.isNumber()) {
				outputQueue.add(token);
			} else {
				System.out.println(token+ " in");
				if (token.equals("x")) {
					outputQueue.add(token);
				} else if (token.equals(" ")) {
				} else if (token.equals("(")) {
					operatorStack.add(token);
				} else if (token.equals(")")) {
					Token lastOperator = operatorStack.get(operatorStack.size()-1);
					while(!lastOperator.equals("(")) {
						outputQueue.add(lastOperator);
						operatorStack.remove(operatorStack.size()-1);
						lastOperator = operatorStack.get(operatorStack.size()-1);
					}
					operatorStack.remove(operatorStack.size()-1);
				} else {
					if (operatorStack.size()>0) {
						Token lastOperator = operatorStack.get(operatorStack.size()-1);
						while(lastOperator.isHigher(token) && operatorStack.size()>1) {
							System.out.println("popping "+lastOperator);
							System.out.println(lastOperator.operatorValue()+" ? "+token.operatorValue());
							outputQueue.add(lastOperator);
							operatorStack.remove(operatorStack.size()-1);
							lastOperator = operatorStack.get(operatorStack.size()-1);
						}
					}
					operatorStack.add(token);
				}
			}
		}
		while (operatorStack.size()>0) {
			Token lastOperator = operatorStack.get(operatorStack.size()-1);
			outputQueue.add(lastOperator);
			operatorStack.remove(operatorStack.size()-1);
		}
		
		return outputQueue;
	}
	
	public static void solve(ArrayList<Token> eq) {
		
		int x = 2;
		int j = 0;
		double n1 = 0;
		double n2 = 0;
		boolean nExists = false;
		while (j<eq.size()) {
			Token t = eq.get(j);
			int i = j-1;
			if (t.isNumber() || t.isVariable()) {
				n2 = Double.parseDouble(eq.get(i).getTokenValue());
				n1 = n2;
			} else if (t.isOperator()) {
				
//				APPLY OPERATOR
				if (t.getTokenValue().equals("*")) {
					n2 = n1*n2;
				} else if (t.getTokenValue().equals("/")) {
					n2 = n1/n2;
				} else if (t.getTokenValue().equals("+")) {
					n2 = n1*n2;
				} else if (t.getTokenValue().equals("-")) {
					n2 = n1-n2;
				}
			}

			j++;
		}
		System.out.println(n2);
//		for (int x = 0; x < 100; x++) {
//			float y;
//			for (int j = 0; j < eq.size(); j++) {
//				Token t = eq.get(j);
//				if (t.isVariable()) {
//					
//				}
//			}
//			points.add(new Point(x,y));
//		}
	}
	
	public static BufferedImage getGraph() {
		int size = SCREEN_SIZE;
		BufferedImage graph = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
		Graphics g = graph.getGraphics();
		
		g.setColor(color(39.2, 11.5, 89));
		g.fillRect(0, 0, size, size);
		
		g.setColor(Color.gray);
		g.drawLine(size/2, 0, size/2, size);
		g.drawLine(0, size/2, size, size/2);
		
		g.setColor(Color.black);
		g.drawLine(0, 0, size, 0);
		g.drawLine(0, 0, 0, size);
		g.drawLine(size-1, 0, size-1, size-1);
		g.drawLine(0, size-1, size-1, size-1);
		
		return graph;
	}
	
}
