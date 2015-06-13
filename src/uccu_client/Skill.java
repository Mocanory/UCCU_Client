package uccu_client;
public class Skill{
	public int gifID;
	public int picID;
	public int instanceID;
	public long coldtime; 
	public long lastuse;
	public int level;	//技能等级
	public int exp;		//技能经验
	public boolean empty; //该位置是否占用
	public String name;
	public String describtion;
	public Skill() {
		empty=true;
	}
}