import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.sql.*;

public class Harbor{
	//We keep cruisers and destroyers separate for the purpose of splitting
	//them easily when creating fleets.
	private ArrayList<Cruiser> cruisers;
	private ArrayList<Destroyer> destroyers;
	//Fishers and barges can be lumped together, because they never have
	//to be split.
	private ArrayList<CivilianShip> civilians;
	private String name;
	private int warShipCap = 20;
	private int civilianShipCap = 20;
	private int country_id;
	private int id;
	private int xLoc, yLoc;
	/**
	 * The harbor's bank.
	 */
	private int money = 0;
	/**
	 * The amount of money harbors generate on their own.
	 */
	private final int HARBOR_GENERATE = 2;

	/**
	 * Creates a harbor with the given parameters. This is the least amount
	 * of data needed to make a harbor.
	 */
	public Harbor(String name, int id, int country_id, int xLoc, int yLoc){
		this.name = name;
		this.id = id;
		this.country_id = country_id;
		this.xLoc = xLoc;
		this.yLoc = yLoc;

		cruisers = new ArrayList<Cruiser>();
		destroyers = new ArrayList<Destroyer>();
		civilians = new ArrayList<CivilianShip>();
	}


	public void generate(){
		for (CivilianShip ship : civilians){
			money += ship.generate();
		}
		money += HARBOR_GENERATE;
	}
	/**
	 * Creates a fleet using half (rounded up) of each type of warship.
	 *
	 * @param target_harbor The id of the harbor to send the fleet to.
	 *
	 * @return An ArrayList containing all ships in the fleet.
	 */
	public Fleet createFleet(int target_harbor){
		ArrayList<Cruiser> cruiserSend = new ArrayList<Cruiser>();
		if (cruisers.size() != 0){
			for (int i = 0; i <= cruisers.size() / 2; ++i){
				cruiserSend.add(cruisers.remove(0));
			}
		}

		ArrayList<Destroyer> destroyerSend = new ArrayList<Destroyer>();
		if (destroyers.size() != 0){
			for (int i = 0; i <= destroyers.size() / 2; ++i){
				destroyerSend.add(destroyers.remove(0));
			}
		}

		int id = 0;
		try{
			Statement stm = Main.getConnection().createStatement();
			ResultSet rs = stm.executeQuery("select fleet_id from fleet order by fleet_id desc");
			try{
				rs.next();
				id = rs.getInt("fleet_id") + 1;
			} catch(Exception e){}
			Fleet fl = new Fleet(id, country_id, id, target_harbor, cruiserSend, destroyerSend);
			rs.close();
			stm.close();
			return fl;
		} catch(Exception e){
			System.err.println("Error when creating fleet!");
			return null;
		}
	}
	/**
	 * Adds a cruiser. Assumes cap hasn't been reached and it has enough money.
	 */
	public void addCruiser(){
		cruisers.add(new Cruiser(country_id));
		money -= Cruiser.getPrice();
	}
	/**
	 * Adds a destroyer. Assumes cap hasn't been reached and it has enough
	 * money.
	 */
	public void addDestroyer(){
		destroyers.add(new Destroyer(country_id));
		money -= Destroyer.getPrice();
	}
	/**
	 * Adds a fisher. Assumes cap hasn't been reached and it has enough money.
	 */
	public void addFisher(){
		civilians.add(new Fisher(country_id));
		money -= Fisher.getPrice();
	}
	/**
	 * Adds a barge. Assumes cap hasn't been reached and it has enough money.
	 */
	public void addBarge(){
		civilians.add(new Barge(country_id));
		money -= Barge.getPrice();
	}

	/**
	 * Attempts to remove a destroyer, and returns whether or not the removal
	 * was successful.
	 *
	 * @return true if removal successful, false otherwise.
	 */
	public boolean removeDestroyer(){
		if (destroyers.size() > 0){
			destroyers.remove(destroyers.size() - 1);
			return true;
		} else{
			return false;
		}
	}

	/**
	 * Attempts to remove a cruiser, and returns whether or not the removal
	 * was successful.
	 *
	 * @return true if removal successful, false otherwise.
	 */
	public boolean removeCruiser(){
		if (cruisers.size() > 0){
			cruisers.remove(cruisers.size() - 1);
			return true;
		} else{
			return false;
		}
	}

	/**
	 * Paints itself as a rectangle. Color based on country_id.
	 *
	 * @param g The segment of graphics to paint on.
	 */
	public void paint(Graphics g){
		g.setColor(WorldMap.getColor(country_id));
		g.fillRect(0, 0, DrawPanel.harborWidth, DrawPanel.harborHeight);
	}
	public void animate(){
		//TODO blinking corners? maybe later.
	}

	/**
	 * Sums the total damage potential of the harbor.
	 *
	 * @return Total damage by harbor.
	 */
	public int getDamage(){
		int dmg = 0;
		for (Cruiser ship : cruisers){
			dmg += ship.getDamage();
		}
		for (Destroyer ship : destroyers){
			dmg += ship.getDamage();
		}
		return dmg;
	}

	/**
	 * Resets the harbor after a successful takeover.
	 * 
	 * @param ships Surviving warships after the attack.
	 * @param country_id New country for harbor.
	 */
	public void reset(ArrayList<Warship> ships, int country_id){
		this.country_id = country_id;
		civilians = new ArrayList<CivilianShip>();
		destroyers = new ArrayList<Destroyer>();
		cruisers = new ArrayList<Cruiser>();
		addWarships(ships);
	}

	/**
	 * Adds warships for this harbor, most likely called after a battle.
	 * Any ships exceeding the ship cap will be discarded.
	 *
	 * @param ships The ships to place in this harbor.
	 */
	public void addWarships(ArrayList<Warship> ships){
		for (Warship ship : ships){
			if (destroyers.size() + cruisers.size() < warShipCap){
				if (ship.getType().equals("Destroyer")){
					destroyers.add((Destroyer)ship);
				} else if (ship.getType().equals("Cruiser")){
					cruisers.add((Cruiser)ship);
				}
			} else{
				break;
			}
		}
	}

	/**
	 * Returns an SQL statement to insert the harbor.
	 *
	 * @return An SQL insert statement.
	 */
	public String toString(){
		String str = "insert into harbor (name, harbor_id, country_id, harbor_bank, num_cruisers, num_destroyers, num_fishers, num_barges, civilian_cap, war_cap, xLoc, yLoc) values (";
		String strName = "'" + name + "'";
		str += strName + "," + id + "," + country_id + "," + money + "," +
			cruisers.size() + "," + destroyers.size() + "," + civilians.size() +
			"," + civilians.size() + "," + civilianShipCap + "," + warShipCap +
			"," + xLoc + "," + yLoc + ")";
		return str;
	}

	//Getters:
	/**
	 * Grabs all warships from this harbor in the event of an attack.
	 *
	 * @return An ArrayList of warships from this harbor.
	 */
	public ArrayList<Warship> getAllWarships(){
		ArrayList<Warship> toSend = new ArrayList<Warship>();
		for (Cruiser ship : cruisers){
			toSend.add(ship);
		}
		for (Destroyer ship : destroyers){
			toSend.add(ship);
		}
		return toSend;
	}
	public String getName(){
		return name;
	}
	public int getNumDestroyers(){
		return destroyers.size();
	}
	public int getNumCruisers(){
		return cruisers.size();
	}
	public int getNumFishingShips(){
		return civilians.size();
	}
	public int getWarshipCap(){
		return warShipCap;
	}
	public int getCivilianShipCap(){
		return civilianShipCap;
	}
	public int getMoney(){
		return money;
	}
	public int getCountryID(){
		return country_id;
	}
	public Point getLoc(){
		return new Point(xLoc, yLoc);
	}
}
