package com.example.asg3.model;

import java.util.HashMap;

/**
 * Task priority values.
 */
public enum Priority {
  NONE,
  LOW,
  MEDIUM,
  HIGH;

  public static Priority from(int x) {
    Priority[] values = values();
    return values[x % values.length];
  }

  public static Double urgency(Priority p) {
    // define priority -> urgency values
    HashMap<Priority, Double> urgencyMap = new HashMap() {{
      put(Priority.NONE,   0.0);
      put(Priority.LOW,    1.8);
      put(Priority.MEDIUM, 3.9);
      put(Priority.HIGH,   6.0);
    }};

    // return corresponding priority urgency
    return urgencyMap.get(p);
  }
}
