import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.ArrayList;

/**
 * Displays information regarding coutries' harbors, ship numbers, and overall
 * wealth.
 */
public class Stats extends JFrame implements ActionListener{
	private Connection myConnection;
	private final int FRAME_WIDTH = 300, FRAME_HEIGHT = 500;
	public Stats(){
		myConnection = Main.getConnection();

		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		JTextArea area = new JTextArea();
		area.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT - 30));

		try{
			Statement stm = myConnection.createStatement();
			ResultSet rs = stm.executeQuery("select * from country");
			ArrayList<Integer> countryList = new ArrayList<Integer>();
			ArrayList<String> countryNameList = new ArrayList<String>();
			while(rs.next()){
				countryList.add(rs.getInt("country_id"));
				countryNameList.add(rs.getString("country_name"));
			}
			for (int i = 0; i < countryList.size(); ++i){
				int totalCruisers = 0, totalDestroyers = 0, totalWorth = 0;
				rs = stm.executeQuery("select * from harbor where country_id = " + countryList.get(i));
				ArrayList<String> harborNames = new ArrayList<String>();
				while (rs.next()){
					harborNames.add(rs.getString("name"));
					totalCruisers += rs.getInt("num_cruisers");
					totalDestroyers += rs.getInt("num_destroyers");
					totalWorth += rs.getInt("harbor_bank");
					totalWorth += rs.getInt("num_fishers") * Fisher.getPrice();
					totalWorth += rs.getInt("num_barges") * Barge.getPrice();
				}

				rs = stm.executeQuery("select num_cruisers, num_destroyers from fleet where country_id = " + countryList.get(i));

				while (rs.next()){
					totalCruisers += rs.getInt("num_cruisers");
					totalDestroyers += rs.getInt("num_destroyers");
				}

				totalWorth += totalCruisers * Cruiser.getPrice();
				totalWorth += totalDestroyers * Destroyer.getPrice();

				area.append("Country Name: " + countryNameList.get(i) + "\n");
				area.append("\tTotal worth: " + totalWorth + "\n");
				area.append("\tTotal cruisers: " + totalCruisers + "\n");
				area.append("\tTotal destroyers: " + totalDestroyers + "\n");
				area.append("\tHarbors:\n");
				for (String name : harborNames){
					area.append("\t\t" + name + "\n");
				}

			}

			rs.close();
			stm.close();
		} catch (Exception e){
			System.out.println("Something went wrong when displaying stats.");
			e.printStackTrace();
		}

		JScrollPane pane = new JScrollPane(area);
		area.setEditable(false);
		add(pane);


		//Button
		JButton closeBtn = new JButton("Close");
		closeBtn.addActionListener(this);
		closeBtn.setActionCommand("close");

		add(closeBtn, BorderLayout.SOUTH);

		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent ae){
		String command = ae.getActionCommand();
		if (command.equals("close")){
			close();
		}
	}

	/**
	 * Closes the frame and enables the world map.
	 */
	private void close(){
		Main.enableWorldMap();
		dispose();
	}
}
