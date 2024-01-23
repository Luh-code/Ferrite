package com.ferrite.dom.exceptions.TreeWalker;

import com.ferrite.dom.DOMNode;
import com.ferrite.dom.NodeType;

public class TreeWalkerNodeNotFoundException extends TreeWalkerException {
  public TreeWalkerNodeNotFoundException(NodeType type, DOMNode node) {
    super(String.format("No node of type '%s' is an edge to node '%s'", type, node));
  }
}
