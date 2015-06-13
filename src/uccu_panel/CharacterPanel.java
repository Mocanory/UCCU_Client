package uccu_panel;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.nio.ByteBuffer;

import javax.swing.*;

import uccu_client.ClientMain;
import uccu_client.Datagram;
import uccu_client.LoginBox;
import uccu_client.Picture;
import uccu_client.SendingModule;

class CharacterInfo{
	int id;
	String name;
	byte level;
	byte gender;
	int picID;
	public CharacterInfo(	int id,String name,byte level,	byte gender,int picID) {
		this.id = id;
		this.name = name;
		this.level = level;
		this.gender = gender;
		this.picID=picID;
	}
}
public class CharacterPanel extends JPanel{
	JButton[] characterButton;
	JTextField nameText;
	JTextField levelText;
	JTextField genderText;
	JButton entryButton;
	JButton createButton;
	CharacterInfo[] characterInfo;
	LoginBox parent;
	int select;
	int characterCount;
	public CharacterPanel(LoginBox loginBox){
		parent = loginBox;
		characterCount = 0;
		select = -1;
		nameText = new JTextField();
		levelText = new JTextField();
		genderText = new JTextField();
		createButton = new JButton("create");
		createButton.addActionListener(e->{
			parent.beginCreate();
		});
		entryButton = new JButton("start game");
		entryButton.addActionListener(e->{
			if(select == -1) return;
			int pid = characterInfo[select].id;
			ClientMain.mainID = pid;
			SendingModule.sendCharacter(pid);
			ClientMain.isLoginsuccess = true;
			ClientMain.isLoginOver = true;
			loginBox.dispose();
		});
		characterButton = new JButton[8];
		characterInfo = new CharacterInfo[8];
		for(int i=0;i<8;++i){
			characterButton[i] = new JButton();
			characterButton[i].setName(String.valueOf(i));
			characterButton[i].setSize(250, 300);
			characterButton[i].setOpaque(false);
			characterButton[i].setContentAreaFilled(false);
			characterButton[i].setBorderPainted(false);
			characterButton[i].addActionListener(e->{
				int buttonNum = Integer.parseInt( ((JButton)e.getSource()).getName() );
				if(buttonNum >= characterCount) return;
				select = buttonNum;
				nameText.setText( characterInfo[select].name );
				levelText.setText( String.valueOf(characterInfo[select].level) );
				genderText.setText( characterInfo[select].gender == 0 ? "boy" : "girl" );
			});
		}
		
		this.setOpaque(false);
		this.setLayout(null);
		JPanel cbp = new JPanel();
		cbp.setOpaque(false);
		cbp.setLayout(new GridLayout(2,4,0,0));
		for(JButton cb : characterButton)
			cbp.add(cb);
		cbp.setBounds(100, 100, 1000, 400);
		this.add(cbp);
		
		JPanel lbp = new JPanel();
		lbp.setOpaque(false);
		lbp.setLayout(new GridLayout(3,2,0,150));
		lbp.add(new JLabel("name"));
		nameText.setSize(100,30);
		lbp.add(nameText);
		lbp.add(new JLabel("level"));
		levelText.setSize(100,30);
		lbp.add(levelText);
		lbp.add(new JLabel("gender"));
		genderText.setSize(100,30);
		lbp.add(genderText);
		lbp.setBounds(1100, 0, 200, 600);
		this.add(lbp);
		
		entryButton.setBounds(1100,600,100,50);
		this.add(entryButton);
		createButton.setBounds(1100,680,100,50);
		this.add(createButton);
	}
	public void addCharacter(int id,String name,byte level, byte gender,int picID){
		if(characterCount == 8) return;
		characterInfo[characterCount] = new CharacterInfo(id,name,level, gender,picID);
		characterButton[characterCount].setIcon(new ImageIcon(
				Toolkit.getDefaultToolkit().getImage("loading.gif") ));
		characterCount++;
	}
}