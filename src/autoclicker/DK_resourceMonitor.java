package autoclicker;

import java.io.IOException;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class DK_resourceMonitor {
	private int wood,clay,iron;
	private int warehouse;
	
	public DK_resourceMonitor(Map<String,String> cookies){
		
		try {
			Document overview = Jsoup.connect("https://cs48.divokekmeny.cz/game.php?village=12563&screen=overview")
					.cookies(cookies)
					.get();
			
			this.wood = Integer.parseInt(overview.getElementById("wood").text());
			this.clay = Integer.parseInt(overview.getElementById("stone").text());
			this.iron = Integer.parseInt(overview.getElementById("iron").text());
			this.warehouse = Integer.parseInt(overview.getElementById("storage").text());
			
		} catch (IOException e) {
			this.wood = -1;
			this.clay = -1;
			this.iron = -1;
			this.warehouse = -1;
		}
		
	}
	
}
