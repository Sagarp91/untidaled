import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.sql.*;
import java.util.ArrayList;

/**
 * A dialogue box for creating new harbors and adding them to the database.
 */
public class NewHarbor extends JFrame implements ActionListener{
	private Connection myConnection;
	private Statement stm;
	private ResultSet rs;
	private JTextField nameFld;
	private JComboBox countryBox, xBox, yBox;
	/**
	 * Creates a dialogue box with a connection to the database.
	 *
	 * @param myConnection The connection to the database.
	 */
	public NewHarbor(Connection myConnection){
		this.myConnection = myConnection;
		stm = this.myConnection.createStatement();
		try{
			stm = myConnection.createStatement();
		} catch(Exception e){
			System.err.println("Could not create a statement!");
		}

		setSize(400,400);
		setUndecorated(true);
		setLayout(new BorderLayout());

		//Create labels and text field.
		JLabel nameLbl = new JLabel("Name:", JLabel.CENTER);
		JLabel countryLbl = new JLabel("Country:", JLabel.CENTER);
		JLabel locationLbl = new JLabel("Location:", JLabel.CENTER);
		nameFld = new JTextField();

		//Create the country combo box.
		ArrayList<String> countryList = new ArrayList<String>();
		try{
			rs = stm.executeQuery("select country_name from country");
			while (rs.next()){
				countryList.add(rs.getString("country_name"));
			}
			rs.close();
		} catch(Exception e){
			System.err.println("Something went wrong while trying to create the country name array list!");
			e.printStackTrace();
		}
		String[] namesArray = (String[])(countryList.toArray());
		countryBox = new JComboBox(namesArray);
		countryBox.setSelectedIndex(0);
		countryBox.addActionListener(this);

		//Create the combo boxes for x and y locations.
		Integer[] xArray = new Integer[WorldMap.HARBOR_XBOUND];
		Integer[] yArray = new Integer[WorldMap.HARBOR_YBOUND];
		for (int i = 0; i < xArray.length; ++i){
			xArray[i] = i;
		}
		for (int i = 0; i < yArray.length; ++i){
			yArray[i] = i;
		}
		xBox = new JComboBox(xArray);
		yBox = new JComboBox(yArray);

		//Add everything to panels and the frame.
		JPanel namePnl = new JPanel();
		namePnl.setLayout(new FlowLayout());
		namePnl.add(nameLbl);
		namePnl.add(nameFld);

		JPanel countryPnl = new JPanel();
		countryPnl.setLayout(new FlowLayout());
		countryPnl.add(countryLbl);
		countryPnl.add(countryBox);

		JPanel locationPnl = new JPanel();
		locationPnl.setLayout(new FlowLayout());
		locationPnl.add(locationLbl);
		locationPnl.add(xBox);
		locationPnl.add(yBox);



		add(namePnl, BorderLayout.NORTH);
		add(countryPnl);
		add(locationPnl, BorderLayout.SOUTH);

		setVisible(true);
	}

	/**
	 * Just for testing the frame's looks.
	 */
	public static void main(String[] args){
		Connection cn = null;
		NewHarbor hb = new NewHarbor(cn);
	}
	public void actionPerformed(ActionEvent ae){
		String command = ae.getActionCommand();
		if (command.equals("exit")){
			close();
		} else{
			//Check to see if harbor name is taken.
			String harborNameInput = nameFld.getText();
			rs = stm.executeQuery("select name from harbor");
			boolean nameFound = false;
			while (rs.next() && !nameFound){
				String name = rs.getString("name");
				if (harborNameInput.equals(name)){
					nameFound = true;
				}
			}

			//Check to see if harbor location is taken.
			int xInput = xBox.getSelectedIndex();
			int yInput = yBox.getSelectedIndex();
			rs = stm.executeQuery("select xLoc, yLoc from harbor");
			boolean pointTaken = false;
			while (rs.next() && !pointTaken){
				int x = rs.getInt("xLoc");
				int y = rs.getInt("yLoc");
				if (xInput == x && yInput == y){
					pointTaken = true;
				}
			}

			if (nameFound){
				//Alert the user that name has been taken.
			} else if (pointTaken){
				//Alert user that point is taken.
			} else{
				//Add the harbor to the database.
				//Find next available id.
				rs = stm.executeQuery("select harbor_id from harbor order " +
					"by desc");
				rs.next();
				int id = rs.getInt("harbor_id") + 1;

				//Find country id.
				rs = stm.executeQuery("select country_id from country where country_name = '" + (String)(countryBox.getSelectedItem()) + "'");
				rs.next();
				int country_id = rs.getInt("country_id");

				//Add.
				stm.execute("insert into harbor (harbor_id, harbor_name, country_id, xLoc, yLoc) values (" + id + ", " + harborNameInput + ", " + country_id + ", " + xInput + ", " + yInput + ")");

				rs.close();
				close();
			}
		}
	}

	/**
	 * Closes the frame. In a later implementation, this should also return
	 * focus to the main GUI.
	 */
	public void close(){
		stm.close();
		dispose();
	}
}