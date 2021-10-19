package com.example.asg3.viewmodel;

import com.example.asg3.model.Action;
import com.example.asg3.model.Task;

public class TaskViewModel extends ObservableModel<TaskViewModel> {
  private Action action;
  private Task task;

  public Action getAction() {
    return action;
  }

  public Task getTask() {
    return task;
  }

  public TaskViewModel setTask(Task task) {
    this.task = task;
    return this;
  }

  public TaskViewModel setAction(Action action) {
    this.action = action;
    return this;
  }

  @Override
  protected TaskViewModel get() {
    return this;
  }
}
