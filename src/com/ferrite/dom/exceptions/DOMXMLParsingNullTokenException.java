package com.ferrite.dom.exceptions;

public class DOMXMLParsingNullTokenException extends DOMXMLException {
  public DOMXMLParsingNullTokenException() {
    super("A null token was discovered whilst parsing XML");
  }
}
