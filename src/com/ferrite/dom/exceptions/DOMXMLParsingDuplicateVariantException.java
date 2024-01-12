package com.ferrite.dom.exceptions;

import com.ferrite.dom.XMLNode;

public class DOMXMLParsingDuplicateVariantException extends DOMXMLException {
  public DOMXMLParsingDuplicateVariantException(XMLNode node) {
    super(String.format("Tried to set variant of node '%s' multiple times during parsing", node.toString()));
  }
}
