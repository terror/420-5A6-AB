package com.example.asg2.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a task.
 * <p>
 * This representation is based on the information stored for tasks in the command line
 * tool "task" (https://taskwarrior.org/).
 * <p>
 * A more detailed description of the fields can be found at https://taskwarrior.org/docs/design/task.html
 */
public class Task {
  // generate local IDs (in memory only).
  private static int CURRENT_LOCAL_ID = 0;

  // Identifying tasks
  private int id;
  private UUID uuid;  // UUID

  // The decription of the task.
  private String description;

  // Basic features of the task
  private Status status;
  private Priority priority;

  private String project;
  private List<Annotation> annotations;
  private List<String> tags;

  // Standard dates and times for tasks
  private Date entry;     // created timestamp
  private Date modified;  // last modified timestamp
  private Date due;       // due date of the task

  // Optional dates and times for tasks
  private Date start;     // start working on task
  private Date end;       // end working on task
  private Date wait;      // for delayed tasks
  private Date scheduled; // for delayed starts to tasks (not the same as "due" above)

  // Data relating to the recurrence of tasks (To be determined)
  private String recur;
  private String mask;
  private int imask;
  private Date until;
  private String parent;

  // Data relating to task dependency
  private String depends;

  // Computed value of the task's urgency.
  private double urgency;

  /**
   * Create a blank task.
   */
  public Task() {
    this(++CURRENT_LOCAL_ID, UUID.randomUUID());
  }

  /**
   * Create a task with a set UUID.
   *
   * @param uuid
   */
  public Task(UUID uuid) {
    this(++CURRENT_LOCAL_ID, uuid);
  }

  /**
   * Create a task with a set id and UUID.
   *
   * @param id
   * @param uuid
   */
  public Task(int id, UUID uuid) {
    this.id = id;
    this.uuid = uuid;
    status = Status.NONE;
    priority = Priority.NONE;
    tags = new ArrayList<>();
    annotations = new ArrayList<>();
  }


  public int getId() {
    return id;
  }

  public Task setId(int id) {
    this.id = id;
    return this;
  }

  public UUID getUuid() {
    return uuid;
  }

  public Task setUuid(UUID uuid) {
    this.uuid = uuid;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public Task setDescription(String description) {
    this.description = description;
    return this;
  }

  public Status getStatus() {
    return status;
  }

  public Task setStatus(Status status) {
    this.status = status;
    return this;
  }

  public Priority getPriority() {
    return priority;
  }

  public Task setPriority(Priority priority) {
    this.priority = priority;
    return this;
  }

  public Date getEntry() {
    return entry;
  }

  public Task setEntry(Date entry) {
    this.entry = entry;
    return this;
  }

  public Date getModified() {
    return modified;
  }

  public Task setModified(Date modified) {
    this.modified = modified;
    return this;
  }

  public Date getStart() {
    return start;
  }

  public Task setStart(Date start) {
    this.start = start;
    return this;
  }

  public Date getEnd() {
    return end;
  }

  public Task setEnd(Date end) {
    this.end = end;
    return this;
  }

  public Date getDue() {
    return due;
  }

  public Task setDue(Date due) {
    this.due = due;
    return this;
  }

  public Date getWait() {
    return wait;
  }

  public Task setWait(Date wait) {
    this.wait = wait;
    return this;
  }

  public Date getUntil() {
    return until;
  }

  public Task setUntil(Date until) {
    this.until = until;
    return this;
  }

  public Date getScheduled() {
    return scheduled;
  }

  public Task setScheduled(Date scheduled) {
    this.scheduled = scheduled;
    return this;
  }

  public String getRecur() {
    return recur;
  }

  public Task setRecur(String recur) {
    this.recur = recur;
    return this;
  }

  public String getMask() {
    return mask;
  }

  public Task setMask(String mask) {
    this.mask = mask;
    return this;
  }

  public int getImask() {
    return imask;
  }

  public Task setImask(int imask) {
    this.imask = imask;
    return this;
  }

  public String getParent() {
    return parent;
  }

  public Task setParent(String parent) {
    this.parent = parent;
    return this;
  }

  public String getProject() {
    return project;
  }

  public Task setProject(String project) {
    this.project = project;
    return this;
  }

  public String getDepends() {
    return depends;
  }

  public Task setDepends(String depends) {
    this.depends = depends;
    return this;
  }

  public List<String> getTags() {
    return tags;
  }

  public Task setTags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  public double getUrgency() {
    return urgency;
  }

  public Task setUrgency(double urgency) {
    this.urgency = urgency;
    return this;
  }

  public List<Annotation> getAnnotations() {
    return annotations;
  }

  public Task setAnnotations(List<Annotation> annotations) {
    this.annotations = annotations;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Task task = (Task) o;
    return id == task.id &&
      imask == task.imask &&
      Double.compare(task.urgency, urgency) == 0 &&
      Objects.equals(uuid, task.uuid) &&
      Objects.equals(description, task.description) &&
      status == task.status &&
      priority == task.priority &&
      Objects.equals(entry, task.entry) &&
      Objects.equals(modified, task.modified) &&
      Objects.equals(start, task.start) &&
      Objects.equals(end, task.end) &&
      Objects.equals(due, task.due) &&
      Objects.equals(wait, task.wait) &&
      Objects.equals(until, task.until) &&
      Objects.equals(scheduled, task.scheduled) &&
      Objects.equals(recur, task.recur) &&
      Objects.equals(mask, task.mask) &&
      Objects.equals(parent, task.parent) &&
      Objects.equals(project, task.project) &&
      Objects.equals(depends, task.depends) &&
      Objects.equals(tags, task.tags) &&
      Objects.equals(annotations, task.annotations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, uuid, description, status, priority, entry, modified, start, end, due, wait, until, scheduled, recur, mask, imask, parent, project, depends, tags, urgency, annotations);
  }

  /**
   * Create a copy (clone) of the task.
   *
   * @return The copy of the task.
   */
  public Task copy() {
    Task tmp = new Task();
    tmp.setId(this.id);
    tmp.setUuid(this.uuid);
    tmp.setDescription(this.description);
    tmp.setStatus(this.status);
    tmp.setPriority(this.priority);
    tmp.setEntry(this.entry);
    tmp.setModified(this.modified);
    tmp.setStart(this.start);
    tmp.setEnd(this.end);
    tmp.setDue(this.due);
    tmp.setWait(this.wait);
    tmp.setUntil(this.until);
    tmp.setScheduled(this.scheduled);
    tmp.setRecur(this.recur);
    tmp.setMask(this.mask);
    tmp.setImask(this.imask);
    tmp.setParent(this.parent);
    tmp.setProject(this.project);
    tmp.setDepends(this.depends);
    tmp.setTags(new ArrayList<>(this.tags));
    tmp.setUrgency(this.urgency);
    tmp.setAnnotations(new ArrayList<>(this.annotations));
    return tmp;
  }

  @Override
  public String toString() {
    return "Task{" +
      "id=" + id +
      ", uuid=" + uuid +
      ", description='" + description + '\'' +
      ", status=" + status +
      ", priority=" + priority +
      ", entry=" + entry +
      ", modified=" + modified +
      ", start=" + start +
      ", end=" + end +
      ", due=" + due +
      ", wait=" + wait +
      ", until=" + until +
      ", scheduled=" + scheduled +
      ", recur='" + recur + '\'' +
      ", mask='" + mask + '\'' +
      ", imask=" + imask +
      ", parent='" + parent + '\'' +
      ", project='" + project + '\'' +
      ", depends='" + depends + '\'' +
      ", tags=" + tags +
      ", urgency=" + urgency +
      ", annotations=" + annotations +
      '}';
  }
}
