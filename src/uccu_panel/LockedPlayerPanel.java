package uccu_panel;

import java.awt.*;

import javax.swing.*;

import uccu_client.Airplane;
import uccu_client.Painter;
import uccu_client.Picture;

public class LockedPlayerPanel extends JPanel{
	Picture headPic;
	Airplane player;
	Font font;
	public LockedPlayerPanel(){
		this.setOpaque(false);
		this.setLayout(null);
		this.setBounds(600,50,200,100);
		this.setVisible(false);
		font = Font.decode("黑体");
	}
	public void setPlayer(Airplane airplane){
		this.setVisible(false);
		player = airplane;
		headPic = Painter.painter.getPicByPid(airplane.getPicID());
		this.setVisible(true);
	}
	@Override
	public void paintComponent(Graphics g){
		g.drawImage(headPic.getImage(), 0, 0,headPic.getWidth(),headPic.getHeight(),this);
		g.setColor(Color.RED);
		g.setFont(font);
		g.drawChars(player.name.toCharArray(), 0, player.name.length(), headPic.getWidth()+20, headPic.getHeight()/4);
	}
}