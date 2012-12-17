public class Cruiser extends Warship{
	public Cruiser(int countryID){
		this.countryID = countryID;
		damage = 1;
	}
	public static int getPrice(){
		return 10;
	}
}
