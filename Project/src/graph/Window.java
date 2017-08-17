/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javafx.scene.input.KeyCode;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 *
 * @author Andres
 */
public class Window extends JFrame {
	
        Screen s;
    
	public Window() {
        setLayout(null);
        setSize(Handler.SCREEN_SIZE+Handler.EXTRA_X,Handler.SCREEN_SIZE+Handler.EXTRA_Y+Handler.WINDOW_Y);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Graficador de Funciones");
		
		s = new Screen(Handler.SCREEN_SIZE,Handler.SCREEN_SIZE);
		add(s);
		
		JTextField inputField = new JTextField();
		inputField.setSize(Handler.SCREEN_SIZE+1, Handler.WINDOW_Y);
		inputField.setLocation(1, Handler.SCREEN_SIZE+2);
		inputField.setFont(new Font("Arial",Font.BOLD,55));
		inputField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) { }

			@Override
			public void keyPressed(KeyEvent e) { }

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                                         s.paint(true);
					inputField.setBackground(Handler.graph(inputField.getText())? Handler.color(123.4f, 23.6f, 88.2f) : Handler.color(6.4f, 63f, 99.6f));
//                                        new Thread(s).
                                        s.paint(false);
				}
			}
		
		});
		add(inputField);
		
		
		
        setVisible(true);
//        new Thread(s).start();
	}
	
	private class Screen extends Canvas implements Runnable {


		/**
		 * Create a GameDisplay custom canvas.
		 */
		public Screen(int width, int height) {
			setSize(width,height);
			setLocation(1,1);
		}
                
                public void paint(boolean thinking) {
				Graphics g = getGraphics();

                                
				try {
					g.drawImage(Handler.getGraph(), 0, 0, this);
				} catch (Exception e) {
//					System.out.println(e.getMessage());
				}
                                
                                if (thinking) {
                                    g.setFont(new Font("Arial",Font.BOLD,30));
                                 g.drawString("Calculating...", 215, 310);
                                }

//				getBufferStrategy().show();
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