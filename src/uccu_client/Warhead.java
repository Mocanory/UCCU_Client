package uccu_client;

public class Warhead extends Entity {
	Airplane targetAirplane;
	double speed;
	public Warhead(double x, double y, Airplane target) {
		super(Entity.style.bullet,0,0, x, y, 5, 0);
		targetAirplane=target;
		speed = 5;
	}
}
