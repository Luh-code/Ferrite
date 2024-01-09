package com.ferrite.serialization;

public class XMLToken {
  private String data;
  private XMLToken pairToken;
  private int depth;

  public XMLToken(String data, int depth) {
    this.data = data;
    this.depth = depth;
  }

  public String getData() {
    return data;
  }

  public XMLToken getPairToken() {
    return pairToken;
  }

  public void setPairToken(XMLToken pairToken) {
    this.pairToken = pairToken;
  }

  public int getDepth() {
    return depth;
  }

  public void setDepth(int depth) {
    this.depth = depth;
  }
}
