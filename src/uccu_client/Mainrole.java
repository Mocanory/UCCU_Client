package uccu_client;

public class Mainrole extends Airplane {
	static public item[] pre_items;
	static public skill[] pre_skills;
	public item[] items= new item[32];//物品数组
	public skill[] skills = new skill[32];//技能数组
	public Mainrole(int id, int picid, double x, double y,String name,byte level,byte gender) {
		super(id, picid, x, y,name,level,gender);
	}
	public void add_items (int ID,int num) {
		int i=0;
		for(;i<items.length;++i)
			if(items[i].empty||items[i].num==0)	break;
		items[i].empty=false;
		items[i].picID=pre_items[ID].picID;
		items[i].num=num;
		items[i].name=pre_items[ID].name;
		items[i].describtion=pre_items[ID].describtion;		
	}
}
class item{
	public item() {
		empty=true;
	}
	public item(int ID,String n,String d){//for static pre_items
		picID=ID;
		name= n;
		describtion=d;			
	}
	int picID;
	int num;
	boolean empty;//标记该物品栏是否为空
	String name;
	String describtion;

}
class skill{
	int gifID;
	String name;
	String describtion;
}