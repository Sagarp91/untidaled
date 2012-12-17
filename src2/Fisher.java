public class Fisher extends CivilianShip{
	public Fisher(int countryID){
		this.countryID = countryID;
		moneyPerTurn = 1;
	}

	public static int getPrice(){
		return 8;
	}
}
