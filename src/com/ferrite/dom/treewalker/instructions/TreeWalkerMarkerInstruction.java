package com.ferrite.dom.treewalker.instructions;

import com.ferrite.dom.treewalker.TreeWalker;

public class TreeWalkerMarkerInstruction implements TreeWalkerInstruction {
  private boolean value;

  public TreeWalkerMarkerInstruction(boolean value) {
    this.value = value;
  }

  @Override
  public void act(TreeWalker walker) {

  }

  @Override
  public boolean getLate() {
    return false;
  }

  public boolean getValue() {
    return this.value;
  }
}
