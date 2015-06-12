package uccu_client;

public class ClientReaper implements Reaper {

	public ClientReaper() {
		// TODO 自动生成的构造函数存根
	}
	@Override
	public void reap(AioSession session) {
	        System.out.println("Session " + session.getRemoteSocketAddress() + " has disconnected!");
		// TODO 自动生成的方法存根
	}

}
