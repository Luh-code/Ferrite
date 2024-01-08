package com.ferrite.dom.exceptions;

import com.ferrite.dom.NodeType;

public class DOMNodeRuleNonExistentException extends DOMException {
  public DOMNodeRuleNonExistentException(NodeType root, NodeType vertex) {
    super(String.format("Node of type '%s' does not have a rule for '%s'", root.toString(), vertex.toString()));
  }
}
