package com.ferrite.dom.treewalker.instructions;

import com.ferrite.dom.treewalker.TreeWalker;

public class TreeWalkerGetInstruction implements TreeWalkerInstruction {
  @Override
  public void act(TreeWalker walker) {
    TreeWalkerInstruction[] instructions = walker.getPosition().getInstructions();
    for (int i = instructions.length-1; i >= 0; --i) {
      walker.addInstruction(instructions[i]);
    }
  }
}
