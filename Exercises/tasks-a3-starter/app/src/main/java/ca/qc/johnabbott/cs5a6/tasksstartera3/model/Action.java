package ca.qc.johnabbott.cs5a6.tasksstartera3.model;

import java.util.HashMap;

public enum Action {
  ADD,
  EDIT;

  public static String message(Action action) {
    HashMap<Action, String> messageMap = new HashMap() {{
      put(Action.ADD, "Task added.");
      put(Action.EDIT, "Task modified.");
    }};
    return messageMap.get(action);
  }
}
