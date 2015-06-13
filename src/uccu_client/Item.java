package uccu_client;
public class Item{
	public Item() {
		empty=true;
	}
	public Item(int picid,String n,String d){//for static pre_items
		picID=picid;
		name= n;
		describtion=d;	
	}
	public int instanceID;
	public int picID;
	public int num;
	public long coldtime;
	public long lastuse;
	public boolean empty;//标记该物品栏是否为空
	public String name;
	public String describtion;
}