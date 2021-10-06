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

  public static Priority from(int x) {
    Priority[] values = values();
    return values[x % values.length];
  }
}
