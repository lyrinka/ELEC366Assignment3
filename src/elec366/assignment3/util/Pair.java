package elec366.assignment3.util;

public class Pair <U, V> {

	private final U first; 
	private final V second; 
	
	public Pair(U first, V second) {		
		this.first = first; 
		this.second = second; 
	}

	public U getFirst() {
		return this.first;
	}

	public V getSecond() {
		return this.second;
	}

	@Override
	public String toString() {
		return "Pair [first=" + this.first + ", second=" + this.second + "]";
	}
	
}
