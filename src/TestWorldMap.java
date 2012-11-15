/**
* Name:    MisMatch
* Section: 1
* Program: Term Project ["UnTidaled"]
* Date: 10/9/2012
*
*/

/**
 * Tests the WorldMap class.
 * @author Tommy
 * @version 1.0 10/9/2012
 */
public class TestWorldMap {
	public static void main(String[] args){
		WorldMap.addHarbor(new Harbor("Spanish Port", "Spain", new Point(1,1)));
		WorldMap.addHarbor(new Harbor("Indian Port", "India", new Point(2,2)));
		System.out.println(WorldMap.printHarborList());
		
		WorldMap.removeHarbor("Spanish Port");
		System.out.println("Printing after removing Spanish port.");
		System.out.println(WorldMap.printHarborList());
	}
}
