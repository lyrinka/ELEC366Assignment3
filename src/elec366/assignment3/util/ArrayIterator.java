package elec366.assignment3.util;

import java.util.Iterator;

public class ArrayIterator<E> implements Iterator<E> {

	private final E[] array; 
	private int index;
	
	public ArrayIterator(E[] array) {
		this.array = array;
		this.index = 0; 
	}

	@Override
	public boolean hasNext() {
		return this.index < this.array.length; 
	}

	@Override
	public E next() {
		if(!this.hasNext()) return null; 
		return this.array[this.index++]; 
	}

}
