/**
* Name:    MisMatch
* Section: 1
* Program: Term Project ["UnTidaled"]
* Date: 10/9/2012
*
*/

import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * The gateway between the GUI and the rest of our data. Holds all harbors.
 * @author Tommy
 * @version 1.0 10/7/2012
 */
public class WorldMap extends JFrame implements ActionListener, MouseListener{
	public static ArrayList<Harbor> harborList;
	private static ArrayList<String> countryList;
	private boolean isAdmin;
	private static final int WORLD_WIDTH = 600, WORLD_HEIGHT = 500;
	public static final int HARBOR_XBOUND = 50, HARBOR_YBOUND = 50;
	public WorldMap(boolean isAdmin){
		setSize(WORLD_WIDTH, WORLD_HEIGHT);
		setResizable(false);
		setLayout(new BorderLayout());

		this.isAdmin = isAdmin;

		initVars();
		initMenu();
		initPanels();



		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	/**
	 * Initialize variables
	 */
	private void initVars(){
		harborList = new ArrayList<Harbor>();
		countryList = new ArrayList<String>();
	}
	/**
	 * Set up the panels and the search button.
	 */
	private void initPanels(){
		DrawPanel drawPnl = new DrawPanel();
		JPanel botPnl = new JPanel();
		botPnl.setLayout(new BorderLayout());
		botPnl.setPreferredSize(new Dimension(WORLD_WIDTH, 50));
		add(drawPnl, BorderLayout.CENTER);
		add(botPnl, BorderLayout.SOUTH);
		JPanel infoPnl = new JPanel();
		JButton searchBtn = new JButton("Search");
		searchBtn.addActionListener(this);
		searchBtn.setActionCommand("search");
		botPnl.add(searchBtn, BorderLayout.EAST);
		botPnl.add(infoPnl, BorderLayout.CENTER);
	}

	/**
	 * Set up the menu bar.
	 */
	private void initMenu(){
		JMenuBar bar = new JMenuBar();
		JMenu fileM = new JMenu("File");

		JMenuItem newM = new JMenuItem("New");
		newM.setActionCommand("new");
		newM.addActionListener(this);

		JMenuItem openM = new JMenuItem("Open");
		openM.setActionCommand("open");
		openM.addActionListener(this);

		JMenuItem saveM = new JMenuItem("Save");
		saveM.setActionCommand("save");
		saveM.addActionListener(this);

		JMenuItem exitM = new JMenuItem("Exit");
		exitM.setActionCommand("exit");
		exitM.addActionListener(this);

		//These are added in a certain order, which is why the if statements
		//are not combined.
		if (isAdmin){
			fileM.add(newM);
		}
		fileM.add(openM);
		if (isAdmin){
			fileM.add(saveM);
		}
		fileM.add(exitM);
		bar.add(fileM);

		setJMenuBar(bar);
	}

	/**
	 * Attempts to add the passed harbor. If it already exists in the world,
	 * does nothing. Otherwise, adds it to the harbor list.
	 * @param hb Harbor to add.
	 */
	public static void addHarbor(Harbor hb){
		for (Harbor harbor : harborList){
			if (harbor.getName().equals(hb.getName())){
				return;
			}
		}
		harborList.add(hb);
	}
	/**
	 * Attempts to remove the named harbor from the world. If found, will
	 * remove both the harbor and all of its ships from the world. Otherwise,
	 * does nothing.
	 * @param name Name of harbor to remove.
	 */
	public static void removeHarbor(String name){
		for (int i = 0; i < harborList.size(); ++i){
			if (harborList.get(i).getName().equals(name)){
				harborList.get(i).clearList();
				harborList.remove(i);
				return;
			}
		}
	}
	/*
	 * Adds a ship to a harbor. Can be called in a static context.
	 * @param ship The ship to add.
	 */
	public static void addShip(Ship ship){
		String harborName = ship.getCurHarbor();
		for (Harbor hb : harborList){
			if (harborName.equals(hb.getName())){
				hb.addShip(ship);
				return;
			}
		}
	}	
	public static String printHarborList(){
		String toReturn = "Harbors in world: \n";
		for (Harbor harbor : harborList){
			toReturn += harbor.getName() + "\n";
		}
		return toReturn;
	}

//---LISTENER METHODS:---

	public void actionPerformed(ActionEvent ae){
			String command = ae.getActionCommand();
			if (command.equals("new")){
					countryList = new ArrayList<String>();
					harborList = new ArrayList<Harbor>();
					//TODO Things.
			} else if (command.equals("open")){
					//TODO Things.
			} else if (command.equals("save")){
					//TODO Things.
			} else if (command.equals("exit")){
					System.exit(0);
			}
	}

	public void mouseEntered(MouseEvent me){}
	public void mouseExited(MouseEvent me){}
	public void mouseClicked(MouseEvent me){}
	public void mousePressed(MouseEvent me){
			//TODO Things
	}
	public void mouseReleased(MouseEvent me){
			//TODO Things
	}
}
