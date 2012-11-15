import javax.swing.JPanel;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;


public class DrawPanel extends JPanel implements MouseMotionListener{
	private ArrayList<Harbor> harborList;
	private int[][] harborMap;
	private final Color BG_COLOR = new Color(150,180,255);
	private final Color HARBOR_COLOR = new Color(230,220,180);
	private final int HARBOR_WIDTH = 10;
	private final int HARBOR_HEIGHT = 10;
	public DrawPanel(){
		this.harborList = WorldMap.harborList;
		updateHarborMap();
	}

	public void updateHarborMap(){
		harborMap = new int[WorldMap.HARBOR_XBOUND][WorldMap.HARBOR_YBOUND];
		for (Harbor hb : harborList){
			Point pt = hb.getLoc();
			harborMap[pt.x][pt.y] = 1;
		}
	}

	public void paint(Graphics g){
		//Paint background.
		g.setColor(BG_COLOR);
		g.fillRect(0, 0, getWidth(), getHeight());

		//Paint harbor symbols.
		g.setColor(HARBOR_COLOR);
		for (Harbor hb : harborList){
			Point pt = hb.getLoc();
			g.fillRect(pt.x * HARBOR_WIDTH, pt.y * HARBOR_WIDTH,
				HARBOR_WIDTH, HARBOR_HEIGHT);
		}
	}

//---LISTENER METHODS:---

	public void mouseDragged(MouseEvent me){}
	public void mouseMoved(MouseEvent me){
		//TODO Things.
	}
}
