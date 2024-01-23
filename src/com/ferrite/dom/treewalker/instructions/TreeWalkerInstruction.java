package com.ferrite.dom.treewalker.instructions;

import com.ferrite.dom.treewalker.TreeWalker;

public interface TreeWalkerInstruction {
  void act(TreeWalker walker);

  boolean getLate();
}
