package com.ferrite.dom.exceptions;

import com.ferrite.dom.XMLNode;

public class DOMXMLParsingMissingClosingTokenException extends DOMXMLException {
  public DOMXMLParsingMissingClosingTokenException(XMLNode unclosed) {
    super(String.format("The node '%s' was never closed", unclosed.toString()));
  }
}
