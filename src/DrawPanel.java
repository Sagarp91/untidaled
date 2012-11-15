import javax.swing.JPanel;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;


public class DrawPanel extends JPanel implements MouseListener, MouseMotionListener{
	private ArrayList<Harbor> harborList;
	private int[][] harborMap;
	private final Color BG_COLOR = new Color(150,180,255);
	private final Color HARBOR_COLOR = new Color(250,255,180);
	private int harborWidth = 1;
	private int harborHeight = 1;

	public DrawPanel(){
		this.harborList = WorldMap.harborList;
		addMouseMotionListener(this);
		updateHarborMap();
	}

	public void updateHarborMap(){
		harborMap = new int[WorldMap.HARBOR_XBOUND][WorldMap.HARBOR_YBOUND];

		//Initialize each element as 0.
		for (int x = 0; x < WorldMap.HARBOR_XBOUND; ++x){
				for (int y = 0; y < WorldMap.HARBOR_YBOUND; ++y){
						harborMap[x][y] = 0;
				}
		}

		for (Harbor hb : harborList){
			Point pt = hb.getLoc();
			harborMap[pt.x][pt.y] = 1;
		}
	}

	public void paint(Graphics g){
		harborWidth = this.getWidth() / WorldMap.HARBOR_XBOUND;
		harborHeight = this.getHeight() / WorldMap.HARBOR_YBOUND;

		//Paint background.
		g.setColor(BG_COLOR);
		g.fillRect(0, 0, getWidth(), getHeight());

		//Paint harbor symbols.
		g.setColor(HARBOR_COLOR);
		for (Harbor hb : harborList){
			Point pt = hb.getLoc();
			g.fillRect(pt.x * harborWidth, pt.y * harborHeight,
				harborWidth, harborHeight);
		}
	}

//---LISTENER METHODS:---

	public void mouseDragged(MouseEvent me){}
	public void mouseMoved(MouseEvent me){
		Point pt = new Point(me.getPoint().x, me.getPoint().y);
		int x = pt.x / harborWidth;
		int y = pt.y / harborHeight;

		try{
			if (harborMap[x][y] == 1){
				pt = new Point(x, y);
				for (Harbor hb : harborList){
					if (hb.getLoc().equals(pt)){
						InfoPanel.harborName = hb.getName();
						InfoPanel.countryName = hb.getCountry();
						break;
					}
				}
			} else{
				InfoPanel.harborName = "";
				InfoPanel.countryName = "";
			}
		} catch (Exception e){
			//There is a known bug here, where, because of the fact that the
			//frame is surrounded by a border, its size is shortened by a few
			//pixels on each side. Faced with the decision to either set
			//Undecorated to true or surround this code with try/catch, we chose
			//the latter.
			//NOTE: The shortened size made for rounding errors that led to
			//	our code throwing out-of-bounds exceptions.
		}
		getParent().repaint();
	}

	public void mouseClicked(MouseEvent me){}
	public void mouseEntered(MouseEvent me){}
	public void mouseExited(MouseEvent me){}
	public void mousePressed(MouseEvent me){
		if (me.getButton() == MouseEvent.BUTTON3){
			//TODO Things.
		}
	}
	public void mouseReleased(MouseEvent me){}




}
