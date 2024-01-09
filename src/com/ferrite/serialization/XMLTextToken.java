package com.ferrite.serialization;

public class XMLTextToken extends XMLToken {
  public XMLTextToken(String text, int depth) {
    super(text, depth);
  }

  public String getText() {
    return getData();
  }
}
