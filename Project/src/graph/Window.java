/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import java.awt.Canvas;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 *
 * @author Andres
 */
public class Window extends JFrame {
	
	public Window() {
        setLayout(null);
        setSize(Handler.SCREEN_SIZE+Handler.EXTRA_X,Handler.SCREEN_SIZE+Handler.EXTRA_Y+Handler.WINDOW_Y);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Graficador de Funciones");
		
		Screen s = new Screen(Handler.SCREEN_SIZE,Handler.SCREEN_SIZE);
		add(s);
		
		JTextField inputField = new JTextField();
		inputField.setSize(Handler.SCREEN_SIZE+1, Handler.WINDOW_Y);
		inputField.setLocation(1, Handler.SCREEN_SIZE+2);
		inputField.setFont(new Font("Arial",Font.BOLD,60));
		inputField.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				Handler.interpret(inputField.getText());
			}
		});
		add(inputField);
		
		
        setVisible(true);
		
        new Thread(s).start();
	}
	
	private class Screen extends Canvas implements Runnable {


		/**
		 * Create a GameDisplay custom canvas.
		 */
		public Screen(int width, int height) {
			setSize(width,height);
			setLocation(1,1);
		}

		@Override
		public void run() {
			createBufferStrategy(2);
			while (true) {
				Graphics g = getBufferStrategy().getDrawGraphics();

				try {
					g.drawImage(Handler.getGraph(), 0, 0, this);
				} catch (Exception e) {
//					System.out.println(e.getMessage());
				}

				getBufferStrategy().show();

				try {
					Thread.sleep(100);
				} catch (InterruptedException ex) {
				}
			}
		}

	}
}