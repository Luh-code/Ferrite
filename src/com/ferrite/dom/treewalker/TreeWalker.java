package com.ferrite.dom.treewalker;

import com.ferrite.dom.DOMNode;
import com.ferrite.dom.NodeType;
import com.ferrite.dom.NodeVariant;
import com.ferrite.dom.exceptions.TreeWalker.TreeWalkerNoOriginStateFoundException;
import com.ferrite.dom.treewalker.instructions.TreeWalkerInstruction;

import java.util.ArrayDeque;

public class TreeWalker {
  private DOMNode position;
  private ArrayDeque<TreeWalkerInstruction> instructions;
  private boolean head;

  private TreeWalker(DOMNode position, boolean head) {
    this.position = position;
    this.head = head;
  }

  public static TreeWalker dispatchOnOrigin(DOMNode root) throws TreeWalkerNoOriginStateFoundException {
    OUTER:
    for (DOMNode node : root.getEdges()) {
      if (node.getType() != NodeType.STATE) {
        continue;
      }
      for (DOMNode node1 : node.getEdges()) {
        if (node1.getType() != NodeType.ORIGIN) {
          continue;
        }
        NodeVariant value = node1.getVariant();
        if (value == null) {
          break OUTER;
        }
        return new TreeWalker(node, true);
      }
    }

    throw new TreeWalkerNoOriginStateFoundException(root.getType().name());
  }

  public static TreeWalker customDispatch(DOMNode root) {
    return new TreeWalker(root, false);
  }

  public void addInstruction(TreeWalkerInstruction instruction) {
    this.instructions.push(instruction);
  }

  public DOMNode getPosition() {
    return position;
  }

  public void setPosition(DOMNode position) {
    this.position = position;
  }
}
