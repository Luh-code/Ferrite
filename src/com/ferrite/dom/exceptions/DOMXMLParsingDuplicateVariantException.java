package com.ferrite.dom.exceptions;

import com.ferrite.dom.Node;
import com.ferrite.dom.NodeVariant;

public class DOMXMLParsingDuplicateVariantException extends DOMXMLException {
  public DOMXMLParsingDuplicateVariantException(Node node) {
    super(String.format("Tried to set variant of node '%s' multiple times during parsing", node.toString()));
  }
}
