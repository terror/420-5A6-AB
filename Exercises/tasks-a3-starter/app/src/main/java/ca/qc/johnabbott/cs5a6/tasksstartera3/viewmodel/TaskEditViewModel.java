package ca.qc.johnabbott.cs5a6.tasksstartera3.viewmodel;

import ca.qc.johnabbott.cs5a6.tasksstartera3.model.Action;
import ca.qc.johnabbott.cs5a6.tasksstartera3.model.Task;

public class TaskEditViewModel extends ObservableModel<TaskEditViewModel> {
  private Task task;
  private Action action;

  public Task getTask() {
    return task;
  }

  public Action getAction() {
    return action;
  }

  public TaskEditViewModel setTask(Task task) {
    this.task = task;
    return this;
  }

  public TaskEditViewModel setAction(Action action) {
    this.action = action;
    return this;
  }

  @Override
  protected TaskEditViewModel get() {
    return this;
  }
}
