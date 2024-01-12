package com.ferrite.dom.exceptions;

import com.ferrite.dom.XMLNode;

public class DOMNodeRulePluralityViolationException extends DOMException {

  public DOMNodeRulePluralityViolationException(XMLNode root, XMLNode vertex) {
    super(String.format(
            "A NodeRule plurality violation occurred, trying to create edge between node '%s' of type '%s' and node '%s' of type '%s'",
            root.toString(), root.getType().toString(), vertex.toString(), vertex.getType().toString()));
  }
}
