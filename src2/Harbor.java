import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Harbor{
	//We keep cruisers and destroyers separate for the purpose of splitting
	//them easily when creating fleets.
	private ArrayList<Cruiser> cruisers;
	private ArrayList<Destroyer> destroyers;
	//Fishers and barges can be lumped together, because they never have
	//to be split. They live and die together.
	private ArrayList<CivilianShip> civilians;
	private int warShipCap = 20;
	private int civilianShipCap = 20;
	private int country_id;
	private int money = 0;


	public void generate(){
		for (CivilianShip ship : civilians){
			money += ship.generate();
		}
	}
	/**
	 * Creates a fleet using half (rounded up) of each type of warship.
	 *
	 * @return An ArrayList containing all ships in the fleet.
	 */
	public ArrayList<Warship> createFleet(){
		ArrayList<Warship> toSend = new ArrayList<Warship>();
		if (cruisers.size() != 0){
			for (int i = 0; i <= cruisers.size() / 2; ++i){
				toSend.add(cruiser.remove(0));
			}
		}
		if (destroyers.size() != 0){
			for (int i = 0; i <= destroyers.size() / 2; ++i){
				toSend.add(destroyers.remove(0));
			}
		}
	}
	/**
	 * Attempts to add a cruiser.
	 *
	 * @return false if cap has been reached.
	 */
	public boolean addCruiser(){
		if (cruisers.size() + destroyers.size() < warshipCap){
			//Add
			return true;
		} else{
			return false;
		}
	}
	/**
	 * Attempts to add a destroyer.
	 *
	 * @return false if cap has been reached.
	 */
	public boolean addDestroyer(){
		if (cruisers.size() + destroyers.size() < warshipCap){
			//Add to both database & object?
			return true;
		} else{
			return false;
		}
	}
	/**
	 * Attempts to add a fisher.
	 *
	 * @return false if cap has been reached.
	 */
	public boolean addFisher(){
		if (civilians.size() < civilianShipCap){
			//Add
			return true;
		} else{
			return false;
		}
	}
	/**
	 * Attempts to add a barge.
	 *
	 * @return false if cap has been reached.
	 */
	public boolean addBarge(){
		if (civilians.size() < civilianShipCap){
			//Add
			return true;
		} else{
			return false;
		}
	}
	public void paint(Graphics g){
		//TODO many things.
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
		cruisers = new ArrayList<Cruisers>();
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
			if (destroyers.size() + cruisers.size() < warshipCap){
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
	public int getNumDestroyers(){
		return destroyers.size();
	}
	public int getNumCruisers(){
		return cruisers.size();
	}
	public int getNumFishingShips(){
		return fishingShips.size();
	}
	public int getWarshipCap(){
		return warshipCap;
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
}
