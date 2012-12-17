import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
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
	private final int FRAME_WIDTH = 250, FRAME_HEIGHT = 400;
	/**
	 * Creates a dialogue box with a connection to the database.
	 *
	 * @param myConnection The connection to the database.
	 */
	public NewHarbor(){
		myConnection = Main.getConnection();

		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setUndecorated(true);
		setLayout(new FlowLayout());

		//Create labels and text field.
		Dimension lblDim = new Dimension(60, 30);
		JLabel nameLbl = new JLabel("Name:", JLabel.RIGHT);
		nameLbl.setPreferredSize(lblDim);
		JLabel countryLbl = new JLabel("Country:", JLabel.RIGHT);
		countryLbl.setPreferredSize(lblDim);
		JLabel locationLbl = new JLabel("Location:", JLabel.RIGHT);
		locationLbl.setPreferredSize(lblDim);
		nameFld = new JTextField();
		nameFld.setPreferredSize(new Dimension(150,30));

		//Create the country combo box.
		ArrayList<String> countryList = new ArrayList<String>();
		try{
			Statement stm = myConnection.createStatement();
			rs = stm.executeQuery("select country_name from country");
			while (rs.next()){
				countryList.add(rs.getString("country_name"));
			}
			rs.close();
			stm.close();
		} catch(Exception e){
			System.err.println("Something went wrong while trying to create the country name array list!");
			e.printStackTrace();
		}
		countryBox = new JComboBox(countryList.toArray());
		countryBox.setSelectedIndex(0);
		countryBox.addActionListener(this);
		countryBox.setPreferredSize(new Dimension(150, 30));

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
		xBox.setPreferredSize(new Dimension(70,30));
		yBox = new JComboBox(yArray);
		yBox.setPreferredSize(new Dimension(70,30));

		//Create buttons.
		JButton cancelBtn = new JButton("cancel");
		JButton okayBtn = new JButton("okay");
		cancelBtn.addActionListener(this);
		okayBtn.addActionListener(this);
		cancelBtn.setActionCommand("cancel");
		okayBtn.setActionCommand("okay");
		cancelBtn.setPreferredSize(new Dimension(FRAME_WIDTH/2, 30));
		okayBtn.setPreferredSize(new Dimension(FRAME_WIDTH/2, 30));

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

		JPanel btnPnl = new JPanel();
		btnPnl.setLayout(new FlowLayout());
		btnPnl.add(cancelBtn);
		btnPnl.add(okayBtn);

		add(namePnl);
		add(countryPnl);
		add(locationPnl);
		add(btnPnl);

		setVisible(true);
	}

	/**
	 * Just for testing the frame's looks.
	 */
	public static void main(String[] args){
		NewHarbor hb = new NewHarbor();
	}
	public void actionPerformed(ActionEvent ae){
		String command = ae.getActionCommand();
		if (command.equals("cancel")){
			close();
		} else if (command.equals("okay")){
			try{
			//Check to see if harbor name is taken.
			String harborNameInput = nameFld.getText();
			Statement stm = myConnection.createStatement();
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
				JOptionPane.showMessageDialog(this, "Name taken!");
			} else if (pointTaken){
				JOptionPane.showMessageDialog(this, "Location already in use!");
			} else{
				//Add the harbor to the database.
				//Find next available id.
				int id = 0;
				try{
					rs = stm.executeQuery("select harbor_id from harbor " +
						"order by harbor_id desc");
					rs.next();
					id = rs.getInt("harbor_id") + 1;
				} catch(Exception e){}

				//Find country id.
				rs = stm.executeQuery("select country_id from country where country_name = '" + (String)(countryBox.getSelectedItem()) + "'");
				rs.next();
				int country_id = rs.getInt("country_id");

				//Add.
				Harbor hb = new Harbor(harborNameInput, id, country_id, xInput, yInput);
				stm.execute(hb.toString());
				WorldMap.addHarbor(hb);

				stm.close();
				rs.close();
				close();
			}
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	/**
	 * Closes the frame and enables the WorldMap.
	 */
	public void close(){
		Main.enableWorldMap();
		dispose();
	}
}
