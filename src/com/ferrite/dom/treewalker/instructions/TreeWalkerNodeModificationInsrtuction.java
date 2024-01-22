package com.ferrite.dom.treewalker.instructions;

import com.ferrite.dom.DOMNode;
import com.ferrite.dom.NodeVariantType;
import com.ferrite.dom.exceptions.*;
import com.ferrite.dom.treewalker.TreeWalker;

import java.util.Optional;

public class TreeWalkerNodeModificationInsrtuction implements TreeWalkerInstruction {
  private final DOMNode[] nodes;
  private final boolean late;

  public TreeWalkerNodeModificationInsrtuction(DOMNode[] nodes, boolean late) {
    this.nodes = nodes;
    this.late = late;
  }

  @Override
  public void act(TreeWalker walker) {
    for (DOMNode node : nodes) {
      if (node.getVariant() == null || node.getVariant().getType() == NodeVariantType.NONE) {
        throw new RuntimeException("Cannot change value of a non-existent variant or a variant of type NONE");
      }

      Optional<DOMNode> node1 = walker.getPosition().getEdge(node.getType());
      if (node1.isEmpty()) {
        try {
          walker.getPosition().addEdge(node.clone());
        } catch (DOMNodeEdgeDuplicationException | DOMNodeRuleTypeViolationException |
                 DOMNodeRulePluralityViolationException | DOMNodeRuleNonExistentException | CloneNotSupportedException e) {
          throw new RuntimeException(e);
        }
        continue;
      }

      if (node1.get().getVariant() == null || node1.get().getVariant().getType() == NodeVariantType.NONE) {
        throw new RuntimeException("Cannot change value of a non-existent variant or a variant of type NONE");
      }

      if (node.getVariant().getType() != node1.get().getVariant().getType()) {
        throw new RuntimeException(String.format("Cannot set a variant of type '%s' to a variant of type '%s'", node1.get().getType(), node.getType()));
      }

      try {
        node1.get().getVariant().setVariant(node.getVariant());
      } catch (DOMNodeVariantTypeMismatchException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public boolean getLate() {
    return late;
  }
}
