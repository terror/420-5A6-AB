package com.example.asg3.viewmodel;

import com.example.asg3.model.Action;
import com.example.asg3.model.Task;

public class TaskListViewModel extends ObservableModel<TaskListViewModel> {
  private Action action;
  private Task task;
  private int id;

  public Action getAction() {
    return action;
  }

  public Task getTask() {
    return task;
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
