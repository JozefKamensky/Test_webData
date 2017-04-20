
public class User {
	private int id;
	private String username;
	private double wood,clay,iron,food;
	private double w_prod,c_prod,i_prod,f_prod;
	private double b1,b2,b3,b4,b5,b6,b7;
	
	public User(int id, String username){
		this.id = id;
		this.username = username;
	}
	
	public void loadResources(double wood, double clay, double iron, double food){
		this.wood = wood;
		this.clay = clay;
		this.iron = iron;
		this.food = food;
	}
	
	public void loadProduction(double w_prod,double c_prod,double i_prod,double f_prod){
		this.w_prod = w_prod;
		this.c_prod = c_prod;
		this.i_prod = i_prod;
		this.f_prod = f_prod;
	}
	
	public void loadBuildings(double b1,double b2,double b3,double b4,double b5,double b6){
		this.b1 = b1;
		this.b2 = b2;
		this.b3 = b3;
		this.b4 = b4;
		this.b5 = b5;
		this.b6 = b6;
	}
	
	public String getUserInfo(){
		StringBuilder sb = new StringBuilder();
		sb.append("Username: "+this.username+"\n");
		sb.append("Wood: "+this.wood+"\n");
		sb.append("Clay: "+this.clay+"\n");
		sb.append("Iron: "+this.iron+"\n");
		sb.append("Food: "+this.food+"\n");
		sb.append("Buildings: ");
		sb.append(this.b1+" ");
		sb.append(this.b2+" ");
		sb.append(this.b3+" ");
		sb.append(this.b4+" ");
		sb.append(this.b5+" ");
		sb.append(this.b6+"\n");
		return sb.toString();
	}
	
	public void updateResources(long time){
		long currTime = System.currentTimeMillis();
		wood += (w_prod / 3600000) * (currTime - time);
		clay += (c_prod / 3600000) * (currTime - time);
		iron += (i_prod / 3600000) * (currTime - time);
		food += (f_prod / 3600000) * (currTime - time);
	}
}
