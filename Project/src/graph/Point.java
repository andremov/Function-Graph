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
	
	float x;
	float y;

	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public BufferedImage getImage() {
		BufferedImage img = new BufferedImage(5,5,BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		
		g.setColor(Color.red);
		g.fillRect(0,0,5,5);
		
		return img;
	}
	
}
