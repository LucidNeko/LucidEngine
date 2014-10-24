package engine.tasks;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class TaskManager {
	
	private static Set<Task> tasks = new HashSet<Task>();
	
	public static void addTask(Task task) {
		tasks.add(task);
	}
	
	public static void tick(float delta) {
		List<Task> _tasks = new LinkedList<Task>(tasks);
		for(Task task : _tasks) {
			if(task.isFinished())
				tasks.remove(task);
			else task.execute(delta);
		}
	}

}
