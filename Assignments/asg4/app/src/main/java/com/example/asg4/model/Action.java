package com.example.asg4.model;

import java.util.HashMap;

public enum Action {
  ADD,
  EDIT;

  public static String message(Action action) {
    HashMap<Priority, String> messageMap = new HashMap() {{
      put(Action.ADD,  "Task added.");
      put(Action.EDIT, "Task modified.");
    }};

    return messageMap.get(action);
  }
}
