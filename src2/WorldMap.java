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
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.sql.*;

/**
 * The gateway between the GUI and the rest of our data. Holds all harbors.
 * @author Tommy
 * @version 1.1 12/16/2012
 */
public class WorldMap extends JFrame implements ActionListener{
	public static ArrayList<Harbor> harborList; //list of harbors
	public static ArrayList<Fleet> fleetList; //list of fleets
	public static HashMap<Integer, String> countryMap; //map of countries/id's
	public static HashMap<Integer, Color> colorMap; //map of colors
	private static DrawPanel drawPnl; //area to draw on
	public static JPopupMenu right; //right click menu
	public static boolean isAdmin;
	public static final int WORLD_WIDTH = 700, WORLD_HEIGHT = 650;
	public static final int HARBOR_XBOUND = 25, HARBOR_YBOUND = 25;
	private static Connection myConnection;
	public static Statement stm = null;
	public static ResultSet rs = null;
	private static Timer tmr;
	private int generateCounter = 0;
	private final int GENERATE_INTERVAL = 100;
	private int fleetCounter = 0;
	private final int FLEET_INTERVAL = 5;
	public WorldMap(boolean isAdmin){
		setBounds(100, 100, WORLD_WIDTH, WORLD_HEIGHT);
		setResizable(false);
		setLayout(new BorderLayout());

		this.isAdmin = isAdmin;

		initVars();
		initMenu();
		initPanels();
		initRightClick();

		myConnection = Main.getConnection();
		clearDatabase();
		insertNeutralCountry();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		if (!isAdmin){
			JOptionPane.showMessageDialog(this, "Select a map to play!");
			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File("src/"));
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION){
				File aFile = fc.getSelectedFile();
				openFile(aFile);
			}
		}
	}

	/**
	 * Clears all entries in the database.
	 */
	private void clearDatabase(){
		try{
			Statement stm = myConnection.createStatement();
			stm.execute("delete from fleet");
			stm.execute("delete from harbor");
			stm.execute("delete from country");
		} catch(Exception e){
			System.err.println("Could not clear database entries.");
			e.printStackTrace();
		}
	}
	/**
	 * Inserts the neutral country.
	 */
	private void insertNeutralCountry(){
		try{
			Statement stm = myConnection.createStatement();
			stm.execute("insert into country (country_id, country_name) values (0, 'Neutral')");
			countryMap.put(0, "Neutral");
		} catch(Exception e){
			System.err.println("Could not insert neutral country.");
			e.printStackTrace();
		}
	}

	/**
	 * Create right-click popup menu (based on what mode was selected).
	 */
	private void initRightClick(){
		right = new JPopupMenu();

		if (isAdmin){
			JMenu newM = new JMenu("New");

			JMenuItem newHarbor = new JMenuItem("Harbor");
			JMenuItem newCountry = new JMenuItem("Country");

			newHarbor.setActionCommand("newHarbor");
			newCountry.setActionCommand("newCountry");

			newHarbor.addActionListener(this);
			newCountry.addActionListener(this);

			newM.add(newHarbor);
			newM.add(newCountry);

			right.add(newM);
		} else{
			JMenu buyM = new JMenu("Buy");

			JMenu warM = new JMenu("War ship");
			JMenu civM = new JMenu("Civilian ship");

			JMenuItem cruiserM = new JMenuItem("Cruiser ($" + 
				Cruiser.getPrice() + ")");
			JMenuItem destroyerM = new JMenuItem("Destroyer ($" + 
				Destroyer.getPrice() + ")");
			JMenuItem fisherM = new JMenuItem("Fisher ($" +
				Fisher.getPrice() + ")");
			JMenuItem bargeM = new JMenuItem("Barge ($" +
				Barge.getPrice() + ")");

			cruiserM.setActionCommand("newCruiser");
			destroyerM.setActionCommand("newDestroyer");
			fisherM.setActionCommand("newFisher");
			bargeM.setActionCommand("newBarge");

			cruiserM.addActionListener(this);
			destroyerM.addActionListener(this);
			fisherM.addActionListener(this);
			bargeM.addActionListener(this);

			warM.add(cruiserM);
			warM.add(destroyerM);

			civM.add(fisherM);
			civM.add(bargeM);

			buyM.add(warM);
			buyM.add(civM);

			right.add(buyM);
		}


	}
	/**
	 * Initialize variables
	 */
	private void initVars(){
		harborList = new ArrayList<Harbor>();
		countryMap = new HashMap<Integer, String>();
		colorMap = new HashMap<Integer, Color>();
		colorMap.put(0, Color.GRAY);
		colorMap.put(1, Color.BLUE);
		colorMap.put(2, Color.RED);
		colorMap.put(3, Color.GREEN);
		colorMap.put(4, Color.BLACK);
		colorMap.put(5, Color.WHITE);
		colorMap.put(6, Color.ORANGE);
		colorMap.put(7, Color.YELLOW);
	}
	/**
	 * Set up the panels and the search button.
	 */
	private void initPanels(){
		drawPnl = new DrawPanel();
		JPanel botPnl = new JPanel();
		botPnl.setLayout(new BorderLayout());
		botPnl.setPreferredSize(new Dimension(WORLD_WIDTH, 100));
		add(drawPnl, BorderLayout.CENTER);
		add(botPnl, BorderLayout.SOUTH);
		InfoPanel infoPnl = new InfoPanel();
		JButton searchBtn = new JButton("Stats");
		searchBtn.addActionListener(this);
		searchBtn.setActionCommand("stats");
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
	 * A method to keep track of time and animate objects. Called by a timer.
	 * Only used in User mode. Disabled while right-clicking.
	 */
	private void tick(){
		if (!right.isVisible() && isEnabled()){
			generateCounter++;
			if (generateCounter >= GENERATE_INTERVAL){
				generateCounter = 0;
				for (Harbor hb : harborList){
					hb.generate();
				}
			}
			fleetCounter++;
			if (fleetCounter >= FLEET_INTERVAL){
				fleetCounter = 0;
				ArrayList<Fleet> temp = new ArrayList<Fleet>();
				for (Fleet fl : fleetList){
					temp.add(fl);
				}
				for (Fleet fl : temp){
					fl.move();
				}
			}
			repaint();
		}
	}

	/**
	 * Attempts to add the passed harbor. If it already exists in the world,
	 * does nothing. Otherwise, adds it to the harbor list.
	 *
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
				harborList.remove(i);
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
	
	/**
	 * Get the country's corresponding color.
	 *
	 * @param country_id The country's id.
	 */
	public static Color getColor(int country_id){
		return colorMap.get(country_id);
	}

//---LISTENER METHODS:---

	public void actionPerformed(ActionEvent ae){
		String command = ae.getActionCommand();
		if (command.equals("new")){
			harborList = new ArrayList<Harbor>();
			countryMap = new HashMap<Integer, String>();
			clearDatabase();
			insertNeutralCountry();
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
			clearDatabase();
			System.exit(0);
		} else if (command.equals("newHarbor")){
			NewHarbor dialogue = new NewHarbor();
			setEnabled(false);
		} else if (command.equals("newCountry")){
			NewCountry dialogue = new NewCountry();
			setEnabled(false);
		} else if (command.equals("newCruiser")){
			Harbor hb = harborList.get(InfoPanel.harbor_id);
			if (hb.getMoney() >= Cruiser.getPrice()){
				if (hb.getNumWarShips() < hb.getWarShipCap()){
					hb.addCruiser();
				}
			}
		} else if (command.equals("newDestroyer")){
			Harbor hb = harborList.get(InfoPanel.harbor_id);
			if (hb.getMoney() >= Destroyer.getPrice()){
				if (hb.getNumWarShips() < hb.getWarShipCap()){
					hb.addDestroyer();
				}
			}
		} else if (command.equals("newFisher")){
			Harbor hb = harborList.get(InfoPanel.harbor_id);
			if (hb.getMoney() >= Fisher.getPrice()){
				if (hb.getNumCivilianShips() < hb.getCivilianShipCap()){
					hb.addFisher();
				}
			}
		} else if (command.equals("newBarge")){
			Harbor hb = harborList.get(InfoPanel.harbor_id);
			if (hb.getMoney() >= Barge.getPrice()){
				if (hb.getNumCivilianShips() < hb.getCivilianShipCap()){
					hb.addBarge();
				}
			}
		} else if (command.equals("stats")){
			Stats dialogue = new Stats();
			setEnabled(false);
		}
	}

//---FILE IN/OUT:---

	/**
	 * Opens a file. The file should be written as SQL insert statements.
	 *
	 * @param aFile The file to read from.
	 */
	private void openFile(File aFile){
		try{
			//Create statement
			stm = myConnection.createStatement();
			//Delete any current entries in database.
			clearDatabase();

			//Reset all member variables.
			countryMap = new HashMap<Integer, String>();
			harborList = new ArrayList<Harbor>();
			fleetList = new ArrayList<Fleet>();
			generateCounter = 0;
			fleetCounter = 0;

			//Add all entries to database.
			Scanner sc = new Scanner(aFile);
			while (sc.hasNext()){
				stm.execute(sc.nextLine());
			}

			//Create instances based on entries in database.
			rs = stm.executeQuery("select * from country");
			while (rs.next()){
				countryMap.put(rs.getInt("country_id"), rs.getString("country_name"));
			}

			rs = stm.executeQuery("select * from harbor");
			while (rs.next()){
				int id = rs.getInt("harbor_id");
				int country_id = rs.getInt("country_id");
				String name = rs.getString("name");
				int x = rs.getInt("xLoc");
				int y = rs.getInt("yLoc");

				harborList.add(new Harbor(name, id, country_id, x, y));
			}

			if (!isAdmin){
				tmr = new Timer();
				TimerTask task = new TimerTask(){
					public void run(){
						tick();
					}
				};
				tmr.schedule(task, 0, 10);
			}
			drawPnl.updateHarborMap();

			repaint();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Saves data as SQL insert statements. Fleets are not saved, since they
	 * are made only when playing.
	 *
	 * @param aFile File to write to.
	 */
	private void saveFile(File aFile){
			try{
				Writer out = new OutputStreamWriter(new FileOutputStream(aFile));
				for (Integer country_id : countryMap.keySet()){
					String str = "insert into country (country_id, country_name) values (" + country_id + ", " + "'" + countryMap.get(country_id) + "')\n";
					out.write(str);
				}

				for (Harbor hb : harborList){
					String str = hb.toString() + "\n";
					out.write(str);
				}

				out.close();
			} catch (Exception e){
				System.err.println("Failed to save!");
			}
	}
}
