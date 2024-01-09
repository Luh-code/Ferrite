package com.ferrite.dom.exceptions;

import com.ferrite.dom.Node;

public class DOMXMLParsingMissingPairingException extends DOMXMLException {
  public DOMXMLParsingMissingPairingException(Node node) {
    super(String.format("No XMLToken pairing for node '%s' found", node.toString()));
  }
}
