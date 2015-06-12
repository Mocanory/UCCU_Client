package uccu_panel;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WaitingPanel extends JPanel {
	JLabel waitPic;
	Image waitBar;
	double stage;
	int barX = 0,barY = 708,barH = 30;
	public WaitingPanel(Image pic,Image bar) {
		this.setOpaque(false);
		this.setLayout(null);
		waitBar = bar;
		stage = 0;
		waitPic = new JLabel("this is a wating(ju) icon(hua)");
		waitPic.setSize(100, 100);
		waitPic.setBounds(600, 300, 100, 100);
		waitPic.setIcon(new ImageIcon(pic));
		this.add(waitPic);
		this.setBounds(0, 0, 1366, 768);
	}
	public void paintComponent(Graphics g){
		super.paintComponents(g);
//		img = Toolkit.getDefaultToolkit().getImage("background.jpg");
		int barW = (int)(1366*stage);
		g.drawImage(waitBar,barX,barY,barW + barX,barY+barH,0,0,barW,barH,this);
	}
	public void setStage(double s){
		stage = s;
	}
}
