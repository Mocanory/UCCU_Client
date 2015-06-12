package uccu_panel;

import javax.swing.*;

import uccu_client.Painter;
import uccu_client.Painter.FrameType;

public class IconPanel extends JPanel{
	JButton info;
	JButton bag;
	JButton skill;
	JButton friend;
	final int iconW = 70,iconH = 70;
	public IconPanel(){
		this.setLayout(null);
		this.setOpaque(false);
		info = new JButton("info");
		bag = new JButton("bag");
		skill = new JButton("skill");
		friend  = new JButton("friend");
		
		info.setBounds(0*iconW, 0, iconW,iconH);
		bag.setBounds(1*iconW, 0, iconW,iconH);
		skill.setBounds(2*iconW, 0, iconW,iconH);
		friend.setBounds(3*iconW, 0, iconW,iconH);
		info.addActionListener(e->{
			Painter.painter.showInnerFrame(FrameType.info);
		});
		bag.addActionListener(e->{
			Painter.painter.showInnerFrame(FrameType.bag);
		});
		skill.addActionListener(e->{
			Painter.painter.showInnerFrame(FrameType.skill);
		});
		friend.addActionListener(e->{
			Painter.painter.showInnerFrame(FrameType.friend);
		});
		
		this.add(info);
		this.add(bag);
		this.add(skill);
		this.add(friend);
		this.setSize(4*iconW,iconH);
	}
}