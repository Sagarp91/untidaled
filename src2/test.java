import java.sql.*;
/**
 * Used to test various classes and methods. Probably should use JUnit instead.
 */
public class test{
	public static void main(String[] args){
		//testDialogues();
		displayCountry();
		//clearTables();
	}

	public static void clearTables(){
		try{
			Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
			Connection myConnection = DriverManager.getConnection("jdbc:derby:database/untidaled", "mismatch", "mismatch");

			Statement stm = myConnection.createStatement();
			stm.execute("delete from country");
			stm.execute("delete from harbor");
			stm.execute("delete from fleet");
		} catch(Exception e){
			System.out.println("Could not connect to database.");
		}
	}

	public static void displayCountry(){
		try{
			Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
			Connection myConnection = DriverManager.getConnection("jdbc:derby:database/untidaled", "mismatch", "mismatch");

			Statement stm = myConnection.createStatement();
			ResultSet rs = stm.executeQuery("select * from country");
			while (rs.next()){
				System.out.println(rs.getString("country_name") + '\t' + rs.getInt("country_id"));
			}
		} catch(Exception e){
			System.err.println("Could not connect to database.");
		}
	}

	public static void testDialogues(){
		try{
			Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
			Connection myConnection = DriverManager.getConnection("jdbc:derby:database/untidaled", "mismatch", "mismatch");

			NewCountry nc = new NewCountry(myConnection);
		} catch(Exception e){
			System.err.println("Could not connect to database.");
		}

	}
}
