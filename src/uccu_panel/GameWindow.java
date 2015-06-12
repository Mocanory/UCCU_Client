package uccu_panel;

import java.awt.*;

import javax.swing.*;

import uccu_client.Airplane;
import uccu_client.Entity;

public class GameWindow extends JPanel{
	public Image img;
	EntityMenu entityMenu;
    Entity mainRole;
	public void showEntityMenu(Airplane e,int x,int y){
		entityMenu.setOwner(e);
		entityMenu.show(this, x, y);
	}
	public void setMainRole(Entity mr){
		mainRole = mr;
	}
	public GameWindow() {
		this.setLayout(null);
		this.setBounds(0,0,1366, 768);
		entityMenu = new EntityMenu();
	}
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		g.drawImage(img,0,0,this);
	}
}