package com.example.asg4.viewmodel;

import com.example.asg4.model.Task;

import java.util.List;

public class TaskRecyclerViewAdapterViewModel extends ObservableModel<TaskRecyclerViewAdapterViewModel> {
  private List<Task> tasks;

  public List<Task> getTasks() {
    return tasks;
  }

  public TaskRecyclerViewAdapterViewModel setTasks(List<Task> tasks) {
    this.tasks = tasks;
    return this;
  }

  @Override
  protected TaskRecyclerViewAdapterViewModel get() {
    return this;
  }
}
