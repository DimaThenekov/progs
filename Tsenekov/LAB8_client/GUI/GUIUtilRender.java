package GUI;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.stream.Stream;

public class GUIUtilRender extends JFrame {
	private boolean presed = false;
	private int lastPosX=0, lastPosY=0;
	private int drawPosX=0, drawPosY=0;
	private int graphicsHeight=0, graphicsWidth=0;
	
	private int selected = -1;
	private Object[][] newds = {{300, 300, 30, "nameUser", 1}, {-300, 300, 50, "nameUser2", 2}, {0, 0, 100, "nameUser2", 3}};
	private long lastTime = System.currentTimeMillis();
	private Object[][] ds = {{300, 300, 30, "nameUser", 1}, {-300, 300, 50, "nameUser2", 2}, {0, 0, 100, "nameUser2", 3}};
	private final float[][] colors = {{1f,0f,0f},{1f,0.2f,0f},{1f,0.4f,0f},{1f,0.6f,0f},{1f,0.8f,0f},{1f,1f,0f},{0.799f,1f,0f},{0.599f,1f,0f},{0.399f,1f,0f},{0.199f,1f,0f},{0f,1f,0f},{0f,1f,0.199f},{0f,1f,0.4f},{0f,1f,0.6f},{0f,1f,0.8f},{0f,1f,1f},{0f,0.8f,1f},{0f,0.6f,1f},{0f,0.4f,1f},{0f,0.2f,1f},{0f,0f,1f},{0.199f,0f,1f},{0.399f,0f,1f},{0.6f,0f,1f},{0.8f,0f,1f},{1f,0f,1f},{1f,0f,0.8f},{1f,0f,0.6f},{1f,0f,0.4f},{1f,0f,0.2f}};
	
	public int getSelected() {
		for (var i=0;i<newds.length;i++)
			if (selected==(Integer)newds[i][4])
				return selected;
		return -1;
	}
	
	public void setDs(Object[][] ds) {
		this.ds=newds;
		newds = ds;
		lastTime = System.currentTimeMillis();
	}
	
	public void startMove(int x, int y) {
		presed = true;
		lastPosX = x;
		lastPosY = y;
	}

	public void move(int x, int y) {
		if (presed) {
			drawPosX -= (lastPosX-x);
			drawPosY -= (lastPosY-y);
			lastPosX = x;
			lastPosY = y;
		}
	}

	public void endMove(int x, int y) {
		if (presed){
			move(x, y);
			presed = false;
			
			for (var i=0;i<newds.length;i++)
				if ((x-transX((Integer)newds[i][0]))*(x-transX((Integer)newds[i][0]))+(y-transY((Integer)newds[i][1]))*(y-transY((Integer)newds[i][1]))<=transSize((Integer)newds[i][2]/2)*transSize((Integer)newds[i][2]/2))
					selected = (selected==(Integer)newds[i][4]?-1:(Integer)newds[i][4]);
		}
	}

	public boolean render(Graphics g, int h, int w) {
		graphicsHeight=h;
		graphicsWidth=w;
		g.setColor(new Color(1,1,1,1));
		g.fillRect(transX(0)-2, 0, 4, graphicsHeight);
		g.fillRect(0, transY(0)-2, graphicsWidth, 4);
		for (var i=-30;i<=30;i++) {
			g.setColor(new Color(1,1,1, 1-Math.abs(((float)i)/31)));
			g.drawString(String.valueOf(i*100), transX(i*100+5), transY(5));
			g.fillRect(transX(i*100)-1, 0, 1, graphicsHeight);
			g.fillRect(transX(i*100)-2, transY(5), 4, 10);
			
			g.drawString(String.valueOf(i*100), transX(5), transY(i*100+5));
			g.fillRect(0, transY(i*100)-1, graphicsWidth, 1);
			g.fillRect(transX(-5), transY(i*100)-2, 10, 4);
		}
		
		
		int xPoly[] = {40,31,22,13,4,-4,-12,-20,-28,-36,-43,-50,-59,-69,-79,-89,-97,-104,-107,-98,-88,-79,-76,-82,-89,-96,-105,-114,-120,-126,-136,-145,-154,-161,-165,-165,-160,-151,-142,-134,-127,-118,-108,-98,-88,-78,-68,-58,-48,-40,-33,-30,-26,-19,-13,-9,-7,-8,-9,-3,2,-2,-8,1,11,20,28,37,42,40,33,24,15,5,-5,-14,-24,-34,-44,-54,-64,-74,-84,-94,-104,-114,-117,-109,-101,-95,-104,-113,-122,-130,-138,-146,-154,-161,-167,-173,-178,-183,-186,-189,-192,-192,-193,-192,-192,-190,-187,-185,-183,-177,-172,-168,-165,-164,-164,-165,-167,-169,-172,-175,-177,-172,-166,-160,-154,-149,-144,-141,-138,-130,-122,-113,-105,-97,-87,-78,-76,-84,-84,-75,-66,-57,-49,-40,-32,-24,-17,-9,-2,5,11,17,20,17,13,9,5,0,-5,-11,-17,-23,-30,-30,-21,-11,-3,5,13,21,29,38,47,56,65,75,85,93,97,87,77,67,58,49,40,42,52,61,70,63,53,44,40,46,55,65,75,84,94,103,112,120,127,132,137,143,147,152,155,158,159,158,156,153,149,146,153,160,167,174,181,186,191,194,195,195,193,190,185,179,173,166,158,151,143,134,126,117,109,100,91,82,73,64,54,45,36,26,17,16,25,33,42,50,58,66,72,69,60,50,40,30,30,40,46,36,26,17,9,2,-6,-14,-24,-22,-14,-11,-12,-16,-26,-35,-45,-55,-65,-75,-85,-95,-90,-81,-71,-62,-55,-65,-75,-85,-95,-104,-111,-118,-123,-127,-129,-123,-116,-109,-101,-93,-95,-96,-94,-91,-87,-81,-74,-66,-57,-47,-37,-41,-49,-45,-35,-25,-16,-6,3,11,20,29,37,45,54,62,70,77,85,92,96,98,99,99,98,98,96,99,106,112,117,121,122,123,122,121,119,116,113,109,105,101,96,91,90,100,109,117,125,133,140,147,154,160,156,151,146,141,135,129,123,116,108,105,114,124,134,144,154,147,138,128,118,108,98,88,88,97,107,116,126,135,145,155,164,174,172,162,152,142,132,122,112,102,92,82,72,62,52,42,43,38,28,18,9,-1,-4,4,13,14,8,14,21,28,35,42,45};
		int yPoly[] = {-246,-243,-238,-234,-229,-224,-218,-212,-206,-199,-192,-185,-185,-187,-187,-185,-180,-173,-166,-170,-173,-172,-164,-156,-149,-142,-137,-133,-126,-118,-114,-111,-107,-100,-91,-81,-73,-68,-70,-77,-83,-87,-89,-90,-91,-91,-91,-92,-95,-100,-107,-116,-116,-109,-101,-91,-99,-109,-119,-117,-115,-124,-132,-129,-125,-121,-116,-110,-102,-93,-86,-82,-78,-75,-73,-70,-68,-67,-65,-64,-63,-62,-62,-61,-62,-62,-61,-55,-50,-44,-40,-35,-31,-25,-19,-13,-6,1,8,16,25,34,43,53,62,72,82,92,102,112,122,117,107,100,109,118,128,138,148,158,167,177,187,196,206,201,193,185,176,168,159,150,140,144,150,156,162,167,171,172,170,163,159,163,168,173,178,184,189,196,202,209,216,223,231,239,239,230,221,211,202,194,185,177,169,161,154,148,145,142,143,149,155,161,167,172,177,181,184,185,183,177,170,172,172,171,167,163,158,154,153,149,143,143,142,137,128,120,116,114,115,117,119,123,128,134,141,150,159,158,149,157,166,176,186,196,205,215,224,234,231,224,217,210,202,194,185,176,166,156,146,137,128,120,112,105,98,92,86,80,75,70,65,60,56,51,47,43,39,36,32,29,26,27,32,37,42,48,54,60,68,72,70,69,68,68,70,72,78,80,82,85,81,74,68,63,62,66,72,81,91,99,103,106,108,110,111,112,112,111,108,107,104,100,96,96,96,95,92,87,81,74,65,56,46,53,60,67,73,76,66,56,46,37,28,19,12,7,4,3,3,9,15,16,15,12,9,5,1,-4,-9,-14,-20,-25,-31,-37,-43,-49,-54,-47,-38,-28,-18,-8,2,12,22,25,18,10,1,-8,-18,-28,-38,-48,-58,-67,-77,-86,-95,-104,-113,-122,-129,-126,-121,-116,-110,-103,-97,-90,-83,-79,-88,-97,-105,-114,-122,-130,-138,-145,-151,-156,-160,-162,-163,-164,-164,-167,-169,-170,-171,-171,-171,-171,-173,-176,-180,-183,-186,-189,-192,-194,-197,-198,-200,-200,-200,-200,-199,-199,-199,-198,-198,-197,-195,-194,-192,-190,-193,-194,-192,-190,-188,-186,-188,-195,-198,-202,-206,-214,-221,-229,-236,-243,-248};
		var ids = getIds();
		for (var i=0;i<ids.length;i++) {
			Object[] dif = difByID(ids[i]);
			float[] dcolor = colors[((String)dif[3]).hashCode()%colors.length];
			if (ids[i]==selected)
				g.setColor(new Color(dcolor[0],dcolor[1],dcolor[2],(float)1));
			else
				g.setColor(new Color(dcolor[0]/2,dcolor[1]/2,dcolor[2]/2,(float)1));
			drawPolygon(g, xPoly, yPoly, (Integer)dif[0], (Integer)dif[1], (Integer)dif[2]);
		}
		return (System.currentTimeMillis()-lastTime)<=1000;
	}

	private int transX(int x) {
		return drawPosX+x+(graphicsWidth/2);
	}

	private Integer[] getIds() {
		return Stream.concat(Arrays.stream(newds),Arrays.stream(ds)).map(x->(Integer)x[4]).distinct().toArray(Integer[]::new);
	}
	
	private Object[] difByID(int x) {
		int lastInd = -1;
		for (var i = 0;i < ds.length; i++)
			if ((Integer)ds[i][4]==x)
				lastInd = i;
		int newInd = -1;
		for (var i = 0;i < newds.length; i++)
			if ((Integer)newds[i][4]==x)
				newInd = i;
		if (lastInd == -1) // создаеться
			return new Object[]{(Integer)newds[newInd][0], (Integer)newds[newInd][1], mix2(0, transSize((Integer)newds[newInd][2])), (String)newds[newInd][3]};
		if (newInd == -1) // удаляется 
			return new Object[]{(Integer)ds[lastInd][0], (Integer)ds[lastInd][1], mix2(transSize((Integer)ds[lastInd][2]), 0), (String)ds[lastInd][3]};
		
		return new Object[]{mix((Integer)ds[lastInd][0], (Integer)newds[newInd][0]), mix((Integer)ds[lastInd][1], (Integer)newds[newInd][1]), mix2(transSize((Integer)ds[lastInd][2]), transSize((Integer)newds[newInd][2])), (String)newds[newInd][3]};
	}

	private int transY(int y) {
		return drawPosY-y+(graphicsHeight/2);
	}

	private void drawPolygon(Graphics g, int[] xs,int[] ys, int posX, int posY, int size) {
		g.fillPolygon(Arrays.stream(xs).map(x->transX(x*size/512+posX)).toArray(), Arrays.stream(ys).map(y->transY(-y*size/512+posY)).toArray(), xs.length);
	}

	private int transSize(int size) {
		return (int)(250-20000/((float)size+100));
	}

	private int mix(int x, int y) {
		var t = animFunction(System.currentTimeMillis()-lastTime);
		return (x*(1000-t)+y*t)/1000;
	}

	private int animFunction(long x) {
		return  (int)(x>1000?1000:(x<0?0:1000+2701*(x-1000)*(x-1000)*(x-1000)/1000/1000/1000+1701*(x-1000)*(x-1000)/1000/1000));
	}

	private int mix2(int x, int y) {
		var t = animFunction2(System.currentTimeMillis()-lastTime);
		return (x*(1000-t)+y*t)/1000;
	}
	
	private int animFunction2(long x) {
		return  (int)(x>1000?1000:(x<0?0:-x*(x-2000)/1000));
	}
}