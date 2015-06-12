package uccu_panel;

import java.nio.ByteBuffer;

import javax.swing.*;

import uccu_client.BasicLib;
import uccu_client.ClientMain;
import uccu_client.Datagram;
import uccu_client.LoginBox;
import uccu_client.SendingModule;

public class LoginPanel extends JPanel{
	JTextField IDField;
	JPasswordField PWDField;
	JButton registerButton;
	JButton loginButton;
	public JLabel serverStat;
	public LoginPanel(LoginBox loginBox){
		IDField = new JTextField();
		PWDField = new JPasswordField();
		registerButton = new JButton("注册");
		loginButton = new JButton("登录");
		serverStat = new JLabel("未知");
		this.setLayout(null);
		IDField.setBounds(0, 0, 240, 30);
		this.add(IDField);
		PWDField.setBounds(0, 50, 240, 30);
		this.add(PWDField);
		serverStat.setBounds(0, 100, 50, 30);
		registerButton.setBounds(50, 100, 70, 30);
		this.add(registerButton);
		loginButton.setBounds(120, 100, 70, 30);
		this.add(loginButton);
		this.setBounds(400, 400, 300, 200);
		this.setOpaque(false);
		
		registerButton.addActionListener(e->{
			String id = IDField.getText();
			String pwd = BasicLib.md5(PWDField.getText());
			if(id.equals("") || pwd.equals("")) return;
			SendingModule.sendRegister(id, pwd);
		});
		loginButton.addActionListener(e->{
			String id = IDField.getText();
			String pwd = PWDField.getText();
			if(id.equals("") || pwd.equals("")) return;
			ClientMain.userID = id;
			SendingModule.sendLogin(id, pwd);
		});
	}
}