package com.ferrite.controller;

import java.time.LocalDateTime;

public class Timer {
  private int timeout;
  private long timestamp;
  private boolean running;

  public Timer() {
  }

  public void start() {
    timestamp = System.currentTimeMillis();
  }

  public boolean running() {
    return System.currentTimeMillis()-timestamp < timeout;
  }

  public int timeLeft() {
    return Math.max(timeout - (int)(System.currentTimeMillis()-timestamp),0);
  }

  public void setTimeout(int timeout) {
    this.timeout = timeout;
    start();
  }

  public boolean isRunning() {
    return running;
  }

  public void setRunning(boolean running) {
    this.running = running;
  }

  @Override
  public String toString() {
    return "[ " + timeout + ", " + timestamp + " -> " + timeLeft() + " ]";
  }
}
