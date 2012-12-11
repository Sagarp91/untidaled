/**
* Name:    MisMatch
* Section: 1
* Program: Term Project ["UnTidaled"]
* Date: 12/11/2012
*
*/

import java.io.BufferedWriter;
import java.io.File;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.*;

/**
 * The gateway between the GUI and the rest of our data. Holds all harbors.
 * @author Tommy
 * @version 1.0 10/7/2012
 */
public class WorldMap extends JFrame implements ActionListener{
	public static ArrayList<Harbor> harborList; //list of harbors
	private static ArrayList<String> countryList; //list of countries
	private static DrawPanel drawPnl; //area to draw on
	public static JPopupMenu right; //right click menu
	public static boolean isAdmin;
	public static final int WORLD_WIDTH = 500, WORLD_HEIGHT = 550;
	public static final int HARBOR_XBOUND = 25, HARBOR_YBOUND = 25;
	public static Statement stm = null;
	public static ResultSet rs = null;
	public WorldMap(boolean isAdmin){
		setBounds(300, 200, WORLD_WIDTH, WORLD_HEIGHT);
		setResizable(false);
		setLayout(new BorderLayout());

		this.isAdmin = isAdmin;

		initVars();
		initMenu();
		initPanels();
		if (isAdmin){
			initRightClick();
		}

		//Database things:
		try{
			Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
			Connection myConnection = DriverManager.getConnection("jdbc:derby:database/untidaled", "app", "app");
			stm = myConnection.createStatement();
		} catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}



		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	/**
	 * Create right-click popup menu
	 */
	private void initRightClick(){
		right = new JPopupMenu();

		JMenu newM = new JMenu("New");

		JMenuItem newShip = new JMenuItem("Ship");
		newShip.setActionCommand("newship");
		newShip.addActionListener(this);

		JMenuItem newHarbor = new JMenuItem("Harbor");
		newHarbor.setActionCommand("newharbor");
		newHarbor.addActionListener(this);

		JMenuItem newCountry = new JMenuItem("Country");
		newCountry.setActionCommand("newcountry");
		newCountry.addActionListener(this);

		JMenu removeM = new JMenu("Remove");

		JMenuItem removeShip = new JMenuItem("Ship");
		removeShip.setActionCommand("removeship");
		removeShip.addActionListener(this);

		JMenuItem removeHarbor = new JMenuItem("Harbor");
		removeHarbor.setActionCommand("removeharbor");
		removeHarbor.addActionListener(this);

		newM.add(newShip);
		newM.add(newHarbor);
		newM.add(newCountry);
		removeM.add(removeShip);
		removeM.add(removeHarbor);
		removeM.setEnabled(false);

		right.add(newM);
		right.add(removeM);
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
		drawPnl = new DrawPanel();
		JPanel botPnl = new JPanel();
		botPnl.setLayout(new BorderLayout());
		botPnl.setPreferredSize(new Dimension(WORLD_WIDTH, 50));
		add(drawPnl, BorderLayout.CENTER);
		add(botPnl, BorderLayout.SOUTH);
		InfoPanel infoPnl = new InfoPanel();
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
		drawPnl.updateHarborMap();
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
				//TODO new things.
			} else if (command.equals("open")){
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File("src/"));
				int returnVal = fc.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION){
					File aFile = fc.getSelectedFile();
					openFile(aFile);
				}
			} else if (command.equals("save")){
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File("src/"));
				int returnVal = fc.showSaveDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION){
					File aFile = fc.getSelectedFile();
					saveFile(aFile);
				}
			} else if (command.equals("exit")){
				//Remove all entries from database.
				try {
					stm.execute("delete from fleet");
					stm.execute("delete from harbor");
					stm.execute("delete from country");
				} catch (Exception e){
					e.printStackTrace();
				}
				System.exit(0);
			} else if (command.equals("newharbor")){
				//TODO new dialogue
			} else if (command.equals("removeship")){
				//remove ship from harbor
				//maybe we won't implement this yet.
			} else if (command.equals("removeharbor")){
				//remove harbor and all ships in it.
			} else if (command.equals("search")){
				//TODO new search gui.
			}
	}

//---FILE IN/OUT:---

	private void openFile(File aFile){
		try{
			//Delete any current entries in database.
			stm.execute("delete from fleet");
			stm.execute("delete from harbor");
			stm.execute("delete from country");

			//Add all entries to database.
			Scanner sc = new Scanner(aFile);
			while (sc.hasNext()){
				stm.execute(sc.nextLine());
			}

			//Create instances based on entries in database.
			rs = stm.executeQuery("select * from country");
			while (rs.next()){
				//TODO create countries based on results.
			}
			rs = stm.executeQuery("select * from harbor");
			while (rs.next()){
				//TODO create new harbor based on results.
			}


			repaint();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	private void saveFile(File aFile){
			try{
				Writer out = new OutputStreamWriter(new FileOutputStream(aFile));
				//TODO writing.
				out.close();
			} catch (Exception e){
				System.err.println("Failed to save!");
			}
	}
}
