public class Destroyer extends Warship{
	public Destroyer(int countryID){
		this.countryID = countryID;
		damage = 2;
	}
	public static int getPrice(){
		return 25;
	}
}
