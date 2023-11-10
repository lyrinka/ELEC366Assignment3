package elec366.assignment3.network.sdu;

public class SDU implements Comparable<SDU> {

	public static enum Priority {
		VERY_HIGH	(200), 
		HIGH		(175), 
		MEDIUM		(150), 
		LOW			(125), 
		VERY_LOW	(100), 
		; 
		
		private final int priority;

		private Priority(int priority) {
			this.priority = priority;
		}

		public int getPriorityLevel() {
			return this.priority;
		} 
		
	}
	
	private final int priority; 
	
	public SDU(int priority) {
		this.priority = priority; 
	}
	
	public SDU(Priority priority) {
		this(priority.getPriorityLevel()); 
	}
	
	public SDU() {
		this(Priority.MEDIUM); 
	}

	@Override
	public int compareTo(SDU other) {
		return Integer.compare(this.priority, other.priority); 
	}

}
