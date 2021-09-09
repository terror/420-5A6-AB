package com.example.asg1.model;

/**
 * Task priority values.
 */
public enum Priority {
  NONE,
  LOW,
  MEDIUM,
  HIGH;

  public static Priority from(String s) {
    switch (s.toLowerCase()) {
      case "low":
        return Priority.LOW;
      case "medium":
        return Priority.MEDIUM;
      case "high":
        return Priority.HIGH;
    }
    return NONE;
  }
}
