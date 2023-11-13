package elec366.assignment3.util;

public class Flag {

	private boolean flag; 
	
	public Flag() {
		this(false); 
	}
	
	public Flag(boolean flag) {
		this.flag = flag; 
	}

	public boolean getFlag() {
		return this.flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}
