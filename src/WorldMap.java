/**
* Name:    MisMatch
* Section: 1
* Program: Term Project ["UnTidaled"]
* Date: 10/9/2012
*
*/

import java.util.ArrayList;

/**
 * The gateway between the GUI and the rest of our data. Holds all harbors.
 * @author Tommy
 * @version 1.0 10/7/2012
 */
public class WorldMap {
	private static ArrayList<Harbor> harborList = new ArrayList<Harbor>();
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
	public static String printHarborList(){
		String toReturn = "Harbors in world: \n";
		for (Harbor harbor : harborList){
			toReturn += harbor.getName() + "\n";
		}
		return toReturn;
	}
}
