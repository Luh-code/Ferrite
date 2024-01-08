package com.ferrite.dom.serialization;

import java.util.ArrayList;

public class XMLOpeningToken extends XMLToken {
  public ArrayList<XMLAttributeToken> attributes;

  public XMLOpeningToken(String tag, int depth) {
    super(tag, depth);
    this.attributes = new ArrayList<>();
  }

  public String getTag() {
    return getData();
  }

  public ArrayList<XMLAttributeToken> getAttributes() {
    return attributes;
  }

  public void setAttributes(ArrayList<XMLAttributeToken> attributes) {
    this.attributes = attributes;
  }

  public void addAttribute(XMLAttributeToken attribute) {
    this.attributes.add(attribute);
  }
}
