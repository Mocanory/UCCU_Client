package uccu_client;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

public class Airplane extends Entity {
	public String name;
	public String describe;
	public byte level;
	public byte gender;
	public double targetX;
	public double targetY;
	public double speed;
	public int deltaTime;
	public int life;
	public int curlife;
	public int mana;
	public int curmana;
	public int atk;
	public int def;
	public int exp;
	public Airplane(int id, int picid, double x, double y,byte l,byte g,int speed,
			String name, String describe,int life,int curlife,int mana,int curmana,
			int atk,int def, int exp) {
		super(Entity.style.airplane, id, picid, x, y, 10.0, 0.0);
		this.name=name;
		this.describe=describe;
		this.level=l;
		this.gender=g;
		this.speed=speed;
		this.life= life;
		this.curlife=life;
		this.mana=mana;
		this.curmana=curmana;
		this.atk=atk;
		this.def=def;
		this.exp=exp;
		deltaTime = 0;
		// TODO 最后两个参数size和angle先传固定值，以后再加
	}
	public Warhead attack(Airplane target){
		Warhead tmp = new Warhead(this.posX, this.posY, target);
		return tmp;
	}
}