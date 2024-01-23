package com.ferrite.controller;

import com.ferrite.dom.DOMNode;

import java.util.BitSet;
import java.util.HashMap;

public class Controller {
  private BitSet input;
  private BitSet output;

  public Controller(int inputBits, int outputBits) {
    this.input = new BitSet(inputBits);
    this.output = new BitSet(outputBits);
    //this.updated =
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

  public void setOutput(int bit, boolean value) {
    if (bit >= this.output.size() || bit < 0) {
      throw new IllegalArgumentException("Cannot set a bit outside the range of the output BitSet");
    }
    this.output.set(bit, value);

    System.out.println("Input: "+bitSetToString(this.input));
    System.out.println("Output: "+bitSetToString(this.output));
  }

  private static String bitSetToString(BitSet bitSet) {
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
}
