package uccu_panel;

import javax.swing.*;

import uccu_client.Airplane;
import uccu_client.Painter;

public class EntityMenu extends JPopupMenu{
	JMenuItem name;
	JMenuItem info;
	JMenuItem privatechat;
	Airplane owner;
	public EntityMenu() {
		name = new JMenuItem();
		info = new JMenuItem("玩家信息");
		privatechat = new JMenuItem("发起私聊");
		this.add(name);
		this.add(info);
		this.add(privatechat);
		info.addActionListener(e->{
			Painter.painter.showPlayerInfo(owner);
		});
		privatechat.addActionListener(e->{
			Painter.painter.setPrivateChat(owner);
		});
	}
	public void setOwner(Airplane o){
		owner = o;
		name.setText(o.name);
	}
}