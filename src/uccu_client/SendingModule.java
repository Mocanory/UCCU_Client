package uccu_client;

import java.nio.ByteBuffer;

public class SendingModule {
	public static void send00Package() {
		ByteBuffer bb = ByteBuffer.allocate(256);
		bb.putInt(12345);
		ClientMain.serverSession.write(Datagram.wrap(bb, Target.CL_Gate, 0x0000));
		UccuLogger.log("SendingModule/send00Package", "send00Package over");
	}
	public static void sendRegister(String id,String pwd){
		ByteBuffer bb = ByteBuffer.allocate(256);
		Datagram.restoreString(bb, id);
		Datagram.restoreString(bb, pwd);
		ClientMain.serverSession.write(Datagram.wrap(bb,Target.CL_Gate,0x0004));
		UccuLogger.log("SendingModule/sendRegister", "package 0004(注册信息) send!");
	}
	public static void sendLogin(String id,String pwd){
		pwd = BasicLib.md5(pwd);
		ByteBuffer bb = ByteBuffer.allocate(256);
		Datagram.restoreString(bb, id);
		Datagram.restoreString(bb, pwd);
		ClientMain.serverSession.write(Datagram.wrap(bb,Target.CL_Gate,0x0002));
		UccuLogger.log("SendingModule/sendLogin", "package 0002(登陆信息) send!");
	}
	public static void sendCreateCharacter(String name,byte gender){
		ByteBuffer bb = ByteBuffer.allocate(256);
		Datagram.restoreString(bb,ClientMain.userID);//user id
		Datagram.restoreString(bb,name);
		bb.put(gender);
		ClientMain.serverSession.write(Datagram.wrap(bb,Target.CL_Gate,0x0008));
		UccuLogger.log("SendingModule/sendCreateCharacter","package 0008(创建角色) send!");
	}
	public static void sendCharacter(int cid){
		ByteBuffer bb = ByteBuffer.allocate(256);
    	bb.putInt(cid);
		ClientMain.serverSession.write(Datagram.wrap(bb,Target.CL_Gate,0x0007));
		UccuLogger.log("SendingModule/sendCharacter", "package 0007(角色选择) send!");
	}
	public static void sendMovingTarget(int ID,int X,int Y){
		ByteBuffer bb = ByteBuffer.allocate(256);
		bb.putInt(ID);
		bb.putInt(X);
		bb.putInt(Y);
		ClientMain.serverSession.write(Datagram.wrap(bb,Target.CL_Gate,0x000B));
		UccuLogger.debug("Client/GameBox/sendTargetPos", "package 000B(角色移动意图) send!");
		UccuLogger.debug("Client/GameBox/sendTargetPos", "targetX: "+X+"/targetY: "+Y);

	}
	public static void sendPubChat(String t){
		ByteBuffer bb = ByteBuffer.allocate(256);
    	bb.putInt(ClientMain.gameBox.mainrole.getID());
		Datagram.restoreString(bb, t);
		ClientMain.serverSession.write(Datagram.wrap(bb,Target.CL_Gate,0x000D));
		UccuLogger.debug("SendingModule/sendPubChat", t);
	}
	public static void sendPriChat(int id,String t){
		//TODO
		UccuLogger.debug("SendingModule/sendPriChat", id + ":" + t);
	}
	public static void sendLogout(){
		//TODO
		UccuLogger.log("SendingModule/sendLogout", "logout send!");
	}
}
