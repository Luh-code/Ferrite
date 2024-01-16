package com.ferrite.dom.exceptions;

import com.ferrite.dom.DOMNode;

public class DOMXMLParsingMissingClosingTokenException extends DOMXMLException {
  public DOMXMLParsingMissingClosingTokenException(DOMNode unclosed) {
    super(String.format("The node '%s' was never closed", unclosed.toString()));
  }
}
