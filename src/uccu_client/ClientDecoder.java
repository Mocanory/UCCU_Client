/* 网络包解码器，定义了解码后应当由哪些模块处理该包 */
package uccu_client;

import java.nio.ByteBuffer;

import uccu_client.GameBox.chatStat;

public class ClientDecoder implements Decoder {
	private LoginBox loginBox;
	private GameBox gameBox;
	public ClientDecoder() {
		loginBox = ClientMain.loginBox;
		gameBox = ClientMain.gameBox;
	}
	@Override
	public void decode(ByteBuffer buffer, AioSession session) {
		while(true){
			ByteBuffer datagram = Datagram.getDatagram(buffer);
	        if(datagram == null)
	            return;
	        char sn = Datagram.trim(datagram);
	        switch (sn) {
			case 0x0001:{//回应连接请求
				byte stat=datagram.get();
				UccuLogger.log("ClientServer/ClientDecoder", "Receive a package 0001(回应连接请求)");
				UccuLogger.log("ClientServer/ClientDecoder", "package 0001: "+stat);
				loginBox.serverState(stat);
				break;
			}
			case 0x0003:{//case：登录反馈
				boolean res = datagram.get()==0x01?true:false;
				UccuLogger.log("ClientServer/ClientDecoder", "Receive a package 0003(登陆反馈)");
				UccuLogger.log("ClientServer/ClientDecoder", "package 0003: "+res);
				loginBox.onLoginResponse(res);
				break;
			}
			case 0x0005: {// case：注册反馈
				boolean res = datagram.getInt() == 0x01 ? true : false;
				UccuLogger.log("ClientServer/ClientDecoder", "Receive a package 0005(注册结果)");
				UccuLogger.log("ClientServer/ClientDecoder", "package 0005: "+res);
				loginBox.onRegistResponse(res);
				break;
			}
			case 0x0006:{//case：初始化选角色
				if(!datagram.hasRemaining()){
					UccuLogger.log("ClientServer/ClientDecoder", "Receive a package 0006(角色预加载——空包)");
					loginBox.noMorePackage();
				}
				else{
					int id=datagram.getInt();
					String name=Datagram.extractString(datagram);
					byte level = datagram.get();
					byte gender=datagram.get();
					UccuLogger.log("ClientServer/ClientDecoder", "Receive a package 0006(角色预加载)");
					UccuLogger.log("ClientServer/ClientDecoder", "package 0006: "+"/id: "+id+"/name: "+name+"/level: "+level+"/gender: "+gender);
					loginBox.addCharacter(id, name, level, gender);
				}
				break;
			}
			case 0x0009:{//角色创建结果
				boolean res = datagram.get() == 0x01 ? true : false;
				UccuLogger.log("ClientServer/ClientDecoder", "Receive a package 0009(角色创建结果)");
				UccuLogger.log("ClientServer/ClientDecoder", "package 0009: "+res);
				loginBox.onCreatResponse(res);
				break;
			}
			case 0x000A:{//角色详细信息
				int id=datagram.getInt();
				String name=Datagram.extractString(datagram);
				byte level=datagram.get();
				byte gender=datagram.get();
				byte pid = 12;	//picid 并没有告诉Client---多图情况下需要知道角色是哪款飞机
				int posX=datagram.getInt();
				int posY=datagram.getInt();
				UccuLogger.log("ClientServer/ClientDecoder", "Receive a package 000A(游戏中所有玩家信息)");
				UccuLogger.log("ClientServer/ClientDecoder", "package 000A: "+"/id: "+id+"/name: "+name+"/level: "+level+"/gender: "+gender+"/posX: "+posX+"/posY: "+posY);
				gameBox.addCharacter(id, name, pid, level, gender, posX, posY);
				break;
			}
			case 0x000C:{//角色发生移动
				int id=datagram.getInt();
				int targetX=datagram.getInt();
				int targetY=datagram.getInt();
				long globalTime= datagram.getLong();
				UccuLogger.log("ClientServer/ClientDecoder", "Receive a package 000C(所有玩家目标坐标更新)");
				UccuLogger.log("ClientServer/ClientDecoder", "package 000C: "+"/id: "+id+"/posX: "+targetX+"/posY: "+targetY);
				gameBox.updateTarget(id, targetX, targetY,globalTime);
				break;
			}
			case 0x000E:{	//全局喇叭被服务器拒绝
				int reason=datagram.getInt();
				chatStat s;
				switch (reason) {
				case 0:
					s=chatStat.frequency;
					break;
				case 1:
					s=chatStat.nopermission;
					break;
				default:
					s=chatStat.wrong;
				}
				gameBox.painter.allMapChat(s,0,"");
				UccuLogger.log("ClientServer/ClientDecoder", "Receive a package 000E(全局喇叭被服务器拒绝)");
				break;
			}
			case 0x000F:{	//全局喇叭信息
				int id = datagram.getInt();
				String msg= Datagram.extractString(datagram);
				gameBox.painter.allMapChat(chatStat.success,id,msg);
				UccuLogger.log("ClientServer/ClientDecoder", "Receive a package 000F(全局喇叭信息)");
				UccuLogger.log("ClientServer/ClientDecoder", "speakerID: "+id+"/message: "+msg);
				break;
			}
			case 0x0011:{	//私聊被服务器拒绝
				int reason=datagram.getInt();
				int recvid=datagram.getInt();
				chatStat s;
				switch (reason) {
				case 0:
					s=chatStat.frequency;
					break;
				case 1:
					s=chatStat.blacklist;
					break;
				default:
					s=chatStat.wrong;
				}
				gameBox.painter.personChat(s,0,recvid,"");
				UccuLogger.log("ClientServer/ClientDecoder", "Receive a package 000F(私聊被服务器拒绝)");
				UccuLogger.log("ClientServer/ClientDecoder", "recvidID: "+recvid);
				break;
			}
			case 0x0012:{	//私聊信息
				int sendid=datagram.getInt();
				int recvid=datagram.getInt();
				String msg= Datagram.extractString(datagram);
				gameBox.painter.personChat(chatStat.success, sendid, recvid, msg);
				UccuLogger.log("ClientServer/ClientDecoder", "Receive a package 000F(私聊信息)");
				UccuLogger.log("ClientServer/ClientDecoder", "sendID: "+sendid+"/recvidID: "+recvid+"/message: "+msg);
				break;
			}
			case 0x0014:{ //PING请求包
				long timestamp = datagram.getLong();
				UccuLogger.log("ClientServer/ClientDecoder", "Receive a package 0014(PING)");
				SendingModule.sendPINGResponse(timestamp);	//立即回应
				break;
			}
			case 0x0016:{ //时间同步
				GameBox.globalTime = datagram.getLong();
				UccuLogger.log("ClientServer/ClientDecoder", "Receive a package 0016(时间同步)");
				break;
			}
			case 0x0019:{ //背包信息
				int size= datagram.getInt();
				int instanceID,data,ID,num;
				for(int i=0;i<size;++i){
					instanceID = datagram.getInt();
					data = datagram.getInt();
					num = data&0x7f;
					ID = data>>>7;
					gameBox.mainrole.add_items(ID, num, instanceID);
				}
				UccuLogger.log("ClientServer/ClientDecoder", "Receive a package 0019(背包信息)");
			}
			case 0x001A:{ //技能信息
				int size= datagram.getInt();
				int instanceID,data,ID,level,exp;
				for(int i=0;i<size;++i){
					instanceID = datagram.getInt();
					ID = datagram.getInt();
					data = datagram.getInt();
					level = data&0x7f;
					exp = data>>>7;
					gameBox.mainrole.add_skills(ID, instanceID, level, exp);
				}
				UccuLogger.log("ClientServer/ClientDecoder", "Receive a package 001A(技能信息)");
			}
			case 0x001B:{ //冷却信息
				int size = datagram.getInt();
				int ID,rest;
				for(int i=0;i<size;++i){
					ID=datagram.getInt();
					rest=datagram.getInt();
					if(ID>=1000000000){
//						这是技能
					}
					else{
//						这是物品
					}
				}
			}
			case 0x001C:{//角色详细信息的重新更新
				int id=datagram.getInt();
				String name=Datagram.extractString(datagram);
				byte level=datagram.get();
				byte gender=datagram.get();
				byte pid = 12;	//picid 并没有告诉Client---多图情况下需要知道角色是哪款飞机
				int posX=datagram.getInt();
				int posY=datagram.getInt();
				UccuLogger.log("ClientServer/ClientDecoder", "Receive a package 000A(游戏中所有玩家信息)");
				UccuLogger.log("ClientServer/ClientDecoder", "package 000A: "+"/id: "+id+"/name: "+name+"/level: "+level+"/gender: "+gender+"/posX: "+posX+"/posY: "+posY);
				gameBox.addCharacter(id, name, pid, level, gender, posX, posY);
				break;
			}
			
	    	}
		}
	}
}
