package uccu_client;

public class Airplane extends Entity {
	public String name;
	public byte level;
	public byte gender;
	public double targetX;
	public double targetY;
	public double speed;
	public Airplane(int id, int picid, double x, double y,String n,byte l,byte g) {
		super(Entity.style.airplane, id, picid, x, y, 10.0, 0.0);
		this.name=n;
		this.level=l;
		this.gender=g;
		this.speed=5;	//初始速度先设为1
		// TODO 最后两个参数size和angle先传固定值，以后再加
	}
	public Warhead attack(Airplane target){
		Warhead tmp = new Warhead(this.posX, this.posY, target);
		return tmp;
	}
}