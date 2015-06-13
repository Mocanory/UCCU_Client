/* 游戏逻辑核心 */
package uccu_client;

import java.awt.List;
import java.awt.geom.Area;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class GameBox{
	//实体池，应为一个HashMap，以ID为索引，实体对象为元素
	public HashMap<Integer, Airplane> playerPool;
	//炮弹池，为一个List
	public ArrayList<Warhead> warheadPool;
	//本人信息，应该比其他玩家的信息更丰富
	public Mainrole mainrole;
	//服务器绝对时间
	static public long globalTime;
	//更改playerPool和warheadPool的锁
	private static Object lock_plane = new Object(); // static确保只有一把锁
	private static Object lock_bullet = new Object(); // static确保只有一把锁
	//系统聊天信息
	public static enum chatStat{success,frequency,nopermission,blacklist,wrong};
	Painter painter;
	public GameBox(){
		playerPool = new HashMap<Integer, Airplane>();
		warheadPool = new ArrayList<Warhead>();
		painter= new Painter(this);
	}
	//开始游戏
	public void startGame(){
		boolean roleSending=true;
		int i=0;
		//打开加载等待窗口，后台开始等待服务器详细角色信息的发送
		painter.waitGameInit();
		while(roleSending){
			if(i>100){
				UccuLogger.kernel("ClientServer/GameBox/startGame", "接收角色详细信息(000A)超时!(未接收到主角信息)");
				ClientMain.isGameOver=true;
			}
			if(mainrole!=null)
				roleSending=false;			
			painter.setInitStage(i++/100.0);
			ClientMain.mySleep(200);
		}
		//加载完成 等待窗口关闭	正式开始游戏
		UccuLogger.kernel("ClientServer/GameBox/startGame", "接收主角详细信息(000A):关闭加载窗口,开始游戏!");
		painter.gameStart();
		new Thread(new ActionThread_plane(this)).start();
		new Thread(new ActionThread_bullet(this)).start();
	}
	//由decoder调用,更新各个角色的目标位置
	public void updateTarget(int id, int targetX, int targetY,long globalTime) {
		synchronized (lock_plane) {
			(playerPool.get(id)).targetX = targetX;
			(playerPool.get(id)).targetY = targetY;
			(playerPool.get(id)).deltaTime=(int)(System.currentTimeMillis()-globalTime);
			UccuLogger.log("Client/GameBox/updateTarget",
					"receive Package 000C(角色目标)");
			UccuLogger.log("Client/GameBox/updateTarget", "targetX: " + targetX
					+ "/targetY " + targetY);
		}
	}
	public void sendTargetPos(int targetX,int targetY){
		updateTarget(ClientMain.mainID,targetX, targetY,System.currentTimeMillis());//先更新目标地址，再发送数据包
		SendingModule.sendMovingTarget(mainrole.getID(), (int)mainrole.targetX, (int)mainrole.targetY);
		}
	//将角色初始化,加入角色池并放入贴图池
	public void addCharacter(int id,String name,byte pid,byte level,byte gender,int posX,int posY){
		UccuLogger.debug("ClientServer/GameBox/addCharacter", "Receive a package 000A(游戏中所有玩家信息)");
		if(id!=ClientMain.mainID){	//非主角玩家
			Airplane tmpPlane= new Airplane(id, pid, posX, posY,name,level,gender);
			playerPool.put(id,tmpPlane);
			painter.addEntity(tmpPlane);
			UccuLogger.debug("ClientServer/GameBox/addCharacter", "000A:加入一个非主角玩家:"+name);
		}
		else{	//主角玩家
			Mainrole tmpMain=new Mainrole(id, pid, posX, posY,name,level,gender);
			playerPool.put(id,tmpMain);
			painter.addEntity(tmpMain);
			painter.setMainRole(tmpMain);
			mainrole=tmpMain;
			mainrole.deltaTime = 50;
			for(int i=0;i<32;++i)
				mainrole.add_items(0, 5,0);
			UccuLogger.debug("ClientServer/GameBox/addCharacter", "000A:加入一个主角玩家:"+name);
		}
	}	
	//攻击函数,由当前游戏窗体攻击事件触发
	public void attack(int id){
		synchronized (lock_bullet) {
			Warhead warhead=mainrole.attack((playerPool.get(id)));
			warheadPool.add(warhead);
			painter.addEntity(warhead);		
		}
	}
	//Action_plane线程专门负责处理每20ms之后所有飞机的位置
	private class ActionThread_plane implements Runnable{
		GameBox gameBox;
		public ActionThread_plane(GameBox gb) {
			gameBox = gb;	//获取指示变量的引用
		}
		int sleeptime = 20;
		@Override
		public void run() {
			while(true){
				ClientMain.mySleep(sleeptime);
				synchronized (lock_plane) {
					Iterator<?> iter = gameBox.playerPool.keySet().iterator();
					Airplane plane;
					double deltaX, deltaY;
					while (iter.hasNext()) {
						plane = gameBox.playerPool.get(iter.next());
						deltaX = plane.targetX - plane.posX;
						deltaY = plane.targetY - plane.posY;
						int face = deltaX > 0 ? 0 : 1;
						if (deltaX != 0)
							plane.angle = Math.PI / 2 + Math.PI * face+ (Math.atan(deltaY / deltaX));
						/* 我修改了你的移动方法 不然deltaX很小或者deltaY是负数时会出错
						 * 这里采用总速度不变，横纵速度按比例变化的方法 最后如果delta比速度的步长短时，只移动delta*/
						double dL = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
						if(dL == 0) continue;
						double dt = plane.deltaTime;
						double modspeed = plane.speed;
						modspeed = (1+dt/(sleeptime*dL/modspeed - dt))*modspeed;
						double tmp = modspeed / dL;
						if (tmp > 1)
							tmp = 1;// 比例大于1说明步长大于delta，将比例修改为1，否则会不能停止移动，反复在原地抖动
						// speed 是其x轴速度,deltaXY只是用来算角度的
						plane.posX += deltaX * tmp;
						plane.posY += deltaY * tmp;
					}
				}
			}
		}		
	}
	private class ActionThread_bullet implements Runnable{
		GameBox gameBox;
		public ActionThread_bullet(GameBox gb) {
			gameBox = gb;	//获取指示变量的引用
		}
		@Override
		public void run() {
			while(true){
				ClientMain.mySleep(20);
				synchronized (lock_bullet) {
					Iterator<?> iter = gameBox.warheadPool.iterator();
					Warhead warhead;
					Airplane targetPlane;
					double deltaX, deltaY;
					while (iter.hasNext()) {
						warhead = (Warhead) iter.next();
						targetPlane=warhead.targetAirplane;
						deltaX = targetPlane.posX-warhead.posX;
						deltaY = targetPlane.posY-warhead.posY;
						if(deltaX==0 && deltaY==0){
							painter.deleteEntity(warhead);
							iter.remove();
							continue;
						}
						int face = deltaX > 0 ? 0 : 1;
						if (deltaX != 0)
							warhead.angle = Math.PI / 2 + Math.PI * face+ (Math.atan(deltaY / deltaX));
						/* 我修改了你的移动方法 不然deltaX很小或者deltaY是负数时会出错
						 * 这里采用总速度不变，横纵速度按比例变化的方法 最后如果delta比速度的步长短时，只移动delta*/
						double tmp = warhead.speed/ Math.sqrt(deltaX * deltaX + deltaY * deltaY);
						if (tmp > 1)
							tmp = 1;// 比例大于1说明步长大于delta，将比例修改为1，否则会不能停止移动，反复在原地抖动
							// speed 是其x轴速度,deltaXY只是用来算角度的
						warhead.posX += deltaX * tmp;
						warhead.posY += deltaY * tmp;
						
					}
				}
			}
		}
	}
}
