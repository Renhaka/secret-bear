import java.awt.event.KeyEvent;
import java.io.IOException;
//import java.io.IOException;
import java.sql.*;

public class DatabaseHandler 
{
	Connection main;
	MainWindow _parent;
	
	public DatabaseHandler(MainWindow parent)
	{
		_parent = parent;
		try
		{
			Class.forName("org.sqlite.JDBC");
			main = DriverManager.getConnection("jdbc:sqlite:sample.db");
		}
		catch (Exception e)
		{
			MainWindow.infoBox(e.getMessage(), "Call Zach!");
			System.exit(0);
		}
		try { _parent.tossNotification("Successful SQL Database Connect"); } catch (IOException e) { e.printStackTrace(); }
		try
		{
			firstRun();
			try { _parent.tossNotification("Successful SQL First Run Table Generation"); } catch (IOException e) { e.printStackTrace(); }
		}
		catch (Exception e)
		{
			MainWindow.infoBox(e.getMessage(), "Call Zach!");
		}
	}

	public String checkUserCode(String text, boolean failure)
	{
		System.out.println("Checking Product Code  " + KeyEvent.VK_A + "  " + KeyEvent.VK_Z);
		if(failure)
		{
			return null;
		}
		else
		{
			return "Sample Correct";
		}
	}
	
	public void firstRun() throws SQLException, IOException
	{
		Statement run = main.createStatement();
		run.executeUpdate("CREATE TABLE IF NOT EXIST main.items (id INTEGER PRIMARY KEY NOT NULL, name VARCHAR( 45 )  NOT NULL, permissions INTEGER NOT NULL, generic INTEGER, FOREIGN KEY ( permissions ) REFERENCES inPermissions ( id ), FOREIGN KEY ( generic ) REFERENCES generic ( id ));");
		run.close();
	}
}
