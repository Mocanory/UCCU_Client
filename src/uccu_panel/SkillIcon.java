package uccu_panel;

import java.awt.Graphics;
import java.awt.event.*;
import java.sql.Time;

import javax.swing.*;

import uccu_client.Item;
import uccu_client.Painter;
import uccu_client.Picture;
import uccu_client.SendingModule;
import uccu_client.Skill;

class SkillIcon extends UsableIcon{
	Skill info;
	long lastclick;
	public SkillIcon(Skill sk){
		super(Painter.painter.getPicByPid(sk.picID));
		info = sk;
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
			Painter.painter.localInform("技能冷却中，剩余时间："+lefttime+"秒");
			return;
		}
		info.lastuse = now;
		SendingModule.sendUseSkill(info.instanceID,0,-1);
	}
}
