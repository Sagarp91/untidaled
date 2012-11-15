import javax.swing.JFrame;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class Main{
	public static void main(String[] args){
		WorldMap map;
		JFrame frm = new JFrame();
//		frm.setVisible(true);


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
}
