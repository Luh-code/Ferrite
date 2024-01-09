package com.ferrite.dom.exceptions;

import com.ferrite.dom.Node;
import com.ferrite.serialization.XMLClosingToken;
import com.ferrite.serialization.XMLToken;

public class DOMXMLParsingMissingOpeningTokenException extends DOMXMLException {
  public DOMXMLParsingMissingOpeningTokenException(XMLToken token) {
    super(String.format("The token '%s'('%s') does not have a node to close", token.toString(), token.getData()));
  }
}
