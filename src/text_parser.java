
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class text_parser {
	
	public static void main (String args[]){
		break_lines("typy.txt","typy_upravene.txt");
		List<podujatieCheck> podujatia = countPodujatie("typy_upravene.txt");
		int eventNumber = 0;
		for (podujatieCheck p: podujatia){
			eventNumber += p.getCount();
			System.out.println(p.getInfo());
		}
		System.out.println("Number of events: "+eventNumber);
	}
	
	public static void break_lines(String input, String output){
		
		
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(input));
			PrintWriter out = new PrintWriter(output);
			String line = in.readLine();
			int eventNumber = 0;
			
			while (line != null){
				int endIndex = line.indexOf(", ");
				int startIndex = 0;
				
				while(true){
					if (startIndex >= endIndex) {
						out.println(line.substring(startIndex));
						eventNumber++;
						break;
					}
					//System.out.println("Zhoda: "+startIndex+"-"+(startIndex+endIndex)+" ("+line.substring(startIndex,endIndex)+")");
					out.println(new String(line.substring(startIndex,endIndex)));
					startIndex = endIndex + 2;
					endIndex = startIndex + line.substring(startIndex).indexOf(", ");
					//System.out.println("Indexy: "+startIndex+" a "+(startIndex+endIndex));
					eventNumber++;
					
				}
				line = in.readLine();
			}
			in.close();
			out.close();
			//System.out.println("Event types found: "+eventNumber);
			
		} catch (IOException | StringIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		
	}
	
	public static List<podujatieCheck> countPodujatie(String inputFile){
		
		List<podujatieCheck> result = new ArrayList<podujatieCheck>();
		try {
			BufferedReader bf = new BufferedReader(new FileReader(inputFile));
			String line = bf.readLine();
			int lineCount = 1;
			
			while(line != null){
				boolean match = false;
				for (podujatieCheck p : result){
					if (line.compareTo(p.getTitle()) == 0){
						match = true;
						p.incCount();
						break;
					}
				}
				if (!match) result.add(new podujatieCheck(line));
				line = bf.readLine();
				lineCount++;
			}
			
			//System.out.println("Number of lines: "+lineCount);
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
}
