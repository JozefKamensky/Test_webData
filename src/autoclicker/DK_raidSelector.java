package autoclicker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DK_raidSelector {
	
	private List<DK_target> targets = new ArrayList<DK_target>();
	private int lastTarget = 0;	
	private int cavLimit;
	private int cavPerRaid;
	
	public DK_raidSelector(){
		loadTargets("raidTargets.txt");
	}
	
	public void loadTargets(String file){
		
		try {
			BufferedReader bf = new BufferedReader(new FileReader(file));
			String line;
			
			while((line = bf.readLine()) != null){
				int x = Integer.parseInt(line.substring(0, line.indexOf("|")));
				int y = Integer.parseInt(line.substring(line.indexOf("|")+1));
				targets.add(new DK_target(x,y));
			}
			bf.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public DK_target selectTarget(){
			
		DK_target target = targets.get(lastTarget);
		lastTarget++;
		if (lastTarget >= targets.size()) lastTarget = 0;
			
		return target;
			
	}
	
	public void processNewReports(Map<String,String> cookies){
		
		try {
			Document overview = Jsoup.connect("https://cs48.divokekmeny.cz/game.php?village=12563&screen=overview")
					.cookies(cookies)
					.get();
			
			Element reportLabel = overview.getElementById("menu_report_count");
			int numberOfNewReports = Integer.parseInt(reportLabel.text());
			
			Document reportPage = Jsoup.connect("https://cs48.divokekmeny.cz/game.php?village=12563&screen=report&mode=attack")
					.cookies(cookies)
					.get();
			Elements reports = reportPage.select("td[style=\"overflow: hidden\"]");
			
			for(int i = numberOfNewReports; i > 0; i--){
				Document r = Jsoup.connect(reports.get(i).select("a").attr("href"))
						.cookies(cookies)
						.get();
				
				String loot = r.getElementById("attack_results").select("td").get(1).text();
				
				int resources = Integer.parseInt(loot.substring(0, loot.indexOf('/')).replace(".", ""));
				int capacity = Integer.parseInt(loot.substring(loot.indexOf('/')+1).replace(".", ""));
				
				//ziska suradnice
				String target = "pozriet tag";
				int x;
				int y;
				
				if (resources == capacity){
					//zmenit v targets full na true
					//sort na zakladne boolean
				}
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e){
			return;
		}
		
	}
	
	/*public void addTarget(DK_spyResult res){
		
		System.out.println("Add spy result.");
		
		boolean match = false;
		
		for (DK_target t: spiedTargets){
			if ( (t.getX() == res.getX()) && (t.getY() == res.getY()) ) {
				match = true;
				t.setResources(res.getResources());
				t.setWall(res.getWall());
				t.setUnits(res.isUnits());
				t.setState(false);
			}
			
			if(match) break;
			
		}
		
		if(!match) {
			spiedTargets.add(new DK_target(res.getX(),res.getY(),res.getName(),false,res.isUnits(),res.getResources(),res.getWall()));
			System.out.println("Spy added.");
		}
		
		Collections.sort(spiedTargets,new Comparator<DK_target>() {
			@Override public int compare(DK_target t1, DK_target t2){
				return t2.getResources() - t1.getResources();
			}
		});
		
	}*/
	
	/*public void addTarget(DK_result res){
		
		System.out.println("Delete target.");
		
		int i = 0;
		for (DK_target t: spiedTargets){
			if ( (t.getX() == res.getX()) && (t.getY() == res.getY()) ) {
				spiedTargets.remove(i);
				break;
			}
			i++;
		}		
		
		i = 0;
		for (DK_target t: unknownTargets){
			if ( (t.getX() == res.getX()) && (t.getY() == res.getY()) ) {
				unknownTargets.remove(i);
				break;
			}
			i++;
		}		
		
	}*/
	
	/*public DK_target selectSpyTarget(){
		
		outer_loop : for(DK_target t : unknownTargets){
			
			for (DK_target t2 : spiedTargets){
				
				if ( (t.getX() == t2.getX()) && (t.getY() == t2.getY()) ){
					continue outer_loop;
				}
				
			}
			
			return new DK_target(t.getX(), t.getY());
			
		}
	
		return null;
		
	}*/
	
	/*public static int[] calculateUnitNumbers(List<DK_unit> units, int value){
		
		int infantryCargo = 0;
		int cavalryCargo = 0;
		int order[] = {5,6,7,0,2,3,1,8,9};
		
		int result[] = {0,0,0,0,0,0,0,0,0,0};
		
		for(DK_unit u: units){
			if (u.isInfantry()) infantryCargo += u.getCount()*u.getCargo();
			else cavalryCargo += u.getCount()*u.getCargo();
		}
		
		// only attack with cavalry units
		if (cavalryCargo >= value){
			int c = 0;
			outer_loop : for(int i = 0;i < 3; i++){
				for(int j = 1; j <= units.get(order[i]).getCount(); j++){
					c += units.get(order[i]).getCargo();
					if (c >= value){
						result[order[i]] = j;
						break outer_loop;
					}
					if (j == units.get(order[i]).getCount()) result[order[i]] = j;
				}
			}
		}
		
		//only attack with infantry units
		else if (infantryCargo >= value){
			int c = 0;
			outer_loop : for(int i = 3;i < 9; i++){
				for(int j = 1; j <= units.get(order[i]).getCount(); j++){
					c += units.get(order[i]).getCargo();
					if (c >= value){
						result[order[i]] = j;
						break outer_loop;
					}
					if (j == units.get(order[i]).getCount()) result[order[i]] = j;
				}
			}
		}
		
		//attack with combination of infantry and cavalry
		else {
			int c = 0;
			outer_loop : for(int i = 0;i < 9; i++){
				for(int j = 1; j <= units.get(order[i]).getCount(); j++){
					c += units.get(order[i]).getCargo();
					if (c >= value){
						result[order[i]] = j;
						break outer_loop;
					}
					if (j == units.get(order[i]).getCount()) result[order[i]] = j;
				}
			}
		}
		
		return result;
	}*/	

}
