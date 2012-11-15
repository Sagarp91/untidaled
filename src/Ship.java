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
	protected String curHarbor; //Name of current harbor.
	protected String shipClass; //Name of the type of ship it is.
	/**
	 * Creates a ship with the specified name, country, and current harbor.
	 * @param name Unique name of the ship.
	 * @param country Country of ownership.
	 * @param curHarbor Current harbor.
	 */
	public Ship(String name, String country, String curHarbor){
		this.name = name;
		this.country = country;
		this.curHarbor = curHarbor;
	}
	
	public String toString(){
		return name + " " + country + " " + curHarbor + " " +
			shipClass;
	}
	
	//Getters:
	
	public String getCurHarbor(){
		return curHarbor;
	}
	public String getName(){
		return name;
	}
	public String getCountry(){
		return country;
	}
	public String getShipClass(){
		return shipClass;
	}
	
	//Setters:
	
	public void setCurHarbor(String harbor){
		curHarbor = harbor;
	}
	public void setCountry(String country){
		this.country = country;
	}
}
