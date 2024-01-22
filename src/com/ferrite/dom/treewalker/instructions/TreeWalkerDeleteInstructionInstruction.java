package com.ferrite.dom.treewalker.instructions;

import com.ferrite.dom.treewalker.TreeWalker;

public class TreeWalkerDeleteInstructionInstruction implements TreeWalkerInstruction {
  private int count;
  private boolean late;

  public TreeWalkerDeleteInstructionInstruction(int count, boolean late) {
    if (count < 0 ) {
      throw new IllegalArgumentException("Count cannot be < 0");
    }
    this.count = count;
    this.late = late;
  }

  @Override
  public void act(TreeWalker walker) {
    if (late) {
      if (count > walker.lateInstructionCount()) {
        throw new RuntimeException("Tried to delete more late instructions, than exist");
      }
      for (int i = 0; i < count; i++) {
        walker.popLateInstruction();
      }
    }
    if (count > walker.instructionCount()) {
      throw new RuntimeException("Tried to delete more instructions, than exist");
    }
    for (int i = 0; i < count; i++) {
      walker.popInstruction();
    }
  }

  @Override
  public boolean getLate() {
    return false;
  }
}
