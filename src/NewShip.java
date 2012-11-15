import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;

public class NewShip extends JFrame implements ActionListener{
	private JTextField shipNameField;				
	private JComboBox harbor;
	private JComboBox country;
	private JButton cancel;
	private JButton okay;
	private JLabel name;
	private JLabel harborName;
	private JLabel countryName;
	private ArrayList<String> shipNames;

	public NewShip(ArrayList<String> shipNames, ArrayList<String> harborNames,ArrayList<String> countryNames){

//		if(harborNames == null){
//
//			JOptionPane.showMessageDialog(this,"You haven't added any harbor yet.", "Error", JOptionPane.ERROR_MESSAGE);
//			setVisible(false);
//			return;			
//		}

		this.shipNames = shipNames;												// Copying all Ship names so its 
		// Accessible out side the constructor
		setLayout(null); 														// Empty Layout
		setSize(325,275);														// Setting the size of frame
		setTitle("New Ship");													// Setting the title of the frame

		shipNameField = new JTextField(15);										// Creating TextField
//		harbor = new JComboBox(harborNames.toArray());							// Adding all the Harbors into combobox
//		country = new JComboBox(countryNames.toArray());						// Adding all the Countries into Combobox
		harbor = new JComboBox(harborNames.toArray());
		country = new JComboBox(countryNames.toArray());
		cancel = new JButton("Cancel");											// Adding the cancel button
		okay = new JButton("Okay");												// Adding the okay button
		name = new JLabel("Ship Name :");										// Creating a Label for Ship Name
		harborName = new JLabel("Choose Harbor :");								// Creating a Label for Harbor
		countryName = new JLabel("Choose Country :");							// Creating a Label for Country

		// Sets bounds for Labels, combobox, and buttons  
		name.setBounds(25, 5, 250, 25);
		shipNameField.setBounds(25, 25, 250, 20);
		harborName.setBounds(25, 55, 250, 20);
		harbor.setBounds(25, 75, 250, 20);
		countryName.setBounds(25, 105, 250, 20);
		country.setBounds(25, 125, 250, 20);
		cancel.setBounds(25, 175, 110, 20);
		okay.setBounds(150, 175, 110, 20);

		// Adds everything to the frame
		add(name);
		add(shipNameField);
		add(harborName);
		add(harbor);
		add(countryName);
		add(country);
		add(cancel);
		add(okay);

		// implements the action listener
		cancel.addActionListener(this);
		okay.addActionListener(this);

		setVisible(true);
	}

	public static void main(String[] args){

		NewShip panel = new NewShip(null, null, null);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == cancel){
			setVisible(false);
		}
		else if(shipNames.contains(shipNameField.getText())){
			JOptionPane.showMessageDialog(this, "There exists a ship named : " + shipNameField.getText(), "Cloning", JOptionPane.ERROR_MESSAGE);
		}
		else if((String) harbor.getSelectedItem() == null){
			JOptionPane.showMessageDialog(this, "Select a Harbor", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		else if((String) country.getSelectedItem() == null){
			JOptionPane.showMessageDialog(this, "Select a Country", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		else{			
			WorldMap.addShip(new CivilianShip(shipNameField.getText(), (String) country.getSelectedItem(), (String) harbor.getSelectedItem()));
			setVisible(false);
		}
	}
}
