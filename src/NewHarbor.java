import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;

public class NewHarbor extends JFrame implements ActionListener{
	private JTextField harborNamesField, xCoord, yCoord;
	private JComboBox country;
	private JButton cancel;
	private JButton okay;
	private JLabel harborName;
	private JLabel countryName;
	private ArrayList<String> harborNames;
	private JLabel xCoordLabel, yCoordLabel;
	private Point coordinate;

	public NewHarbor(ArrayList<String> harborNames,ArrayList<String> countryNames){

		if(countryNames == null){

			JOptionPane.showMessageDialog(this,"You haven't added any countries yet.", "Error", JOptionPane.ERROR_MESSAGE);
			setVisible(false);
			return;			
		}

		this.harborNames = harborNames;
		setLayout(null); 

		setSize(325,275);
		setTitle("New Ship");

		harborNamesField = new JTextField(15);
		country = new JComboBox(countryNames.toArray());
		xCoord = new JTextField(5);
		yCoord = new JTextField(5);
		cancel = new JButton("Cancel");
		okay = new JButton("Okay");
		harborName = new JLabel("Enter New Harbor Name:");
		countryName = new JLabel("Choose Country :");
		xCoordLabel = new JLabel("X-Coodrinate");
		yCoordLabel = new JLabel("Y-Coodrinate");


		harborName.setBounds(25, 5, 250, 20);
		harborNamesField.setBounds(25, 25, 250, 20);
		countryName.setBounds(25, 55, 250, 20);
		country.setBounds(25, 75, 250, 20);
		xCoordLabel.setBounds(25, 105, 250, 20);
		xCoord.setBounds(25, 125, 110, 20);
		yCoordLabel.setBounds(150, 105, 250, 20);
		yCoord.setBounds(150, 125, 110, 20);
		cancel.setBounds(25, 175, 110, 20);
		okay.setBounds(150, 175, 110, 20);
		
		add(harborName);
		add(harborNamesField);
		add(countryName);
		add(country);
		add(xCoordLabel);
		add(xCoord);
		add(yCoordLabel);
		add(yCoord);
		add(cancel);
		add(okay);
		
		cancel.addActionListener(this);
		okay.addActionListener(this);

		setVisible(true);
	}

	public static void main(String[] args){

		NewHarbor panel = new NewHarbor(null, null);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == cancel){
			setVisible(false);
		}
		else if(Integer.parseInt(xCoord.getText()) > 50 || Integer.parseInt(yCoord.getText()) > 50){
			JOptionPane.showMessageDialog(this,	"X and Y have to be less than or equal to 50","Wrong Coodinates", JOptionPane.ERROR_MESSAGE);
		}
		else if(harborNames.contains(harborNamesField.getText())){
			JOptionPane.showMessageDialog(this, "There exists a harbor named : " + harborNamesField.getText(), "Cloning", JOptionPane.ERROR_MESSAGE);
		}
		else if((String) country.getSelectedItem() == null){
			JOptionPane.showMessageDialog(this, "Select a Country", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		else{
			coordinate = new Point(Integer.parseInt(xCoord.getText()),Integer.parseInt(xCoord.getText()));
			WorldMap.addHarbor(new Harbor(harborNamesField.getText(), (String) country.getSelectedItem(), coordinate));
		}
	}
}
