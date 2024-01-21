package com.ferrite.dom.treewalker.instructions;

import com.ferrite.dom.treewalker.TreeWalker;

public class TreeWalkerDemoInstruction implements TreeWalkerInstruction {
  private String message;

  private boolean late;

  public TreeWalkerDemoInstruction(boolean late) {
    this.late = late;
  }

  public TreeWalkerDemoInstruction(String message) {
    this.message = message;
  }
  @Override
  public void act(TreeWalker walker) {
    walker.print(message);
  }

  @Override
  public boolean getLate() {
    return this.late;
  }
}
