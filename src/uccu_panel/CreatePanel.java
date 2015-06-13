package uccu_panel;

import java.nio.ByteBuffer;

import javax.swing.*;

import uccu_client.ClientMain;
import uccu_client.Datagram;
import uccu_client.LoginBox;
import uccu_client.SendingModule;

public class CreatePanel extends JPanel{
	JButton createButton;
	JButton backButton;
	JTextField nameField;
	LoginBox parent;
	public CreatePanel(LoginBox lb){
		parent = lb;
		nameField = new JTextField();
		createButton = new JButton("create");
		createButton.addActionListener(e->{
			String name = nameField.getText();
//			这两个地方都不要写死
			byte gender = 1;
			int picID=11;
			SendingModule.sendCreateCharacter(name,gender,picID);
		});
		backButton = new JButton("back");
		backButton.addActionListener(e->{
			parent.createBack();
		});
		
		this.setLayout(null);
		this.setOpaque(false);
		nameField.setBounds(500, 300, 100, 50);
		this.add(nameField);
		createButton.setBounds(500, 500, 100, 50);
		this.add(createButton);
		backButton.setBounds(500, 600, 100, 50);
		this.add(backButton);
	}
}