package com.ferrite.dom;

import com.ferrite.dom.exceptions.*;
import com.ferrite.dom.treewalker.instructions.TreeWalkerInstruction;

import java.util.ArrayList;
import java.util.Optional;

public class DOMNode implements Cloneable {
  private ArrayList<DOMNode> edges;
  private NodeType type;
  private NodeVariant variant;
  private DOMNode root;

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
    for (DOMNode edge : getEdges()) {
      if (edge.getType() == type) {
        return Optional.of(edge);
      }
    }
    return Optional.empty();
  }

  public ArrayList<DOMNode> getEdges(NodeType type) {
    ArrayList<DOMNode> nodes = new ArrayList<>();
    for (DOMNode edge : getEdges()) {
      if (edge.getType() == type) {
        nodes.add(edge);
      }
    }
    return nodes;
  }

  public String getName() {
    Optional<DOMNode> rootAlias = getRoot().getEdge(NodeType.ALIAS);
    if (rootAlias.isEmpty()) {
      throw new RuntimeException("Root must have an alias");
    }
    Optional<DOMNode> alias = getEdge(NodeType.ALIAS);
    if (alias.isEmpty()) {
      try {
        return rootAlias.get().getVariant().getString()+"/"+this+"~"+this.getType().toString();
      } catch (DOMNodeVariantTypeMismatchException e) {
        throw new RuntimeException(e);
      }
    }
    try {
      return rootAlias.get().getVariant().getString()+"/"+alias.get().getVariant().getString()+"~"+this.getType().toString();
    } catch (DOMNodeVariantTypeMismatchException e) {
      throw new RuntimeException(e);
    }
  }

  public void replaceEdges(DOMNode newNode) throws DOMNodeEdgeDuplicationException, DOMNodeRuleNonExistentException, DOMNodeRuleTypeViolationException, DOMNodeRulePluralityViolationException {
    this.edges.clear();
    for (DOMNode n : newNode.getEdges()) {
      addEdge(n);
      n.setRoot(this);
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
    return (ArrayList<DOMNode>) edges.clone();
  }

  public TreeWalkerInstruction[] getInstructions(DOMNode node) {
    return this.type.getInstructions(node);
  }

  public DOMNode getRoot() {
    return root;
  }

  public void setRoot(DOMNode root) {
    this.root = root;
  }

  @Override
  public DOMNode clone() throws CloneNotSupportedException {
    return (DOMNode) super.clone();
  }
}
