package uccu_panel;

import javax.swing.*;

import uccu_client.Picture;

public class BagPanel extends JInternalFrame{
	JPanel bp;
	BackgroundPanel backg;
	final int lbW = 50,lbH = 50;
	public BagPanel() {
		super("bag",false,true);
		this.setLocation(20, 20);
		this.setSize(100+4*lbW, 100+8*lbH);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setLayout(null);
		bp = null;
		backg = new BackgroundPanel();
		backg.setSize(getSize());
		backg.img = new Picture("lf.jpg", 0, 0, 0).getImage();
		this.getRootPane().add(backg);
		((JPanel)this.getContentPane()).setOpaque(false);
	}
	public void showPanel(){
//		if(1 == 1)return;
		if(bp != null) this.remove(bp);
		bp = new JPanel();
		bp.setOpaque(false);
		bp.setLayout(null);
		bp.setBounds(50, 50, 4*lbW, 8*lbH);
		this.add(bp);
		for(int i=0;i<32;++i){
			ItemIcon item = new ItemIcon(new Picture("0.png", 0, 0, 0), "123\n1231\neee", 1);
			item.setBounds((i%4)*lbW,(i/4)*lbH,50,50);
			bp.add(item);
		}
		this.show();
	}
}