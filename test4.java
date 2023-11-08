package mypackage.shapes;

import mypackage.Vector2f;
import java.lang.Math;
import java.io.File;
import java.io.IOException;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.*;
import javax.imageio.*;

public class test4 {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Key Character Printer");
        
        frame.add(new myPanel());
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class myPanel extends JPanel {
//	private ImageShape mario = new ImageShape("sprites/mario.png", new Vector2f(10, 20), new Vector2f(10, 20));
	public void paintComponent(Graphics g) {
		BufferedImage img = 

		mario.draw(g);
	}
}
