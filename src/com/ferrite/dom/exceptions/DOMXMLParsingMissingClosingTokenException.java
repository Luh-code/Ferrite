package com.ferrite.dom.exceptions;

import com.ferrite.dom.Node;

public class DOMXMLParsingMissingClosingTokenException extends DOMXMLException {
  public DOMXMLParsingMissingClosingTokenException(Node unclosed) {
    super(String.format("The node '%s' was never closed", unclosed.toString()));
  }
}
