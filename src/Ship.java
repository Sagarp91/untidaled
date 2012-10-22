/**
* Name:    MisMatch
* Section: 1
* Program: Term Project ["UnTidaled"]
* Date: 10/9/2012
*
*/

/**
 * Superclass of all ships.
 * @author Tommy
 * @version 1.0 10/7/2012
 */
public abstract class Ship {
	protected String name; //Unique name of ship.
	protected String country; //Country of ownership.
	/**
	 * Both value and speed will be defaulted by leaf classes. In this manner,
	 * all ships of the same class will have the same worth and speed.
	 */
	protected double value, speed; //Monetary value and speed of ship.
	protected int curHarbor; //ID of current harbor.
	/**
	 * Creates a ship with the specified name, country, and current harbor.
	 * @param name Unique name of the ship.
	 * @param country Country of ownership.
	 * @param curHarbor Current harbor.
	 */
	public Ship(String name, String country, int curHarbor){
		this.name = name;
		this.country = country;
		this.curHarbor = curHarbor;
	}
	
	public String toString(){
		return "SHIP NAME: " + name + ", COUNTRY: " + country + ", CURRENT" +
				" HARBOR ID: " + curHarbor + "\n    VALUE: " + value +
				", SPEED: " + speed;
	}
	
	//Getters:
	
	public int getCurHarbor(){
		return curHarbor;
	}
	public String getName(){
		return name;
	}
	public String getCountry(){
		return country;
	}
	
	//Setters:
	
	public void setCurHarbor(int ID){
		curHarbor = ID;
	}
	public void setCountry(String country){
		this.country = country;
	}
}
