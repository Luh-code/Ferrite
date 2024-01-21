package com.ferrite.dom.treewalker.instructions;

import com.ferrite.dom.DOMNode;
import com.ferrite.dom.treewalker.TreeWalker;

public class TreeWalkerLoopInstruction implements TreeWalkerInstruction {
  private TreeWalkerInstruction[] instructions;
  private DOMNode condition;

  public TreeWalkerLoopInstruction(TreeWalkerInstruction[] instructions, DOMNode condition) {
    this.instructions = instructions;
    this.condition = condition;
  }

  @Override
  public void act(TreeWalker walker) {
    if (condition != null && walker.getPosition() != condition) {
      return;
    }
    walker.addLateInstruction(this);

    for (TreeWalkerInstruction instruction : this.instructions) {
      walker.sortedAddInstruction(instruction);
    }
  }

  @Override
  public boolean getLate() {
    return true;
  }
}
