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
	
	public static float PRECISION = 1f;
	
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
			System.out.println("Raw function: "+text);
			ArrayList<Token> tokens = convertEquation(text);
			System.out.println("Converted function: "+printList(tokens));
			tokens = redact(tokens);
			System.out.println("Redacted function: "+printList(tokens));
			tokens = shuntingYard(tokens);
			System.out.println("Ordered function: "+printList(tokens));
		} catch(Exception e) {
			System.out.println("Exception: "+e.getMessage());
//			System.out.println("Aiuda");
		}
	}
	
	public static String printList(ArrayList<Token> list) {
		String a = "";
		for (int i = 0; i < list.size(); i++) {
			a = a + list.get(i) + " ";
		}
		return a;
	}
	
	public static ArrayList<Token> convertEquation(String eq) {
		ArrayList<Token> tokens = new ArrayList<>();
		int charNumber = 0;
		while (charNumber < eq.length()) {
			String tokenValue = ""+eq.charAt(charNumber);
			if (!tokenValue.equals(" ")) {
				Token token = null;

				if (token == null) {
					// IS IT A NUMBER?
					try {
						if (tokenValue.equals(".")) {
							tokenValue = "0.";
						}
						Double.parseDouble(tokenValue);
						token = new Token(tokenValue);
						while(true) {
							Double.parseDouble(tokenValue+eq.charAt(charNumber+1));
							charNumber++;
							tokenValue = tokenValue+eq.charAt(charNumber);
							token = new Token(tokenValue);
						}
					} catch (Exception e) { }
				}

				if (token == null) {
					// IS OPERATOR?
					if ("x*/+-^()".contains(tokenValue)) {
						token = new Token(tokenValue);
					}
				}

				if (token == null && eq.length() > charNumber+2) {
					// IS ADVANCED OPERATOR?
					String threeLong = tokenValue+eq.charAt(charNumber+1)+eq.charAt(charNumber+2);
					if (threeLong.equals("cos") || threeLong.equals("tan") || 
						threeLong.equals("sin") || threeLong.equals("abs")) {
						charNumber+=2;
						token = new Token(threeLong);
					}
				}

				if (token != null) {
					tokens.add(token);
				}
			}
			charNumber++;
		}
		return tokens;
	}
			
	public static ArrayList<Token> redact(ArrayList<Token> in) throws Exception {
		ArrayList<Token> out = new ArrayList<>();

		if (in.get(0).isOperator()) {
			throw new Exception("Wrongful operator.");
		} else {
			out.add(in.get(0));
		}

		for (int i = 1; i < in.size(); i++) {
			if (in.get(i).isVariable() || in.get(i).isNumber()) {
				if (in.get(i-1).isVariable() || in.get(i-1).isNumber()) {
					out.add(new Token("*"));
				}
				out.add(in.get(i));
			}
			
			if (in.get(i).isOpenParentheses()) {
				if (in.get(i-1).isClosedParentheses() || in.get(i-1).isVariable() || in.get(i-1).isNumber()) {
					out.add(new Token("*"));
				}
				out.add(in.get(i));
			}
			
			if (in.get(i).isClosedParentheses()) {
				out.add(in.get(i));
			}
			
			if (in.get(i).isOperator()) {
				if (in.get(i-1).isOperator()) {
					throw new Exception("Duplicate operators.");
				} else {
					out.add(in.get(i));
				}
			}
		}
			
		return out;
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

			if (token.isNumber() || token.isVariable()) {
				outputQueue.add(token);
			} else {
				if (token.isOpenParentheses()) {
					operatorStack.add(token);
				} else if (token.isClosedParentheses()) {
					Token lastOperator = operatorStack.get(operatorStack.size()-1);
					while(!lastOperator.isOpenParentheses()) {
						outputQueue.add(lastOperator);
						operatorStack.remove(operatorStack.size()-1);
						lastOperator = operatorStack.get(operatorStack.size()-1);
					}
					operatorStack.remove(operatorStack.size()-1);
				} else if (token.isOperator()) {
					if (operatorStack.size()>0) {
						Token lastOperator = operatorStack.get(operatorStack.size()-1);
						while(lastOperator.isHigher(token) && operatorStack.size()>1) {
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
//			System.out.println("popping "+lastOperator);
			operatorStack.remove(operatorStack.size()-1);
		}
		
		return outputQueue;
	}
	
	public static void solve(ArrayList<Token> eq) {
		
		points.clear();
		for (int rawX = 0; rawX < SCREEN_SIZE*PRECISION; rawX++) {
			double x = (rawX-(SCREEN_SIZE*0.5f*PRECISION))/PRECISION;
			
			int j = 0;
			double n1 = 0;
			double n2 = 0;
			
			while (j<eq.size()) {
				Token t = eq.get(j);
				if (t.isNumber() || t.isVariable()) {
//					System.out.println("Setting factor 2: ");
					try {
						n2 = eq.get(j).getNumericalValue(x);
					} catch (Exception e) { }
				} else if (t.isOperator()) {
//					System.out.println("Found an operator!");
					try {
						n1 = eq.get(j-2).getNumericalValue(x);
					} catch (Exception e) { }
					
	//				APPLY OPERATOR
					if (t.getTokenValue().equals("*")) {
						n2 = n1*n2;
					} else if (t.getTokenValue().equals("/")) {
						n2 = n1/n2;
					} else if (t.getTokenValue().equals("+")) {
						n2 = n1+n2;
					} else if (t.getTokenValue().equals("-")) {
						n2 = n1-n2;
					}
				}

				j++;
			}
			points.add(new Point(x+(SCREEN_SIZE/2),(SCREEN_SIZE/2)-n2));
		}
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
		
		for (int i = 0; i < points.size(); i++) {
			g.drawImage(points.get(i).getImage(), (int)points.get(i).getX(), (int)points.get(i).getY(), null);
		}
		
		return graph;
	}
	
}
