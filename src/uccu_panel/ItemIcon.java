package uccu_panel;

import java.awt.Graphics;
import java.awt.event.*;

import javax.swing.*;

import uccu_client.Painter;
import uccu_client.Picture;

class ItemIcon extends JLabel{
	Picture pic;
	String info;
	int num;
	long lastclick;
	public ItemIcon(Picture p,String inf,int n){
		pic = p;
		info = inf;
		num = n;
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
				useItem();
			}
			@Override
		    public void mouseEntered(MouseEvent e) {
		    	int absX = e.getXOnScreen();
		    	int absY = e.getYOnScreen();
		    	Painter.painter.showPopupInfo(info,absX,absY);
		    }
			@Override
		    public void mouseExited(MouseEvent e) {
		    	Painter.painter.unshowPopupInfo();
		    }
		});
	}
	public void useItem(){
		//TODO
		System.out.println("useItem");
	}
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(pic.getImage(), 0, 0, getWidth(),getHeight(),getParent());
	}
}