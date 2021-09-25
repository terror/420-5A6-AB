package com.example.asg2.model;

import java.util.HashMap;

/**
 * Task priority values.
 */
public enum Priority {
  NONE,
  LOW,
  MEDIUM,
  HIGH;

  private HashMap<Priority, Double> urgencyMap = new HashMap() {{
    put(Priority.NONE,   0);
    put(Priority.LOW,    1.8);
    put(Priority.MEDIUM, 3.9);
    put(Priority.HIGH,   6.0);
  }};

  public double urgency() {
    return urgencyMap.get(this);
  }
}
