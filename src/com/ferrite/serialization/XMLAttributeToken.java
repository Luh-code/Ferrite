package com.ferrite.serialization;

public class XMLAttributeToken extends XMLToken {
  private String value;
  public XMLAttributeToken(String name, int depth, String value) {
    super(name, depth);
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
