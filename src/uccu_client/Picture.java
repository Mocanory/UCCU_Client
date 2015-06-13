package uccu_client;

import java.awt.*;
import java.awt.image.ImageObserver;

public class Picture{
    Image img;
    int width;
    int height;
	int id;
    public Picture(String filename, int p_w, int p_h,int ID){
        img = Toolkit.getDefaultToolkit().getImage(filename);
        
        width = p_w;
        height = p_h;
        id = ID;
    }
    public Picture(PicProperty pp){
    	img = Toolkit.getDefaultToolkit().getImage(pp.path);
    	width = pp.p_w;
    	height = pp.p_h;
    	id = pp.id;
    }

    public int getID(){
    	return id;
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public Image getImage(){
        return img;
    }
}
class GIFpicture extends Picture{
	int delta;
	int imgheight;
	int imgwidth;
	public GIFpicture(String filename, int p_w, int p_h,int ID,int dt,int iw,int ih) {
		super(filename, p_w, p_h, ID);
		delta = dt;
		imgheight = ih;
		imgwidth = iw;
	}
	public GIFpicture(GIFPicProperty gpp) {
		super(gpp.path, gpp.p_w, gpp.p_h, gpp.id);
		delta = gpp.dt;
		imgheight = gpp.ih;
		imgwidth = gpp.iw;
	}
	public int getDelta(){
		return delta;
	}
	public int getImgHeight(){
		return imgheight;
	}
	public int getImgWidth(){
		return imgwidth;
	}
}
class GIF{
	GIFpicture pic;
	int count;
	int turncount;
	int maxturn;
	DoublePoint pos;
	public GIF(GIFpicture p,DoublePoint pos){
		this.pos = pos;
		pic = p;
		maxturn = turncount = count = 0;
	}
	public GIF(GIFpicture p,DoublePoint pos,int mt){
		this.pos = pos;
		pic = p;
		maxturn = mt;
		turncount = count = 0;
	}
	public boolean paint(Graphics2D g,double mx,double my,ImageObserver obsv){
		int delta = pic.getDelta();
		int x = (int)(pos.posX - mx) + Painter.width/2 - pic.getWidth()/2;
		int y = (int)(pos.posY - my) + Painter.height/2 - pic.getHeight()/2;
		g.drawImage(pic.getImage(), x, y, x+pic.getWidth(), y+pic.getHeight(), delta*count,0, delta*(count+1), pic.getImgHeight(), obsv);
		++count;
		if(count*delta >= pic.getImgWidth()){
			count = 0;
			++turncount;
		}
		return maxturn <= 0 || turncount < maxturn;
	}
}