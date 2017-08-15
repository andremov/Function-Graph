/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author andresmovilla
 */
public class Point {
	
	private double x;
	private double y;

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public BufferedImage getImage() {
		BufferedImage img = new BufferedImage(2,2,BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		
		g.setColor(Color.red);
		g.fillRect(0,0,2,2);
		
		return img;
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return x-1;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y-1;
	}
	
}
