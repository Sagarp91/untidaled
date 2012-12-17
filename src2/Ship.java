public abstract class Ship{
	protected String shipType;
	protected int countryID;
	protected static int cost;

	public int getCountryID(){
		return countryID;
	}
	public String getType(){
		return shipType;
	}
	public static int getPrice(){
		return cost;
	}
}
