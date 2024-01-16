package com.ferrite.dom.treewalker.instructions;

import com.ferrite.dom.DOMNode;
import com.ferrite.dom.NodeType;
import com.ferrite.dom.treewalker.TreeWalker;

public class TreeWalkerSearchInstruction implements TreeWalkerInstruction {
  private NodeType target;

  public TreeWalkerSearchInstruction(NodeType target) {
    this.target = target;
  }

  @Override
  public void act(TreeWalker walker) {
    for (DOMNode node : walker.getPosition().getEdges()) {
      if (node.getType() != )
    }
  }
}
