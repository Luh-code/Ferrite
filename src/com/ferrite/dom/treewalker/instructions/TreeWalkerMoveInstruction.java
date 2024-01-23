package com.ferrite.dom.treewalker.instructions;

import com.ferrite.dom.DOMNode;
import com.ferrite.dom.treewalker.TreeWalker;

public class TreeWalkerMoveInstruction implements TreeWalkerInstruction {
  private DOMNode target;
  private int back;

  private boolean late;

  public TreeWalkerMoveInstruction(DOMNode target, int back, boolean late) {
    this.target = target;
    this.back = back;
    this.late = late;
  }

  @Override
  public void act(TreeWalker walker) {
    if (target != null) {
      walker.setPosition(target);
      return;
    }
    if (back > 0) {
      processPast(walker);
    }
  }

  @Override
  public boolean getLate() {
    return this.late;
  }

  private void processPast(TreeWalker walker) {
    DOMNode past = null;
    if (walker.pastSize() < back) {
      throw new RuntimeException(String.format("Tied to move %d steps back whilst only %d are available",
              back, walker.pastSize()));
    }
    for (int i = 0; i < back; ++i) {
      past = walker.popPast();
    }
    walker.setPosition(past);
  }
}
