package com.ferrite.dom.exceptions;

import com.ferrite.dom.DOMNode;

public class DOMXMLParsingMissingPairingException extends DOMXMLException {
  public DOMXMLParsingMissingPairingException(DOMNode node) {
    super(String.format("No XMLToken pairing for node '%s' found", node.toString()));
  }
}
