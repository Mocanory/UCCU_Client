package uccu_panel;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

import javax.swing.*;

import uccu_client.Airplane;
import uccu_client.Entity;
import uccu_client.Painter;
import uccu_client.Picture;
import uccu_client.Entity.style;

public class EntityLabel extends JLabel{
	Entity entityInfo;
	Picture pic;
//	static final AffineTransform identity = new AffineTransform();
	public EntityLabel(Entity e,Picture p,boolean clickable){
		entityInfo = e;
		pic = p;
		this.setOpaque(false);
		this.setLayout(null);
		this.setSize(pic.getWidth(), pic.getHeight());
		if(clickable) this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e){
				Entity mr = ((GameWindow)getParent()).mainRole;
				int absX = (int)entityInfo.posX + e.getX() - pic.getWidth()/2;
				int absY = (int)entityInfo.posY + e.getY() - pic.getHeight()/2;
				int relX = absX - (int)mr.posX + Painter.width/2;
				int relY = absY - (int)mr.posY + Painter.height/2;
				switch(e.getButton()){
				case MouseEvent.BUTTON1 :
					Painter.painter.button1Clicked(absX, absY);
					break;
				case MouseEvent.BUTTON2 :
					if(entityInfo.getType() != style.airplane) return;
					((GameWindow)getParent()).showEntityMenu((Airplane)entityInfo, relX, relY);
					break;
				case MouseEvent.BUTTON3:
					if(entityInfo.getType() != style.airplane) return;
					Painter.painter.setLockedPlayer((Airplane)entityInfo);
				}
			}
		});
		return;
	}
	public void flashPos(Graphics2D gbuffer,double X,double Y){
		int relX = (int)(entityInfo.posX - X + Painter.width/2);
		int relY = (int)(entityInfo.posY - Y + Painter.height/2);
		int picW = pic.getWidth();
		int picH = pic.getHeight();
		if(relX + picW <= 0 || relX - picW >= Painter.width || relY + picH <= 0 || relY - picH >= Painter.height){
			this.setVisible(false);
			return;
		}
		this.setVisible(true);
		this.setBounds(relX - picW/2, relY - picH/2, picW, picH);
		
		double theta = entityInfo.angle;
		double hp = entityInfo.hp;
		AffineTransform savedT = gbuffer.getTransform();
        gbuffer.rotate(theta, relX, relY);
        gbuffer.drawImage(pic.getImage() ,relX-picW/2 ,relY-picH/2, picW, picH, getParent());  
        gbuffer.setTransform(savedT);
        if(hp >= 0)
        {
            gbuffer.setColor(Color.BLACK);
            gbuffer.fillRect(relX-picW/2 ,relY-picH/2, pic.getWidth(), 5);
            gbuffer.setColor(Color.RED);
            gbuffer.fillRect(relX-picW/2 ,relY-picH/2, (int)(pic.getWidth()*hp), 5);
        }
        return;
	}
}