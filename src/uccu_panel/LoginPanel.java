package uccu_panel;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
	ImageIcon[] loginIcon;
	ImageIcon[] registerIcon;
	public JLabel serverStat;
	public LoginPanel(LoginBox loginBox){
		IDField = new JTextField();
		PWDField = new JPasswordField();
		registerButton = new JButton();
		loginButton = new JButton();
		serverStat = new JLabel("未知");
		loginIcon = new ImageIcon[2];
		registerIcon = new ImageIcon[2];
		
		this.setLayout(null);
		this.setBounds(0,0, 192*3, 108*3);
		this.setBackground(new Color(0,0,0,0));
		
		ImageIcon vicIcon= new ImageIcon("loginbg.jpg");
		vicIcon.setImage(vicIcon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT) );
		JLabel bgLabel = new JLabel(vicIcon);
		bgLabel.setBounds(getBounds());
		this.add(bgLabel,new Integer(Integer.MIN_VALUE));
		
		IDField.setBounds(0, 0, 240, 30);
		this.add(IDField);
		PWDField.setBounds(0, 50, 240, 30);
		this.add(PWDField);
		serverStat.setBounds(0, 100, 50, 30);
		registerButton.setBounds(50, 100, 70, 30);
		registerButton.setBackground(new Color(0,0,0,0));
		registerButton.setBorderPainted(false);
		registerIcon[0] = new ImageIcon(Toolkit.getDefaultToolkit().getImage("登录2.jpg"));
		registerIcon[0].setImage(registerIcon[0].getImage().getScaledInstance(
				registerButton.getWidth(), registerButton.getHeight(), Image.SCALE_DEFAULT));
		registerIcon[1] = new ImageIcon(Toolkit.getDefaultToolkit().getImage("登录1.jpg"));
		registerIcon[1].setImage(registerIcon[1].getImage().getScaledInstance(
				registerButton.getWidth(), registerButton.getHeight(), Image.SCALE_DEFAULT));
		registerButton.setIcon(registerIcon[0]);
		this.add(registerButton);
		loginButton.setBounds(120, 100, 210, 90);
		loginButton.setBackground(new Color(0,0,0,0));
		loginButton.setBorderPainted(false);
		loginIcon[0] = new ImageIcon(Toolkit.getDefaultToolkit().getImage("登录2.jpg"));
		loginIcon[0].setImage(loginIcon[0].getImage().getScaledInstance(
				loginButton.getWidth(), loginButton.getHeight(), Image.SCALE_DEFAULT));
		loginIcon[1] = new ImageIcon(Toolkit.getDefaultToolkit().getImage("登录1.jpg"));
		loginIcon[1].setImage(loginIcon[1].getImage().getScaledInstance(
				loginButton.getWidth(), loginButton.getHeight(), Image.SCALE_DEFAULT));
		loginButton.setIcon(loginIcon[0]);
		this.add(loginButton);
		
		registerButton.addMouseListener(new MouseAdapter() {
			@Override
		    public void mousePressed(MouseEvent e) {
				registerButton.setIcon(registerIcon[0]);
		    }
			@Override
		    public void mouseReleased(MouseEvent e) {
				registerButton.setIcon(registerIcon[1]);
				String id = IDField.getText();
				String pwd = BasicLib.md5(PWDField.getText());
				if(id.equals("") || pwd.equals("")) return;
				SendingModule.sendRegister(id, pwd);
		    }
			@Override
		    public void mouseEntered(MouseEvent e) {
				registerButton.setIcon(registerIcon[1]);
		    }
			@Override
		    public void mouseExited(MouseEvent e) {
				registerButton.setIcon(registerIcon[0]);
		    }
		});
		loginButton.addMouseListener(new MouseAdapter() {
			@Override
		    public void mousePressed(MouseEvent e) {
				loginButton.setIcon(loginIcon[0]);
		    }
			@Override
		    public void mouseReleased(MouseEvent e) {
				loginButton.setIcon(loginIcon[1]);
				String id = IDField.getText();
				String pwd = PWDField.getText();
				if(id.equals("") || pwd.equals("")) return;
				ClientMain.userID = id;
				SendingModule.sendLogin(id, pwd);
		    }
			@Override
		    public void mouseEntered(MouseEvent e) {
		    	loginButton.setIcon(loginIcon[1]);
		    }
			@Override
		    public void mouseExited(MouseEvent e) {
				loginButton.setIcon(loginIcon[0]);
		    }
		});
	}
}