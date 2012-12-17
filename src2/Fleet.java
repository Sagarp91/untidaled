import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.sql.*;

/**
 * Represents a fleet of ships, traveling between harbors.
 */
public class Fleet{
	private int fleet_id, country_id, source_harbor, target_harbor;
	private Point location;
	private ArrayList<Cruiser> cruisers;
	private ArrayList<Destroyer> destroyers;
	private static final int X_SPEED = 2;
	private static final int Y_SPEED = 2;
	private static final int T = 7;

	public Fleet(int fleet_id, int country_id, int source_harbor, int target_harbor, ArrayList<Cruiser> cruisers, ArrayList<Destroyer> destroyers, Point location){
		this.fleet_id = fleet_id;
		this.country_id = country_id;
		this.source_harbor = source_harbor;
		this.target_harbor = target_harbor;
		this.cruisers = cruisers;
		this.destroyers = destroyers;
		this.location = location;
	}

	public void paint(Graphics g){
		g.setColor(WorldMap.getColor(country_id));
		g.fillOval(location.x, location.y, DrawPanel.harborWidth, DrawPanel.harborHeight);
	}

	public void move(){
		Connection myConnection = Main.getConnection();
		int xLoc = 0, yLoc = 0;
		try{
			Statement stm = myConnection.createStatement();
			ResultSet rs = stm.executeQuery("select xLoc, yLoc from harbor where harbor_id = " + target_harbor);
			rs.next();
			xLoc = rs.getInt("xLoc");
			yLoc = rs.getInt("yLoc");
		} catch(Exception e){
			System.err.println("Could not get xLoc/yLoc for fleet's target harbor.");
			return;
		}

		int width = DrawPanel.harborWidth;
		int height = DrawPanel.harborHeight;

		if (location.x / width == xLoc &&
				location.y / height == yLoc){
			meet();
			removeFromDatabase();
			//Remove from worldmap.
			for (int i = 0; i < WorldMap.fleetList.size(); ++i){
				Fleet fl = WorldMap.fleetList.get(i);
				if (fl.getFleetID() == fleet_id){
					WorldMap.fleetList.remove(fl);
					i = WorldMap.fleetList.size();
				}
			}
			return;
		}

		if (location.x / width < xLoc){
			location.x += X_SPEED;
		} else if (location.x / width > xLoc){
			location.x -= X_SPEED;
		}
		if (location.y / height < yLoc){
			location.y += Y_SPEED;
		} else if (location.y / height > yLoc){
			location.y -= Y_SPEED;
		}
	}

	/**
	 * Calculates losses and ownership of a harbor upon fleet arrival.
	 */
	public void meet(){
		try{
			Harbor hb = WorldMap.harborList.get(target_harbor);
			if (hb.getCountryID() == country_id){
				mergeWithHarbor(hb);
				return;
			}
			int difference = Math.abs(hb.getDamage() - getDamage());

			if (hb.getDamage() > difference){
				while (hb.getDamage() - difference > 0 &&
						hb.getDamage() != 0){
					if (!hb.removeCruiser()){
						hb.removeDestroyer();
					}
				}
			} else{
				while (difference - hb.getDamage() > 0 &&
						hb.getDamage() != 0){
					if (!hb.removeCruiser()){
						hb.removeDestroyer();
					}
				}
			}

			if (getDamage() > difference){
				while (getDamage() - difference > 0 &&
						getDamage() != 0){
					if (!removeCruiser()){
						removeDestroyer();
					}
				}
			} else{
				while (difference - getDamage() > 0 &&
						getDamage() != 0){
					if (!removeCruiser()){
						removeDestroyer();
					}
				}
			}


			if (getDamage() > 0){
				//Fleet won battle
				hb.reset(country_id);
				hb.addCruisers(cruisers);
				hb.addDestroyers(destroyers);
			}
		} catch (Exception e){
			System.err.println("An error occured during a fleet/harbor meet!");
			e.printStackTrace();
		}
	}
	/**
	 * Merges this fleet with an allied harbor. Any excess ships are
	 * destroyed.
	 *
	 * @param hb The harbor to merge with.
	 */
	private void mergeWithHarbor(Harbor hb){
		hb.addCruisers(cruisers);
		hb.addDestroyers(destroyers);
	}
	/**
	 * Removes this fleet from the database.
	 */
	public void removeFromDatabase(){
		try{
			Connection myConnection = Main.getConnection();
			Statement stm = myConnection.createStatement();
			stm.execute("delete from fleet where fleet_id = " + fleet_id);
			stm.close();
		} catch (Exception e){
			System.err.println("Could not remove fleet from database!");
			e.printStackTrace();
		}
	}

	/**
	 * Calculates the damage potential of this fleet.
	 *
	 * @return The damage potential of the fleet.
	 */
	public int getDamage(){
		int result = 0;
		for (Cruiser cs : cruisers){
			result += cs.getDamage();
		}
		for (Destroyer ds : destroyers){
			result += ds.getDamage();
		}
		return result;
	}

	private boolean removeCruiser(){
		if (cruisers.size() == 0){
			return false;
		} else{
			cruisers.remove(cruisers.size() - 1);
			return true;
		}
	}
	private boolean removeDestroyer(){
		if (destroyers.size() == 0){
			return false;
		} else{
			destroyers.remove(destroyers.size() - 1);
			return true;
		}
	}

	public int getFleetID(){
		return fleet_id;
	}

	public String toString(){
		String str = "insert into fleet (fleet_id, country_id, source_harbor, target_harbor, num_cruisers, num_destroyers) values (" + fleet_id + "," + country_id + "," + source_harbor + "," + target_harbor + "," + cruisers.size() + "," + destroyers.size() + ")";
		return str;
	}
}
