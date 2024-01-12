package com.ferrite.dom.treewalker;

import com.ferrite.dom.NodeType;
import com.ferrite.dom.XMLNode;
import com.ferrite.dom.treewalker.instructions.TreewalkerInstruction;

import java.util.ArrayDeque;
import java.util.Optional;

public class Treewalker {
  private XMLNode position;
  private ArrayDeque<TreewalkerInstruction> instructions;

  public Treewalker(XMLNode root) {
    dispatchOnOrigin(root);
  }

  private void dispatchOnOrigin(XMLNode root) {
    for (XMLNode node : root.getEdges()) {
      if (node.getType() != NodeType.STATE) {
        continue;
      }
      for (XMLNode node1 : node.getEdges()) {
        if (node1.getType() != NodeType.ORIGIN) {
          continue;
        }
        for (XMLNode node2 : node1.getEdges()) {
          if (node2.getType() != NodeType.VALUE) {
            continue;
          }

        }
      }
    }
  }
}
