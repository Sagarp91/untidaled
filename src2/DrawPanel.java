import javax.swing.JPanel;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;


public class DrawPanel extends JPanel implements MouseListener, MouseMotionListener{
	private ArrayList<Harbor> harborList;
	private final Color BG_COLOR = new Color(150,180,255);
	private final Color HARBOR_COLOR = new Color(250,255,180);
	public static int harborWidth = 1;
	public static int harborHeight = 1;
	private boolean inMove = false;
	private int source_harbor = 0;

	public DrawPanel(){
		this.harborList = WorldMap.harborList;
		addMouseMotionListener(this);
		addMouseListener(this);
	}

	public void updateHarborMap(){
		harborList = WorldMap.harborList;
		getParent().repaint();
	}

	public void paint(Graphics g){
		harborWidth = this.getWidth() / WorldMap.HARBOR_XBOUND;
		harborHeight = this.getHeight() / WorldMap.HARBOR_YBOUND;

		//Paint background.
		g.setColor(BG_COLOR);
		g.fillRect(0, 0, getWidth(), getHeight());

		//Paint harbor symbols.
		for (Harbor hb : harborList){
			Point pt = hb.getLoc();
			hb.paint(g.create(pt.x * harborWidth, pt.y * harborHeight, harborWidth, harborHeight));
		}

		//Paint fleets.
		if (WorldMap.fleetList != null){
			for (Fleet fl : WorldMap.fleetList){
				fl.paint(g);
			}
		}
	}

//---LISTENER METHODS:---

	public void mouseDragged(MouseEvent me){}
	public void mouseMoved(MouseEvent me){
		if (WorldMap.right.isVisible()){
			return;
		}
		Point pt = new Point(me.getPoint().x, me.getPoint().y);
		int x = pt.x / harborWidth;
		int y = pt.y / harborHeight;

		boolean found = false;
		for (int i = 0; i < harborList.size() && !found; ++i){
			Harbor hb = harborList.get(i);
			Point harborLoc = hb.getLoc();
			if (x == harborLoc.x && y == harborLoc.y){
				InfoPanel.harborName = hb.getName();
				InfoPanel.countryName = WorldMap.countryMap.get(hb.getCountryID());
				InfoPanel.harbor_id = hb.getHarborID();
				InfoPanel.country_id = hb.getCountryID();
				found = true;
			}
		}
		
		if (!found){
			InfoPanel.harborName = "";
			InfoPanel.countryName = "";
			InfoPanel.harbor_id = -1;
			InfoPanel.country_id = -1;
		}

		getParent().repaint();
	}

	public void mouseClicked(MouseEvent me){}
	public void mouseEntered(MouseEvent me){}
	public void mouseExited(MouseEvent me){}
	public void mousePressed(MouseEvent me){
		if (me.getButton() == MouseEvent.BUTTON3){
			Point pt = new Point(me.getPoint().x, me.getPoint().y);
			if (WorldMap.isAdmin){
				WorldMap.right.show(this, pt.x, pt.y);
			} else{
				//Only opens right-click menu for user if user owns harbor.
				inMove = false;
				if (InfoPanel.country_id == 1){
					WorldMap.right.show(this, pt.x, pt.y);
				}
			}

		} else if (me.getButton() == MouseEvent.BUTTON1 &&
				!WorldMap.isAdmin){
			if (!inMove){
				if (InfoPanel.country_id == 1){
					inMove = true;
					source_harbor = InfoPanel.harbor_id;
				}
			} else{
				if (InfoPanel.country_id != -1){
					if (source_harbor != InfoPanel.harbor_id){
						Fleet fl = WorldMap.harborList.get(source_harbor).createFleet(InfoPanel.harbor_id);
						if (fl != null){
							WorldMap.fleetList.add(fl);
						}
					}
				}
				inMove = false;
			}
		}
	}
	public void mouseReleased(MouseEvent me){}
}
