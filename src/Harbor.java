/**
* Name:    MisMatch
* Section: 1
* Program: Term Project ["UnTidaled"]
* Date: 10/9/2012
*
*/

import java.util.ArrayList;
/**
 * Holds ships.
 * @author Tommy
 * @version 1.0 10/7/2012
 */
public class Harbor {
	private ArrayList<Ship> shipList;
	private String country; //Country of ownership.
	private String name; //Unique name of harbor.
	private Point loc; //Coordinates of the harbor.
	
	public Harbor(String name, String country, Point loc){
		this.name = name;
		this.country = country;
		this.loc = loc;
		shipList = new ArrayList<Ship>();
	}
	
	public String toString(){
		return "HARBOR NAME: " + name + ", COUNTRY: " + country +
			"LOCATION: " + loc;
	}
	
	//Mutators:
	
	/**
	 * Attempts to add the given ship to the list. If the ship is already on
	 * the list, returns -1 for a failed addition. Otherwise, returns 1.
	 * @return 1, if addition was successful. -1 otherwise.
	 */
	public int addShip(Ship ship){
		for (int i = 0; i < shipList.size(); ++i){
			if (ship.getName().equals(shipList.get(i).getName())){
				return -1;
			}
		}
		shipList.add(ship);
		return 1;
	}
	/**
	 * Attempts to remove the ship of the given name. If the ship is found,
	 * returns the ship. Otherwise, returns null.
	 * @param name Name of the ship to remove.
	 * @return The ship that was removed, or null if removal failed.
	 */
	public Ship removeShip(String name){
		for (int i = 0; i < shipList.size(); ++i){
			if (shipList.get(i).getName().equals(name)){
				return shipList.remove(i);
			}
		}
		return null;
	}
	/**
	 * Clears the ship list. For what reason, I have no idea. Maybe a nuclear
	 * fallout. :D
	 */
	public void clearList(){
		shipList = new ArrayList<Ship>();
	}
	
	//Getters:
	
	public Point getLoc(){
		return loc;
	}
	public String getCountry(){
		return country;
	}
	public String getName(){
		return name;
	}
	public ArrayList<Ship> getShipList(){
		return shipList;
	}
	public boolean hasShip(Ship ship){
		for (Ship s : shipList){
			if (s.getName().equals(ship.getName())){
				return true;
			}
		}
		return false;
	}
	
	//Setters:
	
	public void setCountry(String country){
		this.country = country;
	}
}
