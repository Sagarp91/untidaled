public class Barge extends CivilianShip{
	public Barge(int countryID){
		this.countryID = countryID;
		moneyPerTurn = 2;
	}
	public static int getPrice(){
		return 20;
	}
}
