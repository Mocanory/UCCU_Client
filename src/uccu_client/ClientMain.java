/* 客户端的入口 
 * 5-30 版本下午17:52 修改了loginBox的 isGateComfirm变量
 * */
package uccu_client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

import javax.swing.JOptionPane;

import uccu_client.GameBox.chatStat;

public class ClientMain {
	static String hostName;	//Gate服务器地址
	public static String userID;	//用户账户名
	static int port;		//Gate服务器端口号
	public static int mainID;		//用户主角ID(全服务器唯一标识)
	static public AioModule clientAio;	//客户端异步通信模块
	static public AioSession serverSession;	//客户端与Gate服务器的会话
	static public LoginBox loginBox;	//登陆界面
	static public GameBox gameBox;		//游戏界面
	static boolean isGateComfirm; //几个状态判定函数
	public static boolean isLoginOver;
	public static boolean isLoginsuccess;
	static boolean isGameOver;
	static boolean deBug =true;//调试模式
	public static void main(String[] arg) {
		init(LogMode.DEBUG);
		UccuLogger.kernel("ClientServer/ClientMain", "0.开始建立网络连接…………");
		setUpConnection();
		UccuLogger.kernel("ClientServer/ClientMain", "1.网络连接成功!");
		UccuLogger.kernel("ClientServer/ClientMain", "2.发送0000包，确认对方是否为服务器Gate端");
		SendingModule.send00Package();
		myDebug(1);//debug调试模式使用
		while(!isGateComfirm){mySleep(200);}
		UccuLogger.kernel("ClientServer/ClientMain", "3.收到0001包，确认对方为服务器Gate端");
		UccuLogger.kernel("ClientServer/ClientMain", "4.初始化loginBox登陆界面");
		loginBox.init();
		myDebug(2);//debug调试模式使用
		while (!isLoginOver) {mySleep(200);}
		UccuLogger.kernel("ClientServer/ClientMain", "5.登陆结束");
		if (!isLoginsuccess) {UccuLogger.kernel("ClientServer/ClientMain", "登陆失败，退出主程序");procExit();}
		UccuLogger.kernel("ClientServer/ClientMain", "6.开始游戏，启动gameBox");
		gameBox.startGame();
		myDebug(3);//debug调试模式使用
		while (!isGameOver) {mySleep(500);myDebug(4);}//debug调试模式使用	
		UccuLogger.kernel("ClientServer/ClientMain", "7.游戏结束，退出主程序");
		procExit();
	}
	//几个辅助函数，简化main流程
	private static void init(int style){
		//注册logger
		UccuLogger.setOptions("logs/ClientServer/", style);
		//从文件加载hostName和port
//		InputStream inputStream = ClientMain.class.getResourceAsStream("uccu_client/Config.properties");   
		InputStream inputStream = null;
		try {inputStream = new FileInputStream(new File("Config.properties"));
		} catch (FileNotFoundException e) {e.printStackTrace();}
		Properties p = new Properties();   
		try {p.load(inputStream);} catch (IOException e1) {e1.printStackTrace();}   
		hostName=p.getProperty("hostName");
		port=Integer.parseInt(p.getProperty("port"));  
		UccuLogger.debug("ClientServer/ClientMain/init", "address: "+hostName+":"+port);
		//加载各张图片
		int picNum= Integer.parseInt(p.getProperty("picNum"));  
		PainterProperty.picp = new PicProperty[picNum];
		for(int i=0;i<picNum;++i){
			Scanner s = new Scanner(p.getProperty("pic"+i));
			PainterProperty.picp[i]=new PicProperty(s.next(),s.nextInt(),s.nextInt(),s.nextInt());
		}
		UccuLogger.debug("ClientServer/ClientMain/init", "图片加载成功");
		int gifNum= Integer.parseInt(p.getProperty("gifNum"));  
		PainterProperty.gifp = new GIFPicProperty[gifNum];
		for(int i=0;i<gifNum;++i){
			Scanner s = new Scanner(p.getProperty("gif"+i));
			PainterProperty.gifp[i]=new GIFPicProperty(s.next(),s.nextInt(),s.nextInt(),s.nextInt(),s.nextInt(),s.nextInt(),s.nextInt());
		}	
		UccuLogger.debug("ClientServer/ClientMain/init", "GIF动画加载成功");
		//预加载物品
		int itemNum = Integer.parseInt(p.getProperty("itemNum"));
		Mainrole.pre_items= new Item[itemNum];
		for(int i=0;i<itemNum;++i){
			Scanner s = new Scanner(p.getProperty("item"+i));
			Mainrole.pre_items[i]=new Item(s.nextInt(),s.next(),s.next());
		}
		UccuLogger.debug("ClientServer/ClientMain/init", "物品预加载成功");
		// 标记初始状态为false
		isGateComfirm = isGameOver = isLoginOver = isLoginsuccess = false;
		// 一定要先构造LoginBox和GameBox再构造ClientDecoder，否则decoder会得不到他们正确的引用
		loginBox = new LoginBox();
		gameBox = new GameBox();
		clientAio = new AioModule(null, null, null);
		clientAio.init(null, -1, 4);
		UccuLogger.debug("ClientServer/ClientMain/init", "init over");
	}
	private static void setUpConnection() {
		if ((serverSession = clientAio.connect(hostName, port,
				new ClientDecoder(), new ClientReaper())) == null) {
			UccuLogger.kernel("ClientServer/ClientMain", "1.网络连接失败");
			JOptionPane.showMessageDialog(null, "网络连接失败", "错误提示@ClientServer",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		serverSession.asyncRead();
		UccuLogger.debug("ClientServer/ClientMain/setUpConnection", "setUpConnection over");
	}
	private static void procExit(){
		SendingModule.sendLogout();
		System.exit(0);//不然有些线程没结束掉还会继续运行
	}
	private static void myDebug(int num){
		if (deBug) {
			if (num == 1) {
//				mySleep(500);
				loginBox.serverState((byte) 1);// 假装连接成功，服务器处于空闲状态
			}
			else if (num == 2) {
//				mySleep(2000);
				loginBox.onLoginResponse(true);// 假装收到了一个登录成功反馈
				// loginBox.onRegistResponse(true);//假装收到了一个注册成功反馈
//				mySleep(1000);
				// 假装收到了两个角色信息
				loginBox.addCharacter(1, "first role", (byte) 12, (byte) 0);
				loginBox.addCharacter(2, "second role", (byte) 15, (byte) 1);
				loginBox.noMorePackage();
//				mySleep(1000);
				// 假装再创建1个角色
				SendingModule.sendCreateCharacter("dddd",(byte) 1);
				// 假装角色创建成功
				loginBox.onCreatResponse(true);
				//创建成功后服务器还会再发回来一个包代表刚创建的角色
				loginBox.addCharacter(3, "third role", (byte) 1, (byte) 1);
				//然后客户端发送一个角色选择的包，登陆结束
				ClientMain.mainID = 3;	//选出了一个主角ID
				ClientMain.isLoginsuccess = true;
				ClientMain.isLoginOver = true;
				loginBox.dispose();
				//收到两个角色详细的包，其中一个是mainID代表的主角
				gameBox.addCharacter(3,"third role", (byte)12, (byte)1,(byte)1,50,50);
				gameBox.addCharacter(5,"another role", (byte)13, (byte)1,(byte)1,100,100);
			}
			else if(num==3){//模拟移动情况
//				gameBox.updateTarget(3, 2000, 5000);
				gameBox.updateTarget(5, 10000, 20000,System.currentTimeMillis());
			}
			else if(num==4){	//模拟各种聊天请求
				mySleep(500);
				double ran=Math.random();
				if(ran<0.08){gameBox.painter.allMapChat(chatStat.frequency,0,"");}
				else if(ran<0.16){gameBox.painter.allMapChat(chatStat.nopermission,0,"");}
				else if(ran<0.25){gameBox.painter.allMapChat(chatStat.wrong,0,"");}
				else if(ran<0.37){gameBox.painter.allMapChat(chatStat.success,ClientMain.mainID,"全局喇叭测试(主角)");}
				else if(ran<0.50){gameBox.painter.allMapChat(chatStat.success,(int)(ran+0.5),"全局喇叭测试(其他人/系统)");}
				else if(ran<0.58){gameBox.painter.personChat(chatStat.frequency,0,ClientMain.mainID,"");}
				else if(ran<0.66){gameBox.painter.personChat(chatStat.blacklist,0,ClientMain.mainID,"");}
				else if(ran<0.75){gameBox.painter.personChat(chatStat.wrong,0,ClientMain.mainID,"");}
				else if(ran<0.86){gameBox.painter.personChat(chatStat.success,(int)(ran+100),ClientMain.mainID,"私聊测试(别人对你说)");}
				else {gameBox.painter.personChat(chatStat.success,ClientMain.mainID,(int)(ran+100),"私聊测试(你对别人说)");}
			}
		}
	}
	public static void mySleep (int time) {
		try {Thread.sleep(time);} catch (InterruptedException e) {}	
	}
}
