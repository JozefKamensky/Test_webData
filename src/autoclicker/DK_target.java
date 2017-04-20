package autoclicker;

public class DK_target {

	private int x,y;
	private boolean full;
	
	public DK_target(int x, int y){
		this.x = x;
		this.y = y;
		this.full = false;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isFull() {
		return full;
	}

	public void setFull(boolean full) {
		this.full = full;
	}
	
}
