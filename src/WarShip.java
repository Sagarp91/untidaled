/**
* Name:    MisMatch
* Section: 1
* Program: Term Project ["UnTidaled"]
* Date: 10/9/2012
*
*/

/**
 * Superclass for combat ships.
 * @author Tommy
 * @version 1.0 10/7/2012
 */
public class WarShip extends Ship{
	

	public WarShip(String name, String country, String curHarbor){
		super(name, country, curHarbor);
		this.shipClass = "War Ship";
	}
	
}
