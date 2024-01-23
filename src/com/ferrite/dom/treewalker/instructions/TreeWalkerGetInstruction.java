package com.ferrite.dom.treewalker.instructions;

import com.ferrite.dom.treewalker.TreeWalker;

import java.util.ArrayList;

public class TreeWalkerGetInstruction implements TreeWalkerInstruction {
  private boolean late;

  public TreeWalkerGetInstruction(boolean late) {
    this.late = late;
  }

  @Override
  public void act(TreeWalker walker) {
    TreeWalkerInstruction[] instructions = walker.getPosition().getInstructions(walker.getPosition());
    ArrayList<TreeWalkerInstruction> lateInstructions = new ArrayList<>();
    for (int i = instructions.length-1; i >= 0; --i) {
      TreeWalkerInstruction instruction = instructions[i];
      if (instruction.getLate()) {
        lateInstructions.addFirst(instruction);
        continue;
      }
      walker.addInstruction(instructions[i]);
    }
    for(TreeWalkerInstruction instruction : lateInstructions) {
      walker.addLateInstruction(instruction);
    }
  }

  @Override
  public boolean getLate() {
    return this.late;
  }
}
