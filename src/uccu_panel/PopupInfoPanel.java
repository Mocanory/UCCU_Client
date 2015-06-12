package uccu_panel;

import java.awt.Graphics;

import javax.swing.*;

import uccu_client.Picture;

public class PopupInfoPanel extends JPanel{
	JTextArea text;
	Picture backg;
	public PopupInfoPanel(Picture bg){
		backg = bg;
		text = new JTextArea();
		text.setOpaque(false);
		text.setEditable(false);
		this.add(text);
	}
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(backg.getImage(),0,0,getWidth(), getHeight(), this);
	}
	public void showInfo(String t,int x,int y){
		text.setText(t);
		setLocation(x, y);
		setSize(100, 200);
		setVisible(true);
	}
}