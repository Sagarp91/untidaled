public abstract class Ship{
	protected String shipType;
	protected int countryID;

	public int getCountryID(){
		return countryID;
	}
	public String getType(){
		return shipType;
	}
	public static int getPrice(){
		return 0;
	}
}
