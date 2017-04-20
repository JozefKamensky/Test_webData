import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class browser_game_runner {

	public static void main(String args[]){
		
		final int count = 10000;
		final int cycles = 10000;
		
		Properties prop = new Properties();
		loadProperties(prop);
		/*prepareDatabase(prop);
		generateUsernames(count,prop);*/
		List<User> users = loadUsers(count,prop);
		printInfoAbouUsers(users);
		
		for (int i = 0; i < cycles; i++){
			updateUsersResources(users);
		}
		
		printInfoAbouUsers(users);
		
		}
		
	private static void generateUsernames(int count, Properties prop){
		if (!prop.equals(null)){
		try{
			browser_game_generator bw = new browser_game_generator();	
			//Connection con = DriverManager.getConnection(prop.getProperty("url"), prop.getProperty("username"), prop.getProperty("password"));
			Connection con = DriverManager.getConnection(prop.getProperty("url"));

			for (int i = 0;i < count;i++){
				PreparedStatement stm = con.prepareStatement(bw.generateQuery());
				stm.executeUpdate();
			}
			con.close();
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private static void loadProperties(Properties prop){
		final String file = "src/config.properties";
		try {
			prop.load(new FileInputStream(file));
		} catch (IOException e) {
			e.printStackTrace();
			prop = null;
		}	
	}
	
	private static void prepareDatabase(Properties prop){
		try {
			//Connection con = DriverManager.getConnection(prop.getProperty("url"), prop.getProperty("username"), prop.getProperty("password"));
			Connection con = DriverManager.getConnection(prop.getProperty("url"));
			PreparedStatement stm = con.prepareStatement(prop.getProperty("clear_table"));
			stm.executeUpdate();
			PreparedStatement stm2 = con.prepareStatement(prop.getProperty("init_table"));
			stm2.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static List<User> loadUsers(int count, Properties prop){
		if (!prop.equals(null)) {
			try {
				Connection con = DriverManager.getConnection(prop.getProperty("url"));
				Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery("SELECT * FROM users");
				List<User> users = new LinkedList<User>();

				while (rs.next()) {
					User usr = new User(rs.getInt(1), rs.getString(2));
					usr.loadResources(rs.getDouble(3), rs.getDouble(4), rs.getDouble(5), rs.getDouble(6));
					usr.loadProduction(rs.getDouble(7), rs.getDouble(8), rs.getDouble(9), rs.getDouble(10));
					usr.loadBuildings(rs.getDouble(11), rs.getDouble(12), rs.getDouble(13), rs.getDouble(14),
							rs.getDouble(15), rs.getDouble(16));
					users.add(usr);
				}

				rs.close();
				stm.close();
				con.close();

				return users;

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		else return null;
	}
	
	private static void printInfoAbouUsers(List<User> users){
		System.out.println("Number of Users: "+users.size());
		System.out.println("\n---First User---\n"+users.get(0).getUserInfo());
	}
	
	private static void updateUsersResources(List<User> usr){
		long time = System.currentTimeMillis();
		for(User u: usr){
			u.updateResources(time);
		}
	}
	
}
