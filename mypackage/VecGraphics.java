package mypackage;

import java.awt.Graphics;
import java.awt.Color;
import java.lang.Math;

public class VecGraphics {

	private Graphics g;

	public VecGraphics(Graphics g) {
		this.g = g;
	}
	public void drawLine(Vector2f v, Vector2f origin) {
		g.drawLine(Math.round(origin.getX()), Math.round(origin.getY()), Math.round(origin.getX() + v.getX()),Math.round(origin.getY() + v.getY()));
	}
	public void setColor(Color c) {
		g.setColor(c);
	}
}
