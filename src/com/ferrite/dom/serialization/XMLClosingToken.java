package com.ferrite.dom.serialization;

public class XMLClosingToken extends XMLToken {
  public XMLClosingToken(String tag, int depth) {
    super(tag, depth);
  }

  public String getTag() {
    return getData();
  }
}
