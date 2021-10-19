package ca.qc.johnabbott.cs5a6.tasksstartera3.viewmodel;

import ca.qc.johnabbott.cs5a6.tasksstartera3.model.Action;
import ca.qc.johnabbott.cs5a6.tasksstartera3.model.Task;

public class TaskListViewModel extends ObservableModel<TaskListViewModel> {
  private Task task;
  private Action action;
  private int id;

  public Task getTask() {
    return task;
  }

  public Action getAction() {
    return action;
  }

  public int getId() {
    return id;
  }

  public TaskListViewModel setTask(Task task) {
    this.task = task;
    return this;
  }

  public TaskListViewModel setAction(Action action) {
    this.action = action;
    return this;
  }

  public TaskListViewModel setId(int id) {
    this.id = id;
    return this;
  }

  @Override
  protected TaskListViewModel get() {
    return this;
  }
}
