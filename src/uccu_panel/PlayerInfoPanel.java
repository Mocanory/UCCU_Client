package uccu_panel;

import javax.swing.JInternalFrame;

public class PlayerInfoPanel extends JInternalFrame{

	public PlayerInfoPanel(String text, boolean re, boolean cl) {
		super(text, re, cl);
		this.setLocation(20, 20);
		this.setSize(200, 200);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
	
}