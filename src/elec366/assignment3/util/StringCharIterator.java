package elec366.assignment3.util;

import java.util.Iterator;

public class StringCharIterator implements Iterator<Character> {

	private final String string; 
	private final int length; 
	
	private int index; 
	
	public StringCharIterator(String string) {
		this.string = string; 
		this.length = string.length(); 
		this.index = 0; 
	}

	public void reset() {
		this.index = 0; 
	}
	
	@Override
	public boolean hasNext() {
		return this.index < this.length; 
	}
	
	public boolean hasNext(int n) {
		return this.index + n <= this.length; 
	}

	@Override
	public Character next() {
		return this.string.charAt(this.index++); 
	}
	
	public String next(int n) {
		String string = this.string.substring(this.index, this.index + n); 
		this.index += n; 
		return string; 
	}
	
	public int getIndex() {
		return this.index; 
	}
	
	public String getString() {
		return this.string; 
	}

}
