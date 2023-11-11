package elec366.assignment3.network.sdu;

public class BaseSDU implements Comparable<BaseSDU> {

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
	
	public BaseSDU(int priority) {
		this.priority = priority; 
	}
	
	public BaseSDU(Priority priority) {
		this(priority.getPriorityLevel()); 
	}
	
	public BaseSDU() {
		this(Priority.MEDIUM); 
	}

	@Override
	public int compareTo(BaseSDU other) {
		return Integer.compare(this.priority, other.priority); 
	}

}
