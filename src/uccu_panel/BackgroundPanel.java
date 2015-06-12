package uccu_panel;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.*;

public class BackgroundPanel extends JPanel{
	public Image img;
	public BackgroundPanel() {
		this.setLayout(null);
		this.setBounds(0,0,1366, 768);
	}
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		g.drawImage(img,0,0,this);
	}
}