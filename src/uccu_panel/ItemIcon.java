package uccu_panel;

import java.awt.Graphics;
import java.awt.event.*;
import java.sql.Time;

import javax.swing.*;

import uccu_client.Item;
import uccu_client.Painter;
import uccu_client.Picture;
import uccu_client.SendingModule;

class ItemIcon extends UsableIcon{
	Item info;
	public ItemIcon(Item it){
		super(Painter.painter.getPicByPid(it.picID));
		info = it;
	}
	@Override
    public void myMouseEntered(MouseEvent e) {
    	int absX = e.getXOnScreen();
    	int absY = e.getYOnScreen();
    	Painter.painter.showPopupInfo(info.describtion,absX,absY);
    }
	@Override
    public void myMouseExit(MouseEvent e) {
    	Painter.painter.unshowPopupInfo();
    }
	@Override
	public void use(){
		long now = System.currentTimeMillis();
		long canuse = info.coldtime + info.lastuse;
		if(canuse > now){
			long lefttime = 1+(canuse-now)/1000;
			Painter.painter.localInform("物品冷却中，剩余时间："+lefttime+"秒");
			return;
		}
		info.lastuse = now;
		SendingModule.sendUseItem(info.instanceID,1,-1);
	}
}