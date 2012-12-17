import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * Acts as both 1) a dialogue for selecting modes, and 2) a database connection
 * provider. As a side note, the "admin" mode should probably be renamed as
 * "level creator", or something to that effect.
 */
public class Main{
	private static Connection myConnection;
	private static WorldMap map;
	public static void main(String[] args){
		createConnection();
		JFrame frm = new JFrame();

		Object[] options = {"Admin", "User", "Exit"};
		int selection = JOptionPane.showOptionDialog(
			frm, "Choose run mode:", "Untidaled",
			JOptionPane.YES_NO_CANCEL_OPTION,
			JOptionPane.QUESTION_MESSAGE,
			null, options, options[2]);

		frm.dispose();
		switch(selection){
			case 0:
				//Run as admin.
				map = new WorldMap(true);
				break;
			case 1:
				//Run as user.
				map = new WorldMap(false);
				break;
			case 2:
				System.exit(0);
				break;
		}
	}

	/**
	 * Enables the world map. Called by dialogue boxes.
	 */
	public static void enableWorldMap(){
		map.setEnabled(true);
	}

	/**
	 * Returns a connection to the untidaled database.
	 *
	 * @return A connection to the database.
	 */
	public static Connection getConnection(){
		return myConnection;
	}
	/**
	 * Attempts to connect to the untidaled database.
	 */
	public static void createConnection(){
		try{
//			Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
			myConnection = DriverManager.getConnection("jdbc:derby:database/untidaled", "mismatch", "mismatch");
		} catch(Exception e){
			System.err.println("Could not connect to the database.");
		}
	}
}
