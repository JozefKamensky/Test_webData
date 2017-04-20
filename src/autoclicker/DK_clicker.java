package autoclicker;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

public class DK_clicker {

	public static void main(String args[]){
		
		final String prefix = "https://cs48.divokekmeny.cz";
		
		try {	
			
			//player for playing .wav notification
			BasicPlayer player = new BasicPlayer();
			
			//boolean var for audio on/off
			boolean audio = true;
			
			//open properties file with settings
			Properties prop = new Properties();
			prop.load(new FileInputStream("src/DK_config.properties"));
			
			//cavLimit is number of light cavalry that can be used for raids
			//cavPerRaid is number of light cavalry in one raid
			int cavLimit = Integer.parseInt(prop.getProperty("cavRaidLimit"));
			int cavPerRaid = Integer.parseInt(prop.getProperty("cavPerRaid"));
			
			//loginPeriodMinutes sets interval of logins to website in millis (calculated from minutes)
			//loginPeriodVar sets variable of interval of logins to website in millis (calculated from minutes)
			long loginPeriodMinutes = Integer.parseInt(prop.getProperty("loginPeriodMinutes")) * (60 * 1000);
			long loginPeriodVar = Integer.parseInt(prop.getProperty("loginPeriodVar")) * (60 * 1000);
			
			//attackPeriodSeconds sets interval of sending attack(raids) (calculated from seconds)
			//attackPeriodVar sets variable of interval per attack(raid) (calculated from seconds)
			long attackPeriodSeconds = Integer.parseInt(prop.getProperty("attackPeriodSeconds")) * 1000;
			long attackPeriodVar = Integer.parseInt(prop.getProperty("attackPeriodVar")) * 1000;
			
			//attackConfirmDelaySeconds sets time between sending and confirming attack(raid) (calculated from seconds)
			//attackConfirmDelayVar sets variable of time between sending and confirming attack(raid) (calculated from seconds)
			long attackConfirmDelaySeconds = Integer.parseInt(prop.getProperty("attackConfirmDelaySeconds")) * 1000;
			long attackConfirmDelayVar = (long) (Double.parseDouble(prop.getProperty("attackConfirmDelayVar")) * 1000);
			
			//initialDelaySeconds sets delay between login and sending attacks
			//initialDealyVar sets variable of delay time
			long initialDelaySeconds = Integer.parseInt(prop.getProperty("initialDelaySeconds")) * 1000;
			long initialDelayVar = Integer.parseInt(prop.getProperty("initialDelayVar")) * 1000;
			
			//storageAlert sets ratio between resources and warehouse capacity - if exceeded, user will be alerted
			double storageAlert = Double.parseDouble(prop.getProperty("storageAlert"));
			
			DK_raidSelector slct = new DK_raidSelector();
			long time;
			
			while(true) {
				Map<String,String> cookies = loginToDK();
				int c = getCavalryNumber(cookies);
				int light_cavalry = ( c > cavLimit)? cavLimit : c;
				
				time = calculateTime(initialDelaySeconds,initialDelayVar);
				System.out.println("Debug: Initial delay: "+time/1000+" sec.");
				Thread.sleep(time);
				
		        while (light_cavalry >= cavPerRaid){
		        	DK_target target = slct.selectTarget();
		        	sendRaidAttack(cavPerRaid, target.getX(), target.getY(), cookies, calculateTime(attackConfirmDelaySeconds,attackConfirmDelayVar));
		        	
		        	time = calculateTime(attackPeriodSeconds,attackPeriodVar);
		        	double t = time/1000;
					System.out.println("Debug: Delay between attacks: "+t+" sec.");
					Thread.sleep(time);

		        	light_cavalry -= cavPerRaid;
		        }    
		        
		        time = calculateTime(loginPeriodMinutes,loginPeriodVar);
				System.out.println("Debug: Delay between logins: "+time/(1000*60)+" min.");
				Thread.sleep(time);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static long calculateTime(long time,long var){
		Random rng = new Random();
		return time + rng.nextLong()%(2*var) - var;
	} 
	
	private static Map<String,String> loginToDK(){
		try {
			Connection.Response loginForm = Jsoup.connect("https://www.divokekmeny.cz/index.php?action=login&server_cs48")
					.data("user","deadsoul")
			        .data("password", "e3a0e3d0d88ebf65c61243b08b15a471186c3d0f")
			        .data("sso", "0")
			        .method(Connection.Method.POST)
			        .followRedirects(true)
			        .execute();
			System.out.println("Debug: Login successful.");
			return loginForm.cookies();
		} catch (IOException e) {
			return null;
		}
	}
	
	private static boolean sendAttack(int[] units, int x, int y, Map<String,String> cookies, long time){
			
			final String url = "https://cs48.divokekmeny.cz/game.php?village=12563&screen=place&try=confirm";
			final String prefix = "https://cs48.divokekmeny.cz";
			
			try {
				Document place = Jsoup.connect(url)
						.cookies(cookies)
						.get();
				
				Element attackForm = place.getElementById("command-data-form");
				Elements hidden = attackForm.select("input[type=hidden]");
				
				/*for(Element e: hidden){
					System.out.println(e.attr("name")+" = "+e.attr("value"));
				}*/
				
				Document placeAttack = Jsoup.connect(url)
						.cookies(cookies)
						.data(hidden.get(0).attr("name"),hidden.get(0).attr("value"))
						.data(hidden.get(1).attr("name"),hidden.get(1).attr("value"))
						.data(hidden.get(2).attr("name"),hidden.get(2).attr("value"))
						.data("spear",Integer.toString(units[0]))
						.data("sword",Integer.toString(units[1]))
						.data("axe",Integer.toString(units[2]))
						.data("archer",Integer.toString(units[3]))
						.data("spy",Integer.toString(units[4]))
						.data("light",Integer.toString(units[5]))
						.data("marcher",Integer.toString(units[6]))
						.data("heavy",Integer.toString(units[7]))
						.data("ram",Integer.toString(units[8]))
						.data("catapult",Integer.toString(units[9]))
						.data("knight","0")
						.data("snob","0")
						.data("x",Integer.toString(x))
						.data("y",Integer.toString(y))
						.data("target_type","coord")
						.data("attack","Útok")
						.followRedirects(true)
						.post();
				
				Element attackConfirmForm = placeAttack.getElementById("command-data-form");
				Elements hidden2 = attackConfirmForm.select("input[type=hidden]");
				
				Thread.sleep(time);
				
				Jsoup.connect(prefix+attackConfirmForm.attr("action"))
						.cookies(cookies)
						.data("attack","true")
						.data(hidden2.get(1).attr("name"),hidden2.get(1).attr("value"))
						.data(hidden2.get(2).attr("name"),hidden2.get(2).attr("value"))
						.data(hidden2.get(3).attr("name"),hidden2.get(3).attr("value"))
						.data(hidden2.get(4).attr("name"),hidden2.get(4).attr("value"))
						.data(hidden2.get(5).attr("name"),hidden2.get(5).attr("value"))
						.data(hidden2.get(6).attr("name"),hidden2.get(6).attr("value"))
						.data(hidden2.get(7).attr("name"),hidden2.get(7).attr("value"))
						.data(hidden2.get(8).attr("name"),hidden2.get(8).attr("value"))
						.data(hidden2.get(9).attr("name"),hidden2.get(9).attr("value"))
						.data(hidden2.get(10).attr("name"),hidden2.get(10).attr("value"))
						.data(hidden2.get(11).attr("name"),hidden2.get(11).attr("value"))
						.data(hidden2.get(12).attr("name"),hidden2.get(12).attr("value"))
						.data(hidden2.get(13).attr("name"),hidden2.get(13).attr("value"))
						.data(hidden2.get(14).attr("name"),hidden2.get(14).attr("value"))
						.data(hidden2.get(15).attr("name"),hidden2.get(15).attr("value"))
						.data(hidden2.get(16).attr("name"),hidden2.get(16).attr("value"))
						.data(hidden2.get(17).attr("name"),hidden2.get(17).attr("value"))
						.data("building","main")
						.method(Connection.Method.POST)
						.execute();
				System.out.println("Debug: Attack sent.");
				return true;
			} catch (IOException | IndexOutOfBoundsException | InterruptedException e) {
				System.out.println("Debug: Error.");
				return false;
			}
		}
	
	private static void sendSpyAttack(int spy, int x, int y, Map<String,String> cookies, long time){
		
		int[] units = {0,0,0,0,0,0,0,0,0,0};
		
		units[4] = spy;
		
		sendAttack(units, x, y, cookies, time);
		
	}
	
	private static void sendRaidAttack(int light, int x, int y, Map<String,String> cookies, long time){
		
		int[] units = {0,0,0,0,0,0,0,0,0,0};
		
		units[5] = light;
		
		System.out.println("Debug: Sending attack at "+x+"|"+y);
		sendAttack(units, x, y, cookies, time);
		
	}
	
	private static int getCavalryNumber(Map<String,String> cookies){
		
		Document overview;
		try {
			overview = Jsoup.connect("https://cs48.divokekmeny.cz/game.php?village=12563&screen=overview")
	        		.cookies(cookies)
	        		.get();
	        
	        Elements unitsInfo = overview.getElementById("show_units").select("td");
	        Element cavInfo = unitsInfo.select("a[data-unit=light]").first();
	        String cav = cavInfo.parent().select("strong").text();
	        
	        System.out.println("Debug: cavalry: "+cav);
	        int count = Integer.parseInt(cav);
	        
	        System.out.println("Debug: Number of light cavalry: "+count);	        
	        return count;
	        
	        /*for(int i = 0; i < unitsInfo.size()-3;i++){
	        	Element e = unitsInfo.get(i);
	        	String name = e.select("a").attr("data-unit");
	        	int count = Integer.parseInt(e.select("strong").text());
	        	
	        	units.add(new DK_unit(name,count));
	        }*/
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		} catch (NumberFormatException e){
			e.printStackTrace();
			return 0;
		}
		
	}
	
}
