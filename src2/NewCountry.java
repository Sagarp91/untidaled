import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;

/**
 * Dialogue box for creating a new country. Does not allow duplicate names.
 *
 * @author Mismatch
 */
public class NewCountry extends JFrame implements ActionListener{
	/**
	 * The input text field.
	 */
	private JTextField inputFld;
	/**
	 * The provided connection to the database.
	 */
	private Connection myConnection;

	/**
	 * Creates a dialogue box for adding new countries to the database. If the
	 * country name is already in the database, alerts the user.
	 */
	public NewCountry(){
		this.myConnection = Main.getConnection();
		setSize(200,140);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//I really didn't want to do this, but some machines offset the
		//width and height of the frame by a lot.
		setUndecorated(true);

		//Prompt things:
		JLabel nameLbl = new JLabel("Enter the new country's name:",
				JLabel.CENTER);
		nameLbl.setPreferredSize(new Dimension(this.getWidth(),40));
		add(nameLbl, BorderLayout.NORTH);
		inputFld = new JTextField();
		add(inputFld);


		//Button things:
		JButton cancelBtn = new JButton("Cancel");
		JButton enterBtn = new JButton("Enter");
		cancelBtn.addActionListener(this);
		enterBtn.addActionListener(this);
		cancelBtn.setActionCommand("cancel");
		enterBtn.setActionCommand("enter");
		cancelBtn.setPreferredSize(new Dimension(this.getWidth()/2, 30));
		enterBtn.setPreferredSize(new Dimension(this.getWidth()/2, 30));

		JPanel btnPnl = new JPanel();
		btnPnl.setLayout(new BorderLayout());
		btnPnl.add(cancelBtn, BorderLayout.WEST);
		btnPnl.add(enterBtn, BorderLayout.EAST);

		add(btnPnl, BorderLayout.SOUTH);

		setVisible(true);
	}
	/**
	 * Just here to see how the class looks.
	 *
	 * @param args Command line arguments.
	 */
	public static void main(String[] args){
		NewCountry nc = new NewCountry();
	}

	public void actionPerformed(ActionEvent ae){
		String command = ae.getActionCommand();
		if (command.equals("cancel")){
			//exit
			close();
		} else{
			try{
			Statement stm = myConnection.createStatement();
			ResultSet rs = stm.executeQuery("select country_name from country");

			//Check to see if name entered has been taken.
			boolean taken = false;
			String input = inputFld.getText();
			while (rs.next() && !taken){
				String name = rs.getString("country_name");
				if (input.equals(name)){
					taken = true;
				}
			}

			if (input.length() > 50){
				JOptionPane.showMessageDialog(this, "Name too long! (Must be 50 characters or less.)");
			} else if (taken){
				JOptionPane.showMessageDialog(this, "Name taken!");
			} else{
				//Add the country to the database and exit.
				rs = stm.executeQuery("select country_id from country order by country_id desc");
				int id;
				try{
					rs.next();
					id = rs.getInt("country_id") + 1;
				} catch(Exception e){
					id = 0;
				}
				stm.execute("insert into country (country_id, country_name)" +
					" values ("+ id + ", '" + input + "')");

				WorldMap.countryMap.put(id, input);
				
				rs.close();
				stm.close();
				close();
			}
			} catch(Exception e){
				System.err.println("Something went wrong while trying to add " +
					"a new country.");
				e.printStackTrace();
			}
		}
	}
	/**
	 * Closes the frame. Eventually, we also want this to enable or return focus
	 * to the main GUI.
	 */
	private void close(){
		Main.enableWorldMap();
		dispose();
	}
}
