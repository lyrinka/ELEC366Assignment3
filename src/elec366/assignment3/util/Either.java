package elec366.assignment3.util;

/*
 * A wrapper for either U or V. 
 */
public class Either<U, V> {

	public static <U, V> Either<U, V> ofFirst(U first) {
		return new Either<>(first, null); 
	}
	
	public static <U, V> Either<U, V> ofSecond(V second) {
		return new Either<>(null, second); 
	}
	
	private final U first; 
	private final V second;
	
	private Either(U first, V second) {
		this.first = first;
		this.second = second;
	}
	
	public boolean hasFirst() {
		return this.first != null; 
	}
	
	public boolean hasSecond() {
		return this.second != null; 
	}
	
	public U getFirst() {
		return this.first; 
	}
	
	public V getSecond() {
		return this.second; 
	}
	
	public Object get() {
		if(this.first != null)
			return (Object)this.first;
		else
			return (Object)this.second; 
	}

}
