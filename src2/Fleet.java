import java.util.ArrayList;

/**
 * Represents a fleet of ships, traveling between harbors.
 */
public class Fleet{
	private int fleet_id, country_id, source_harbor, target_harbor;
	private ArrayList<Cruiser> cruisers;
	private ArrayList<Destroyer> destroyers;

	public Fleet(int fleet_id, int country_id, int source_harbor, int target_harbor, ArrayList<Cruiser> cruisers, ArrayList<Destroyer> destroyers){
		this.fleet_id = fleet_id;
		this.country_id = country_id;
		this.source_harbor = source_harbor;
		this.target_harbor = target_harbor;
		this.cruisers = cruisers;
		this.destroyers = destroyers;
	}

	public String toString(){
		String str = "insert into fleet (fleet_id, country_id, source_harbor, target_harbor, num_cruiser, num_destroyer) value (" + fleet_id + "," + country_id + "," + source_harbor + "," + target_harbor + "," + cruisers.size() + "," + destroyers.size() + ")";
		return str;
	}
}
