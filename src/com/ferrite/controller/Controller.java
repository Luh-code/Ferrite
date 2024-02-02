package com.ferrite.controller;

import com.ferrite.dom.DOMNode;

import java.util.BitSet;
import java.util.HashMap;

public class Controller {
  private BitSet input;
  private BitSet output;

  private HashMap<String, Timer> timers;

  private Controller(int inputBits, int outputBits) {
    this.input = new BitSet(inputBits);
    this.output = new BitSet(outputBits);
    this.timers = new HashMap<>();
  }

  private static Controller instance;

  public static void initInstance(int inputBits, int outputBits) {
    if (instance != null) {
      throw new RuntimeException("Cannot create multiple Controller instances");
    }
    instance = new Controller(inputBits, outputBits);
  }

  public static Controller getInstance() {
    if (instance == null) {
      throw new RuntimeException("Cannot get uninitialized instance");
    }
    return instance;
  }

  public void updateInput(BitSet input) {
    if (this.input.size() != input.size()) {
      throw new IllegalArgumentException("Cannot set input BitSet with differently sized BitSet");
    }
    this.input = input;
  }

  public boolean getInput(int bit) {
    if (bit >= this.input.size() || bit < 0) {
      throw new IllegalArgumentException("Cannot get a bit outside the range of the input BitSet");
    }
    return this.input.get(bit);
  }

  public BitSet getInput() {
    return input;
  }

  public void setOutput(int bit, boolean value) {
    if (bit >= this.output.size() || bit < 0) {
      throw new IllegalArgumentException("Cannot set a bit outside the range of the output BitSet");
    }
    this.output.set(bit, value);
  }

  public static String bitSetToString(BitSet bitSet) {
    StringBuilder stringBuilder = new StringBuilder();
    int size = bitSet.length();

    // Append each bit to the StringBuilder
    for (int i = 0; i < size; i++) {
      stringBuilder.append(bitSet.get(i) ? "1" : "0");
    }

    return stringBuilder.toString();
  }

  public boolean getOutput(int bit) {
    if (bit >= this.output.size() || bit < 0) {
      throw new IllegalArgumentException("Cannot get a bit outside the range of the output BitSet");
    }
    return this.output.get(bit);
  }
  public BitSet getOutput() {
    return output;
  }

  public boolean checkIfTimerExists(String name) {
    return this.timers.containsKey(name);
  }

  public void createTimer(String name) {
    if (checkIfTimerExists(name)) {
      throw new RuntimeException("Cannot create two timers with the same key");
    }
    this.timers.put(name, new Timer());
  }

  public void startTimer(String name, int timeout) {
    if (!checkIfTimerExists(name)) {
      throw new RuntimeException("Cannot check non-existent timer");
    }
    this.timers.get(name).setTimeout(timeout);
  }

  public int checkTimer(String name) {
    if (!checkIfTimerExists(name)) {
      throw new RuntimeException("Cannot check non-existent timer");
    }
    return this.timers.get(name).timeLeft();
  }

  public Timer[] getTimer() {
    return this.timers.values().toArray(Timer[]::new);
  }
  public static String timerArrayToString(Timer[] timer) {
    StringBuilder stringBuilder = new StringBuilder();

    stringBuilder.append("{ ");

    for (int i = 0; i < timer.length; i++) {
      stringBuilder.append(timer[i].toString());
      stringBuilder.append(", ");
    }

    stringBuilder.append("}");

    return stringBuilder.toString();
  }
}
