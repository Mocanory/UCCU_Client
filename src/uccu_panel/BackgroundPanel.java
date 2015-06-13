package uccu_panel;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.*;

public class BackgroundPanel extends JPanel{
	public Image img;
	public BackgroundPanel(Rectangle bounds) {
		this.setLayout(null);
		this.setBounds(bounds);
	}
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		g.drawImage(img,0,0,this);
	}
}