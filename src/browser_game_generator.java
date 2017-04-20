import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class browser_game_generator {

	static final String query1 = "INSERT INTO users (username) VALUES('";
	
	private static final String first_names[] = {"Iron","Dead","Brave","Crazy","Sneaky","Lion","Deadly","Mighty","Awesome"};
	private static final String second_names[] = {"foot","soul","heart","devil","mind","dude","king","SVK"};
	private static final Integer number_limit = 821;
	private static Random rand = new Random();

	public static String select_first_name(){
		return first_names[rand.nextInt(first_names.length - 1)];
	}
	
	public static String select_second_name(){
		return second_names[rand.nextInt(second_names.length - 1)];
	}	
	
	public static String generate_name(){
		return select_first_name()+"_"+select_second_name()+rand.nextInt(number_limit);
	}
	
	public String generateQuery(){
		return query1+generate_name()+"')";
	}
	
}
