package uccu_panel;

import java.awt.Graphics;
import java.awt.event.*;

import javax.swing.*;

import uccu_client.Picture;


public abstract class UsableIcon extends JLabel{
	Picture pic;
	long lastclick;
	public UsableIcon(Picture p){ 
		pic = p;
		lastclick = 0;
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e){
				if(e.getButton() != MouseEvent.BUTTON1 ) return;
				if(e.getWhen() - lastclick > 500){
					lastclick = e.getWhen();
					return;
				}
				lastclick = 0;
				myMouseDoubleClicked(e);
			}
			@Override
		    public void mouseEntered(MouseEvent e) {
		    	myMouseEntered(e);
		    }
			@Override
		    public void mouseExited(MouseEvent e) {
		    	myMouseExit(e);
		    }
		});
	}
	public void use(){
		
	}
	public void myMouseDoubleClicked(MouseEvent e){
		use();
	}
	public void myMouseEntered(MouseEvent e){
		
	}
	public void myMouseExit(MouseEvent e){
		
	}
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(pic.getImage(), 0, 0, getWidth(),getHeight(),getParent());
	}
}