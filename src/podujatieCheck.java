
public class podujatieCheck {
	
	private String title;
	private int count;
	
	public podujatieCheck(String title){
		this.title = title;
		this.count = 1;
	}
	
	public void incCount(){
		this.count++;
	}
	
	public String getInfo(){
		return this.title+" - "+this.count;
	}
	
	public String getTitle() {
		return title;
	}
	
	public int getCount(){
		return this.count;
	}
	
}
