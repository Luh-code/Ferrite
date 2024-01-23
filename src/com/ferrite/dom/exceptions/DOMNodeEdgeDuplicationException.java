package com.ferrite.dom.exceptions;

import com.ferrite.dom.DOMNode;

public class DOMNodeEdgeDuplicationException extends DOMException {
  public DOMNodeEdgeDuplicationException(DOMNode root, DOMNode vertex) {
    super(String.format("Tried to add multiple connection from '%s' to '%s'", root.toString(), vertex.toString()));
  }
}
