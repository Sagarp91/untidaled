/**
* Name:    MisMatch
* Section: 1
* Program: Term Project ["UnTidaled"]
* Date: 10/9/2012
*
*/

/**
 * Test driver for the Harbor class.
 * @author Tommy
 * @version 1.0 10/8/2012
 */
public class TestHarbor {
	public static void main(String[] args){
		Harbor[] harborArray = new Harbor[2];
		
		harborArray[0] = new Harbor("Spanish Port", 1, "Spain");
		harborArray[0].addShip(new CivilianShip("Santa Maria", "Spain", 1));
		
		harborArray[1] = new Harbor("American Port", 2, "USA");
		harborArray[1].addShip(new CivilianShip("S.S. Anne", "USA", 2));
		
		for (Harbor testHarbor : harborArray){
			System.out.println(testHarbor);
			for (Ship testShip : testHarbor.getShipList()){
				System.out.println(testShip);
			}
			System.out.println("---");
		}
		
		Ship swapShip = harborArray[0].removeShip("Santa Maria");
		swapShip.setCountry("USA");
		swapShip.setCurHarbor(2);
		harborArray[1].addShip(swapShip);
		
		
		System.out.println("\n---Printing harbor data after swap:");
		for (Harbor testHarbor : harborArray){
			System.out.println(testHarbor);
			for (Ship testShip : testHarbor.getShipList()){
				System.out.println(testShip);
			}
			System.out.println("---");
		}
	}
}
