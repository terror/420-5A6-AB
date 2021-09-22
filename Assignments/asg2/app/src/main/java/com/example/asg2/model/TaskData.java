package com.example.asg2.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TaskData {
  private static List<Task> data;

  private static void loadData() throws ParseException {
    data = new ArrayList<>();

    final SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd'T'hhmmss'Z'");

    data.add(new Task()
      .setUuid(UUID.fromString("eee94ca2-70ea-4114-8f8d-330c199bae50"))
      .setDescription("Recycling")
      .setEntry(fmt.parse("20210916T002338Z"))
      .setModified(fmt.parse("20210916T002501Z"))
      .setDue(fmt.parse("20210920T083000Z"))
      .setPriority(Priority.LOW)
      .setStatus(Status.PENDING)
      .setTags(Arrays.asList("home".split(","))));
    data.add(new Task()
      .setUuid(UUID.fromString("a557b5ea-a652-4719-86e8-a2bbf7ffddae"))
      .setDescription("Garbage")
      .setEntry(fmt.parse("20210916T002405Z"))
      .setModified(fmt.parse("20210916T002511Z"))
      .setDue(fmt.parse("20210922T083000Z"))
      .setPriority(Priority.LOW)
      .setStatus(Status.PENDING)
      .setTags(Arrays.asList("home".split(","))));
    data.add(new Task()
      .setUuid(UUID.fromString("dacf5b66-0386-43f4-b95a-3a838b7fee83"))
      .setDescription("Assignment 2 instructions")
      .setEntry(fmt.parse("20210916T002542Z"))
      .setModified(fmt.parse("20210916T002801Z"))
      .setDue(fmt.parse("20210923T040000Z"))
      .setPriority(Priority.MEDIUM)
      .setStatus(Status.PENDING)
      .setTags(Arrays.asList("work,cs616".split(","))));
    data.add(new Task()
      .setUuid(UUID.fromString("26503266-4073-4e89-bddb-f231ca721d0d"))
      .setDescription("Week 4 lectures")
      .setEntry(fmt.parse("20210916T002633Z"))
      .setModified(fmt.parse("20210916T003546Z"))
      .setPriority(Priority.HIGH)
      .setStatus(Status.PENDING)
      .setTags(Arrays.asList("cs616,work".split(","))));
    data.add(new Task()
      .setUuid(UUID.fromString("74b6d840-52b1-4f8b-b7fc-675516e968d9"))
      .setDescription("Get a new phone")
      .setEntry(fmt.parse("20210916T003240Z"))
      .setModified(fmt.parse("20210916T003418Z"))
      .setDue(fmt.parse("20210831T003418Z"))
      .setPriority(Priority.MEDIUM)
      .setStatus(Status.PENDING)
      .setTags(Arrays.asList("".split(","))));
    data.add(new Task()
      .setUuid(UUID.fromString("94315db1-4ca4-44bc-9467-89cecfac9116"))
      .setDescription("Learn lisp")
      .setEntry(fmt.parse("20210916T003336Z"))
      .setModified(fmt.parse("20210916T003737Z"))
      .setPriority(Priority.NONE)
      .setStatus(Status.PENDING)
      .setUrgency(1.8)
      .setTags(Arrays.asList("".split(","))));
    data.add(new Task()
      .setUuid(UUID.fromString("13ffecaf-353d-4f1c-bf92-8ae33e4f58be"))
      .setDescription("Write a custom emacs mode")
      .setEntry(fmt.parse("20210916T003520Z"))
      .setModified(fmt.parse("20210916T003531Z"))
      .setPriority(Priority.LOW)
      .setStatus(Status.PENDING)
      .setTags(Arrays.asList("".split(","))));
    data.add(new Task()
      .setUuid(UUID.fromString("877dd110-3f99-4bf3-a022-59f3c9040910"))
      .setDescription("Practice music")
      .setEntry(fmt.parse("20210916T003615Z"))
      .setModified(fmt.parse("20210916T003642Z"))
      .setPriority(Priority.HIGH)
      .setStatus(Status.PENDING)
      .setTags(Arrays.asList("home".split(","))));
    data.add(new Task()
      .setUuid(UUID.fromString("6bd19cc7-0db2-4dde-87a8-1eb14e89f1c7"))
      .setDescription("Bake bread")
      .setEntry(fmt.parse("20210916T004239Z"))
      .setModified(fmt.parse("20210916T004307Z"))
      .setDue(fmt.parse("20210915T040000Z"))
      .setPriority(Priority.HIGH)
      .setStatus(Status.COMPLETED)
      .setTags(Arrays.asList("home".split(","))));
    data.add(new Task()
      .setUuid(UUID.fromString("8942302c-8983-4253-9b52-040bc9c38925"))
      .setDescription("Week 3 lectures")
      .setEntry(fmt.parse("20210916T003022Z"))
      .setModified(fmt.parse("20210916T003034Z"))
      .setStatus(Status.COMPLETED)
      .setTags(Arrays.asList("work,cs616".split(","))));
    data.add(new Task()
      .setUuid(UUID.fromString("68050350-995c-4d22-bcfe-0456cc5f17c4"))
      .setDescription("Minutes of the CS Program Committee.")
      .setEntry(fmt.parse("20210916T002841Z"))
      .setModified(fmt.parse("20210916T002858Z"))
      .setDue(fmt.parse("20210916T040000Z"))
      .setStatus(Status.COMPLETED)
      .setTags(Arrays.asList("work".split(","))));
    data.add(new Task()
      .setUuid(UUID.fromString("cf0ed7f6-4015-4a9f-898d-23032ffeb0cc"))
      .setDescription("Week 1 lecture and demo")
      .setEntry(fmt.parse("20210830T002841Z"))
      .setModified(fmt.parse("20210830T002858Z"))
      .setDue(fmt.parse("20210831T040000Z"))
      .setStatus(Status.COMPLETED)
      .setTags(Arrays.asList("work".split(","))));
    data.add(new Task()
      .setUuid(UUID.fromString("9e9a7f09-880c-443c-8f2b-3bedd98d0158"))
      .setDescription("Week 2 lecture.")
      .setEntry(fmt.parse("20210901T002841Z"))
      .setModified(fmt.parse("20210901T002858Z"))
      .setDue(fmt.parse("20210901T040000Z"))
      .setStatus(Status.COMPLETED)
      .setTags(Arrays.asList("work".split(","))));
    data.add(new Task()
      .setUuid(UUID.fromString("aaf18108-2bfe-4a6b-a021-8b832ff133f3"))
      .setDescription("Assignment 1 instructions")
      .setEntry(fmt.parse("20210905T002841Z"))
      .setModified(fmt.parse("20210905T002858Z"))
      .setDue(fmt.parse("20210905T040000Z"))
      .setStatus(Status.COMPLETED)
      .setTags(Arrays.asList("work".split(","))));
    data.add(new Task()
      .setUuid(UUID.fromString("b2ddcb67-d1ff-4e6c-8073-8f0d3ea1ff43"))
      .setDescription("Assignment 2 sample data")
      .setEntry(fmt.parse("20210916T002841Z"))
      .setModified(fmt.parse("20210916T002858Z"))
      .setDue(fmt.parse("20210916T040000Z"))
      .setStatus(Status.COMPLETED)
      .setTags(Arrays.asList("work".split(","))));
    data.add(new Task()
      .setUuid(UUID.fromString("32c8cd40-596d-4dc6-8333-e5ca05636226"))
      .setDescription("Week 5 lecture/demo")
      .setEntry(fmt.parse("20210916T002841Z"))
      .setModified(fmt.parse("20210916T002858Z"))
      .setDue(fmt.parse("20210924T040000Z"))
      .setStatus(Status.COMPLETED)
      .setTags(Arrays.asList("work".split(","))));
    data.add(new Task()
      .setUuid(UUID.fromString("7f8e1a75-cb9d-4b75-b2ef-a7e98440ddb1"))
      .setDescription("Quiz 1 questions")
      .setEntry(fmt.parse("20210915T002841Z"))
      .setModified(fmt.parse("20210915T002858Z"))
      .setDue(fmt.parse("20210920T040000Z"))
      .setStatus(Status.COMPLETED)
      .setTags(Arrays.asList("work".split(","))));
    data.add(new Task()
      .setUuid(UUID.fromString("06869c92-c805-4c03-80ca-d38a3ec76eb3"))
      .setDescription("Assignment 3 instructions")
      .setEntry(fmt.parse("20210905T002841Z"))
      .setModified(fmt.parse("20210905T002858Z"))
      .setDue(fmt.parse("20211005T040000Z"))
      .setStatus(Status.COMPLETED)
      .setTags(Arrays.asList("work".split(","))));
  }

  public static List<Task> getData() {
    if (data == null) {
      try {
        loadData();
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
    return data;
  }
}
