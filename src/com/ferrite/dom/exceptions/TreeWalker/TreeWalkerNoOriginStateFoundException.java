package com.ferrite.dom.exceptions.TreeWalker;

import com.ferrite.dom.treewalker.TreeWalker;

public class TreeWalkerNoOriginStateFoundException extends TreeWaiterException {
  public TreeWalkerNoOriginStateFoundException(String systemName) {
    super(String.format("TreeWalker could not find any origin state in System '%s'", systemName));
  }
}
