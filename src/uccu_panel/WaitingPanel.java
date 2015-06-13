package uccu_panel;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import uccu_client.Painter;

public class WaitingPanel extends JPanel {
	JLabel waitPic;
	Image waitBar;
	double stage;
	int barX = 0,barY,barH = 50;
	public WaitingPanel(Image pic,Image bar,Rectangle bounds) {
		this.setOpaque(false);
		this.setLayout(null);
		this.setBounds(bounds);
		barY = getHeight()-barH;
		waitBar = bar;
		stage = 0;
		waitPic = new JLabel("this is a wating(ju) icon(hua)");
		waitPic.setBounds((getWidth()-100)/2, (getHeight()-100)/2, 100, 100);
		waitPic.setIcon(new ImageIcon(pic));
		this.add(waitPic);
	}
	public void paintComponent(Graphics g){
		super.paintComponents(g);
//		img = Toolkit.getDefaultToolkit().getImage("background.jpg");
		int barW = (int)(getWidth()*stage);
		g.drawImage(waitBar,barX,barY,barW + barX,barY+barH,0,0,barW,barH,this);
	}
	public void setStage(double s){
		stage = s;
	}
}
