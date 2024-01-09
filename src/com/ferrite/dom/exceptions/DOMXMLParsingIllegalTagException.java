package com.ferrite.dom.exceptions;

public class DOMXMLParsingIllegalTagException extends DOMXMLException {
  public DOMXMLParsingIllegalTagException(String tag) {
    super(String.format("The tag '%s' is not representative of a legal NodeType", tag));
  }
}
