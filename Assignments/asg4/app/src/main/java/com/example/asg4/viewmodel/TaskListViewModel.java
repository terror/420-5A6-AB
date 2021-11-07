package com.example.asg4.viewmodel;

import com.example.asg4.model.Action;
import com.example.asg4.model.Task;

public class TaskListViewModel extends ObservableModel<TaskListViewModel> {
  private Action action;
  private Task task;
  private Long id;

  public Action getAction() {
    return action;
  }

  public Task getTask() {
    return task;
  }

  public TaskListViewModel setTask(Task task) {
    this.task = task;
    return this;
  }

  public TaskListViewModel setAction(Action action) {
    this.action = action;
    return this;
  }

  public Long getId() {
    return id;
  }

  public TaskListViewModel setId(Long id) {
    this.id = id;
    return this;
  }

  @Override
  protected TaskListViewModel get() {
    return this;
  }
}
