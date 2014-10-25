package engine.tasks;

import java.util.Iterator;

public abstract class Task<E> implements Iterator<E> {

	@Override
	public boolean hasNext() {
		return isFinished() == false;
	}

	@Override
	public E next() {
		return execute();
	}

	public abstract boolean isFinished();
	
	public abstract E execute();

}
