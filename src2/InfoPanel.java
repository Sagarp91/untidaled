import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.sql.*;

/**
 * Displays information about a harbor that is being hovered over.
 */
public class InfoPanel extends JPanel{
	private final Color FONT_COLOR = Color.BLACK;
	public static String harborName, countryName;
	public static int harbor_id, country_id;
	private static Connection myConnection;
	
	public InfoPanel(){
		harborName = "";
		countryName = "";
		harbor_id = -1;
		country_id = -1;
		myConnection = Main.getConnection();
	}

	public static void resetInfo(){
		harborName = "";
		countryName = "";
		harbor_id = -1;
		country_id = -1;
	}

	public void paint(Graphics g){
		g.setColor(FONT_COLOR);
		int width = getWidth();
		int yDisplace = 0;
		g.setFont(new Font("Arial", 1, 13));
		yDisplace += 15;

		g.drawString("Harbor:", 10, yDisplace);
		g.drawString("Country:", width / 2, yDisplace);
		yDisplace += 15;
		g.setFont(new Font("Arial", 0, 13));
		g.drawString(harborName, 30, yDisplace);
		g.drawString(countryName, width / 2 + 20, yDisplace);
		yDisplace +=15;

		g.drawString("Cruisers:", 10, yDisplace);
		g.drawString("Destroyers:", width/2, yDisplace);
		yDisplace +=15;
		try{
			Statement stm = myConnection.createStatement();
			ResultSet rs = stm.executeQuery("select num_cruisers, num_destroyers from harbor where harbor_id = " + harbor_id);
			rs.next();
			int num_cruisers = rs.getInt("num_cruisers");
			int num_destroyers = rs.getInt("num_destroyers");
			g.drawString(num_cruisers + "", 30, yDisplace);
			g.drawString(num_destroyers + "", width/2 + 20, yDisplace);
		} catch(Exception e){}
		yDisplace += 15;

		g.drawString("Fishers:", 10, yDisplace);
		g.drawString("Barges:", width/2, yDisplace);
		yDisplace += 15;
		try{
			Statement stm = myConnection.createStatement();
			ResultSet rs = stm.executeQuery("select num_barges, num_fishers from harbor where harbor_id = " + harbor_id);
			rs.next();
			int num_fishers = rs.getInt("num_fishers");
			int num_barges = rs.getInt("num_barges");
			g.drawString(num_fishers + "", 30, yDisplace);
			g.drawString(num_barges + "", width/2 + 20, yDisplace);
		} catch (Exception e){}
		yDisplace += 15;
	}
}
