package com.ferrite.dom.exceptions;

import com.ferrite.dom.XMLNode;

public class DOMXMLParsingMissingPairingException extends DOMXMLException {
  public DOMXMLParsingMissingPairingException(XMLNode node) {
    super(String.format("No XMLToken pairing for node '%s' found", node.toString()));
  }
}
