import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;

public class NewCountry extends JFrame implements ActionListener{
	private JTextField countryNamesField;
	private JButton cancel;
	private JButton okay;
	private JLabel countryName;
	private ArrayList<String> countryNames;

	public NewCountry(ArrayList<String> countryNames){

		this.countryNames = countryNames;
		setLayout(null); 

		setSize(325,275);
		setTitle("New Ship");

		countryNamesField = new JTextField(15);
		cancel = new JButton("Cancel");
		okay = new JButton("Okay");
		countryName = new JLabel("Enter a Country :");

		countryName.setBounds(25, 55, 250, 20);
		cancel.setBounds(25, 175, 110, 20);
		okay.setBounds(150, 175, 110, 20);
		
		add(countryName);
		add(cancel);
		add(okay);
		
		cancel.addActionListener(this);
		okay.addActionListener(this);

		setVisible(true);
	}

	public static void main(String[] args){
		NewCountry panel = new NewCountry(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == cancel){
			setVisible(false);
		}
		else if(countryNames.contains(countryNamesField.getText())){
			JOptionPane.showMessageDialog(this, "There exists a country named : " + countryNamesField.getText(), "Cloning", JOptionPane.ERROR_MESSAGE);
		}
		else{
			countryNames.add(countryNamesField.getText());
		}
	}
}
