package uccu_client;

/* 实体对象的基类 */
public class Entity extends DoublePoint{
	/* 实体对象的修改应该是线程安全的 */
	public static enum style {airplane,item,bullet}; // 该实体的种类（如：飞机、道具、炮弹等）
	private style type;//该实体的类型
	private int ID; // 该实体的全服务器唯一标识
	private int picID; //该实体对应的贴图ID
	public double size; // 实体半径
	public double angle; //与x轴正向夹角，逆时针为正
	public double hp;
	public double maxhp;
	
	Entity(style t,int id,int picid,double x,double y,double size,double angle) {
		this.type=t;
		this.ID=id;
		this.picID=picid;
		this.posX=x;
		this.posY=y;
		this.size=size;
		this.angle=angle;
		this.hp = -1;
		this.maxhp = -1;
	}
	Entity(style t,int id,int picid,double x,double y,double size,double angle,double hp,double maxhp) {
		this.type=t;
		this.ID=id;
		this.picID=picid;
		this.posX=x;
		this.posY=y;
		this.size=size;
		this.angle=angle;
		this.hp = hp;
		this.maxhp = maxhp;
	}
	public int getID() {
		return ID;
	}
	public int getPicID() {
		return picID;
	}
	public style getType() {
		return type;
	}
}
