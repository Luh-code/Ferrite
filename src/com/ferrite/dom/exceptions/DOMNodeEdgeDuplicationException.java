package com.ferrite.dom.exceptions;

import com.ferrite.dom.XMLNode;

public class DOMNodeEdgeDuplicationException extends DOMException {
  public DOMNodeEdgeDuplicationException(XMLNode root, XMLNode vertex) {
    super(String.format("Tried to add multiple connection from '%s' to '%s'", root.toString(), vertex.toString()));
  }
}
