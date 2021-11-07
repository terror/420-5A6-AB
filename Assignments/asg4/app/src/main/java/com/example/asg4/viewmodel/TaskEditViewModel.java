package com.example.asg4.viewmodel;

import com.example.asg4.model.Action;
import com.example.asg4.model.Task;

public class TaskEditViewModel extends ObservableModel<TaskEditViewModel> {
  private Action action;
  private Task task;

  public Action getAction() {
    return action;
  }

  public Task getTask() {
    return task;
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
