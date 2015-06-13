/* 登录窗体类 */
package uccu_client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import uccu_panel.BackgroundPanel;
import uccu_panel.CharacterPanel;
import uccu_panel.CreatePanel;
import uccu_panel.LoginPanel;
import uccu_panel.WaitingPanel;

public class LoginBox extends JFrame{
	/* 登录结束后，应修改ClientMain中的isLoginOver,isLoginsuccess为对应值 */
	private BackgroundPanel backPicPanel;
	private LoginPanel loginPanel;
	private WaitingPanel waitingPanel;
	private CharacterPanel characterPanel;
	private CreatePanel createPanel;
	Dimension  screensize;
	public void createBack(){
		createPanel.setVisible(false);
		characterPanel.setVisible(true);
	}
	public void beginCreate(){
		characterPanel.setVisible(false);
		this.add(createPanel);
		createPanel.setVisible(true);
	}
	public LoginBox(){
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				ClientMain.isLoginOver = true;
				((JFrame)e.getSource()).dispose();
			}
		});
		this.setLayout(null);
		screensize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setUndecorated(true);
		this.setBackground(new Color(0,0,0,0));
		loginPanel = new LoginPanel(this);
		this.setSize(loginPanel.getSize());
		this.setLocation(((int)screensize.getWidth()-(int)getSize().getWidth())/2
				, ((int)screensize.getHeight() - (int)getSize().getHeight())/2);
		this.add(loginPanel);
		backPicPanel = new BackgroundPanel(new Rectangle(0,0,1366,768));
		waitingPanel = new WaitingPanel(
				Toolkit.getDefaultToolkit().getImage("loading.gif")
				,Toolkit.getDefaultToolkit().getImage("bar.png")
				,new Rectangle(0,0,1366,768));
		characterPanel = new CharacterPanel(this);
		characterPanel.setBounds(0, 0, 1366, 768);
		createPanel = new CreatePanel(this);
		createPanel.setBounds(0, 0, 1366, 768);
	}
	public void init(){
//		backPicPanel.img = Toolkit.getDefaultToolkit().getImage("bg1.jpg");
		this.setVisible(true);	
	}
	public void onLoginResponse(boolean res){
		UccuLogger.log("ClientServer/LoginBox/onLoginResponse", "Receive a package 0003(登陆反馈)");
		UccuLogger.log("ClientServer/LoginBox/onLoginResponse", "package 0003: "+res);
		if(res){
			this.setVisible(false);
			this.setSize(1366, 768);
			this.setLocation(((int)screensize.getWidth()-(int)getSize().getWidth())/2
					, ((int)screensize.getHeight() - (int)getSize().getHeight())/2);
			this.getLayeredPane().add(backPicPanel,new Integer(Integer.MIN_VALUE));
			((JPanel)this.getContentPane()).setOpaque(false);
			loginPanel.setVisible(false);
			this.add(waitingPanel);
			backPicPanel.img = Toolkit.getDefaultToolkit().getImage("lf.jpg");
			try {Thread.sleep(500);} catch (InterruptedException e) {}
			this.setVisible(true);//backPicPanel.repaint();
		}
		else{
			JOptionPane.showMessageDialog(null, "login fail!");
		}
		//decoder得到一个登录反馈时调用该函数
	}
	//在Loginresponse里面确定了角色ID之后，需将ID传到gameBox中的mainID里	
	public void onRegistResponse(boolean res){
		UccuLogger.log("ClientServer/LoginBox/onRegistResponse", "Receive a package 0005(注册结果)");
		UccuLogger.log("ClientServer/LoginBox/onRegistResponse", "package 0005: "+res);
		if(res){
			JOptionPane.showMessageDialog(null, "regist success!");
		}
		else{
			JOptionPane.showMessageDialog(null, "regist fail!");
		}
		//decoder得到一个注册反馈时调用该函数
	}
	public void onCreatResponse(boolean res){
		UccuLogger.log("ClientServer/LoginBox/onCreatResponse", "Receive a package 0009(角色创建结果)");
		if(res){
			JOptionPane.showMessageDialog(null, "create success!");
		}
		else{
			JOptionPane.showMessageDialog(null, "create fail!");
		}
	}//创建角色成功否
	public void noMorePackage() {
		UccuLogger.log("ClientServer/LoginBox/noMorePackage", "Receive a package 0006(角色预加载: 这是最后一个空包");
		this.setVisible(false);
		waitingPanel.setVisible(false);
		this.add(characterPanel);
		backPicPanel.img = Toolkit.getDefaultToolkit().getImage("background.jpg");
		try {Thread.sleep(500);} catch (InterruptedException e) {}
		this.setVisible(true);//backPicPanel.repaint();
	}
	public void addCharacter(int id,String name,byte level, byte gender,int picID){
		UccuLogger.log("ClientSever/LoginBox/addCharacter", "Receive a package 0006(角色预加载)");
		characterPanel.addCharacter(id,name,level,gender,picID);
		characterPanel.repaint();
	}
	public void serverState(byte stat){
		UccuLogger.log("ClientSever/LoginBox/serverState", "Receive a package 0001(回应连接请求)");
		UccuLogger.log("ClientSever/LoginBox/serverState", "package 0001: "+stat);
		ClientMain.isGateComfirm = true;
		//0维护 1 空闲 2爆满
		//Decoder 收到0001包后调用loginBox的serverState函数，loginBox更新服务器信息并repaint出来，在此之前阻塞在服务器未知状态阶段
		switch(stat){
		case 0:
			loginPanel.serverStat.setText("维护");break;
		case 1:
			loginPanel.serverStat.setText("空闲");break;
		case 2:
			loginPanel.serverStat.setText("爆满");break;
		}
		this.repaint();
	}
}









