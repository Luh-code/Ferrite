package com.ferrite.dom.exceptions.TreeWalker;

public class TreeWalkerNoOriginStateFoundException extends TreeWalkerException {
  public TreeWalkerNoOriginStateFoundException(String systemName) {
    super(String.format("TreeWalker could not find any origin state in System '%s'", systemName));
  }
}
