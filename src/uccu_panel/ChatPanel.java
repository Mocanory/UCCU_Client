package uccu_panel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.ByteBuffer;

import javax.swing.*;

import uccu_client.*;
import uccu_client.Painter;
import uccu_client.Entity.style;

public class ChatPanel extends JPanel{
	Picture backg;
	boolean isHide;
    public enum puborpri{
    	pub,pri
    }
    public puborpri labelstat;
    Entity priID;
//	AioSession session;
	int mainID;
	int MaxX,MaxY;
	JLabel publicLabel;
	JLabel privateLabel;
	final int lbW = 100,lbH = 20;
    JTextArea publicViewArea;
    JTextArea privateViewArea;
    final int vaMaxW = 700,vaMaxH = 170,vaMinW = 700,vaMinH = 30;
    JTextField editField;
    final int efW = 600,efH = 30;
    JButton hideButton;
    final int hbW = 30,hbH = 30;
    JButton sendButton;
    final int sbW = 70,sbH = 30;
    JScrollPane publicSp;
    JScrollPane privateSp;
    JScrollBar publicChatBar;
    JScrollBar privateChatBar;
    public ChatPanel() {
//    	session = s;
    	backg = new Picture("lf.jpg", 0, 0, 0);
    	MaxX = Painter.width;MaxY = Painter.height;
    	publicLabel = new JLabel("公聊");
    	privateLabel = new JLabel("私聊");
        publicViewArea = new JTextArea(10, 50);
        privateViewArea = new JTextArea(10,50);
        publicViewArea.setEditable(false);
        privateViewArea.setEditable(false);
        editField = new JTextField(50);
        sendButton = new JButton("Send");
        hideButton = new JButton("...");
        publicSp = new JScrollPane(publicViewArea);
        privateSp = new JScrollPane(privateViewArea);
    	publicChatBar = publicSp.getVerticalScrollBar();
    	privateChatBar = privateSp.getVerticalScrollBar();
        
    	this.setOpaque(false);
        this.setLayout(null);
        publicSp.setOpaque(false);
        publicSp.getViewport().setOpaque(false);
        publicViewArea.setOpaque(false);
        publicViewArea.setForeground(Color.white);
        publicSp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        publicSp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        privateSp.setOpaque(false);
        privateSp.getViewport().setOpaque(false);
        privateViewArea.setOpaque(false);
        privateViewArea.setForeground(Color.white);
        privateSp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        privateSp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        publicLabel.setBounds(0, 0, lbW, lbH);
        publicLabel.setForeground(Color.WHITE);
        privateLabel.setBounds(lbW, 0, lbW, lbH);
        privateLabel.setForeground(Color.WHITE);
        this.add(publicLabel);
        this.add(privateLabel);
        publicSp.setBounds(0, lbH, vaMaxW, vaMaxH);
        this.add(publicSp);
        privateSp.setBounds(0, lbH, vaMaxW, vaMaxH);
        this.add(privateSp);
        editField.setBounds(0,lbH+vaMaxH,efW,efH);
        this.add(editField);
        sendButton.setBounds(efW,lbH+vaMaxH,sbW,sbH);
        this.add(sendButton);
        hideButton.setBounds(efW + sbW, lbH+vaMaxH, hbW, hbH);
        this.add(hideButton);
        this.setBounds(0, MaxY - (vaMaxH + efH + lbH), vaMaxW, vaMaxH + efH + lbH);
        isHide = false;
        
        publicSp.setVisible(true);
        privateSp.setVisible(false);
        labelstat = puborpri.pub;
        
        publicLabel.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e){
        		privateSp.setVisible(false);
        		publicSp.setVisible(true);
        		labelstat = puborpri.pub;
        	}
		});
        privateLabel.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e){
        		publicSp.setVisible(false);
        		privateSp.setVisible(true);
        		labelstat = puborpri.pri;
        	}
		});
        editField.addActionListener(e->{
        	sendButton.doClick();
        });
        hideButton.addActionListener(e->{
        	if(isHide) maxViewArea();
        	else hideViewArea();
    		publicChatBar.setValue(publicChatBar.getMaximum());//为了把滚动条拖到最下面，直接设置不行
    		privateChatBar.setValue(privateChatBar.getMaximum());
        });
        sendButton.addActionListener(e->{
        	String t = editField.getText();
        	editField.setText("");
        	if(t.equals("")) return;
        	if(t.length() > 10)
        		insertChat("[WARNING] You must talk short!",labelstat);
        	else{
        		switch (labelstat) {
				case pub:
					SendingModule.sendPubChat(t);
					break;
				case pri:
					if(priID == null) insertChat("[WARNING] 你必须指定一个私聊对象",labelstat);
					else SendingModule.sendPriChat(priID.getID(), t);
					break;
				}
        	}
        });
	}
    public void hideViewArea(){
    	publicLabel.setVisible(false);
    	privateLabel.setVisible(false);
        publicSp.setBounds(0, 0, vaMinW, vaMinH);
        privateSp.setBounds(0, 0, vaMinW, vaMinH);
        editField.setBounds(0,vaMinH,efW,efH);
        sendButton.setBounds(efW,vaMinH,sbW,sbH);
        hideButton.setBounds(efW + sbW, vaMinH, hbW, hbH);
        this.setBounds(0, MaxY - (vaMinH + efH), vaMinW, vaMinH + efH);
        isHide = true;
    	publicSp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    	privateSp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    }
    public void maxViewArea(){
    	publicSp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        publicSp.setBounds(0, lbH, vaMaxW, vaMaxH);
    	privateSp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        privateSp.setBounds(0, lbH, vaMaxW, vaMaxH);
        editField.setBounds(0,vaMaxH+lbH,efW,efH);
        sendButton.setBounds(efW,vaMaxH+lbH,sbW,sbH);
        hideButton.setBounds(efW + sbW, vaMaxH+lbH, hbW, hbH);
        this.setBounds(0, MaxY - (vaMaxH + efH+lbH), vaMaxW, vaMaxH + efH+lbH);
       	publicLabel.setVisible(true);
    	privateLabel.setVisible(true);
        isHide = false;
    }
    public void insertChat(String str,puborpri p){
    	switch(p){
    	case pub:
			publicViewArea.append("\n"+str);
			publicChatBar.setValue(publicChatBar.getMaximum());
			break;
    	case pri:
			privateViewArea.append("\n"+str);
			privateChatBar.setValue(privateChatBar.getMaximum());
			break;
    	}
    }
    public void setPrivateChat(Entity e){
    	if(e.getType() != style.airplane) return;
    	String name = ((Airplane)e).name;
    	privateLabel.setText("私聊："+name);
    	priID = e;
    	publicSp.setVisible(false);
    	privateSp.setVisible(true);
    	labelstat = puborpri.pri;
    }
    @Override
    public void paintComponent(Graphics g){
    	super.paintComponent(g);
    	int top = lbH;
    	if(isHide) top = 0;
    	g.drawImage(backg.getImage(), 0,top,getWidth(), getHeight(), this);
    }
}