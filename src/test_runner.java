import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class test_runner {

	public static void main (String args[]){
		
		final String prefix = "http://slovakia.travel/";
		
		try {
			Document doc = Jsoup.connect("http://slovakia.travel/podujatia/kalendar-podujati").get();
			//System.out.println("Pripajam sa na SlovakTravel.");
			Elements pages = doc.select("div[class=\"pagination-centered\"]").select("ul").select("a[href]");
			//System.out.println("Linky na vsetky stranky.");
			Elements links = new Elements();
			links.addAll(doc.select("li.clearfix,li.clearfix last").select("h3").select("a[href]"));
			for (Element p: pages){
				//System.out.println("Prechadzam stranky.");
				Document page = Jsoup.connect(prefix+p.attr("href")).get();
				links.addAll(page.select("li.clearfix,li.clearfix last").select("h3").select("a[href]"));
				//System.out.println("Pridavam podujatia.");
			}
			
			DateFormat datef = new SimpleDateFormat("dd-MM-yyyy");
			Date date = new Date();
			PrintWriter out = new PrintWriter("podujatia"+datef.format(date)+".txt");
			int cycle = 1;
			
			for (Element e: links) {
				System.out.println(cycle);
				try{
				Document info = Jsoup.connect(prefix+e.attr("href")).get();
				
				Element header = info.select("div[class=\"s-object-header clearfix\"").select("h1").first();
				out.println(header.text());
				
				Elements infos = info.select("div[class=\"data\"]").select("p");
				
				for (Element i: infos){					
					out.println(i.text());
				}
				cycle++;
				}
				catch (SocketTimeoutException e2){
					e2.printStackTrace();
				}
			}
			
			out.close();
			System.out.println("Koniec.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
