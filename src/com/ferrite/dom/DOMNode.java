package com.ferrite.dom;

import com.ferrite.dom.exceptions.DOMNodeEdgeDuplicationException;
import com.ferrite.dom.exceptions.DOMNodeRuleNonExistentException;
import com.ferrite.dom.exceptions.DOMNodeRulePluralityViolationException;
import com.ferrite.dom.exceptions.DOMNodeRuleTypeViolationException;
import com.ferrite.dom.treewalker.instructions.TreeWalkerInstruction;

import java.util.ArrayList;
import java.util.Optional;

public class DOMNode {
  private ArrayList<DOMNode> edges;
  private NodeType type;
  private NodeVariant variant;

  public DOMNode(NodeType type) {
    this.type = type;
    this.edges = new ArrayList<>();
  }

  public void addEdge(DOMNode n) throws DOMNodeEdgeDuplicationException, DOMNodeRuleTypeViolationException, DOMNodeRulePluralityViolationException, DOMNodeRuleNonExistentException {
    if (this.edges.contains(n)) {
      throw new DOMNodeEdgeDuplicationException(this, n);
    }
    if (!this.type.checkRule(n.getType())) {
      throw new DOMNodeRuleTypeViolationException(this, n);
    }
    if (!this.type.checkPlurality(n.getType())) {
      for (DOMNode vertex : edges) {
        if (vertex.getType() == n.getType()) {
          throw new DOMNodeRulePluralityViolationException(this, n);
        }
      }
    }
    edges.add(n);
  }

  public Optional<DOMNode> getEdge(NodeType type) {
    for (DOMNode edge : this.edges) {
      if (edge.getType() == type) {
        return Optional.of(edge);
      }
    }
    return Optional.empty();
  }

  public ArrayList<DOMNode> getEdges(NodeType type) {
    ArrayList<DOMNode> nodes = new ArrayList<>();
    for (DOMNode edge : this.edges) {
      if (edge.getType() == type) {
        nodes.add(edge);
      }
    }
    return nodes;
  }

  public void replaceEdges(DOMNode newNode) throws DOMNodeEdgeDuplicationException, DOMNodeRuleNonExistentException, DOMNodeRuleTypeViolationException, DOMNodeRulePluralityViolationException {
    this.edges.clear();
    for (DOMNode n : newNode.getEdges()) {
      addEdge(n);
    }
  }

  public NodeType getType() {
    return this.type;
  }

  public NodeVariant getVariant() {
    return variant;
  }

  public void setVariant(NodeVariant variant) {
    this.variant = variant;
  }

  public ArrayList<DOMNode> getEdges() {
    return edges;
  }

  public TreeWalkerInstruction[] getInstructions(DOMNode node) {
    return this.type.getInstructions(node);
  }
}
