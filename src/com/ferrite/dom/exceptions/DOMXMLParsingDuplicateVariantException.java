package com.ferrite.dom.exceptions;

import com.ferrite.dom.DOMNode;

public class DOMXMLParsingDuplicateVariantException extends DOMXMLException {
  public DOMXMLParsingDuplicateVariantException(DOMNode node) {
    super(String.format("Tried to set variant of node '%s' multiple times during parsing", node.toString()));
  }
}
