package uccu_client;

public class PainterProperty {
	static PicProperty[] picp;
	static GIFPicProperty[] gifp;
}
class PicProperty{
	public PicProperty(String path,int p_w,int p_h,int id){
		this.path = path;
		this.p_h = p_h;
		this.p_w = p_w;
		this.id = id;
	}
	String path;
	int p_w;
	int p_h;
	int id;
}
class GIFPicProperty extends PicProperty{
	public GIFPicProperty(String path,int p_w,int p_h,int id,	int dt,int iw,int ih){
		super(path, p_w, p_h, id);
		this.dt = dt;
		this.iw = iw;
		this.ih = ih;
	}
	int dt;
	int iw;
	int ih;
}
