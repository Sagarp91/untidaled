import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class Search_GUI extends JFrame implements ActionListener {

	//create the GUI components for this GUI
	private JButton searchButton;			
	private JComboBox countryList;		
	private JComboBox harborList;		
	private JComboBox shipClassList;

	private ArrayList<Harbor> harborLists;		//create the harbor list
	private JTextArea infoList;					//create the text area to print results
	private static final String[] ship= {"Ship Class", "War Ship", "Civilian Ship"};	//create the string for ship JComboBox
	private ArrayList<String> printList;		//List we print in the text field 


	public Search_GUI(ArrayList<Harbor> harborLists, ArrayList<String>
countryNames) { 

		setLayout(null);	//no layout manager. we are going to set the bounds using hard code
		setSize(600, 500);	//set the size of the panel
		setTitle("Search Menu");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		this.harborLists = harborLists; // the harbor list will be the same



		searchButton = new JButton("Search");	// create the search button
		ArrayList<String> harborNames = new ArrayList<String>();	//create the harbor list and put them into the array

		for (Harbor hb : harborLists){
			harborNames.add(hb.getName());
		}

		//add the initial value to the list so they can be displayed
		harborNames.add(0, "Harbors");
		countryNames.add(0, "Country");
		//create the comboBoxes and the text area
		harborList = new JComboBox(harborNames.toArray());
		countryList = new JComboBox(countryNames.toArray());
		shipClassList = new JComboBox(ship);
		infoList = new JTextArea(harborNames.size(), 4);
		infoList.setTabSize(10);
		infoList.setEditable(false);
		//create the bounds
		searchButton.setBounds(425, 30, 130, 30);
		harborList.setBounds(10,30,100,25);
		countryList.setBounds(155, 30, 100, 25);
		shipClassList.setBounds(285, 30, 100, 25);
		infoList.setBounds(10, 110, 565, 325);

		infoList.append("Name\tHarbor\tCountry\tShip Class\n");

		//add a listener to the search button
		searchButton.addActionListener(this);


		//add the GUI components to the JFrame
		add(searchButton);
		add(harborList);
		add(countryList);
		add(shipClassList);
		add(infoList);

		setVisible(true);
	}


	public static void main(String[] args) {
		ArrayList<Harbor> hbList = new ArrayList<Harbor>();
		ArrayList<String> countries = new ArrayList<String>();
		Harbor hb = new Harbor("someHarbor", "someCountry", new Point(1,1));
		hb.addShip(new CivilianShip("SSH", "someCountry", "someHarbor"));
		hbList.add(hb);
		countries.add("someCountry");

		Harbor hb1 = new Harbor("someHarb", "someCount", new Point(2,2));
		hb1.addShip(new CivilianShip("SH", "someCount", "someHarb"));
		hbList.add(hb1);
		countries.add("someCount");

		Search_GUI application = new Search_GUI(hbList, countries);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		//if the search button is pushed
		if(e.getSource()==searchButton)
		{
			infoList.setText("");
			infoList.append("Name\tHarbor\tCountry\tShip Class\n");
			ArrayList<Ship> toAdd = new ArrayList<Ship>();

			for (Harbor hb : harborLists){
				if (harborList.getSelectedIndex() != 0){
					if (hb.getName().equals((String)(harborList.getSelectedItem()))){
						for (Ship sh : hb.getShipList()){
							toAdd.add(sh);
						}
						break;
					}
				} else{
					for (Ship sh : hb.getShipList()){
						toAdd.add(sh);
					}
				}
			}

			for (Ship sh : toAdd){
				if (countryList.getSelectedIndex() != 0){
					if (!sh.getCountry().equals((String)(countryList.getSelectedItem()))){
						toAdd.remove(sh);
					}
				}
			}

			for (Ship sh : toAdd){
				infoList.append(sh.getName() + "\t");
				infoList.append(sh.getCurHarbor() + "\t");
				infoList.append(sh.getCountry() + "\t");
				infoList.append(sh.getShipClass() + "\t");
				infoList.append("\n");
			}

			/*
			if(harborList.getSelectedIndex()==0 && countryList.getSelectedIndex()==0 &&
shipClassList.getSelectedIndex()==0)
			{
				for(Harbor hb: harborLists)
				{
					for(Ship sh: hb.getShipList())
					{
						infoList.append(sh.getName() + "\t");
						infoList.append(sh.getCurHarbor() + "\t");
						infoList.append(sh.getCountry() + "\t");
						infoList.append(sh.getShipClass() + "\t ");



						infoList.append("\n");
					}
				}
			}
			for(Harbor hb: harborLists)
			{
				for(Ship sh: hb.getShipList())
				{
					if(harborList.getSelectedIndex()==0 && countryList.getSelectedIndex()==0
&& shipClassList.getSelectedIndex()==0)
					{

						infoList.append(sh.getName() + "\t");
						infoList.append(sh.getCurHarbor() + "\t");
						infoList.append(sh.getCountry() + "\t");
						infoList.append(sh.getShipClass() + "\t ");
						infoList.append("\n");
					}

					else if(harborList.getSelectedItem() == sh.getCurHarbor() &&
countryList.getSelectedItem() == sh.getCountry() &&
shipClassList.getSelectedItem() == sh.getShipClass()){
						infoList.append(sh.getName() + "\t");
						infoList.append(sh.getCurHarbor() + "\t");
						infoList.append(sh.getCountry() + "\t");
						infoList.append(sh.getShipClass() + "\t ");
						infoList.append("\n");	
					}
				}
			}
			*/
		}			



		//			if(harborList.getSelectedIndex() ==0)
		//			{
		//				for(Harbor hb: harborLists )
		//				{
		//					for(Ship sh: hb)
		//					{
		//						
		//					}
		//				}
		//			}
	}

}

