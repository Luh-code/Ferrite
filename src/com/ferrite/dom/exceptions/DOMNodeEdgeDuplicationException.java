package com.ferrite.dom.exceptions;

import com.ferrite.dom.Node;

public class DOMNodeEdgeDuplicationException extends DOMException {
  public DOMNodeEdgeDuplicationException(Node root, Node vertex) {
    super(String.format("Tried to add multiple connection from '%s' to '%s'", root.toString(), vertex.toString()));
  }
}
