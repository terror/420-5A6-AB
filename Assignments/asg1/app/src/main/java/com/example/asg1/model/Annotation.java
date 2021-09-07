package com.example.asg1.model;

import java.util.Date;
import java.util.UUID;

/**
 * An annotation for a task.
 */
public class Annotation {
  private long id;
  private Date entry;
  private String description;
  private UUID taskUuid;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Date getEntry() {
    return entry;
  }

  public Annotation setEntry(Date entry) {
    this.entry = entry;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public Annotation setDescription(String description) {
    this.description = description;
    return this;
  }

  public UUID getTaskUuid() {
    return taskUuid;
  }

  public Annotation setTaskUuid(UUID taskUuid) {
    this.taskUuid = taskUuid;
    return this;
  }
}
