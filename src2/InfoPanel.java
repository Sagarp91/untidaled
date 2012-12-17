import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;

/**
 * Displays information about a harbor that is being hovered over.
 */
public class InfoPanel extends JPanel{
	private final Color FONT_COLOR = Color.BLACK;
	public static String harborName;
	public static String countryName;
	
	public InfoPanel(){
		harborName = "";
		countryName = "";
	}

	public void paint(Graphics g){
		g.setColor(FONT_COLOR);
		int width = getWidth();
		g.setFont(new Font("Arial", 1, 20));
		g.drawString("Harbor:", 10, 20);
		g.drawString("Country:", width / 2, 20);
		g.setFont(new Font("Arial", 0, 20));
		g.drawString(harborName, 30, 45);
		g.drawString(countryName, width / 2 + 20, 45);
	}
}
