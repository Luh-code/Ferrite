package com.ferrite.dom;

import com.ferrite.dom.exceptions.DOMNodeEdgeDuplicationException;
import com.ferrite.dom.exceptions.DOMNodeRuleNonExistentException;
import com.ferrite.dom.exceptions.DOMNodeRulePluralityViolationException;
import com.ferrite.dom.exceptions.DOMNodeRuleTypeViolationException;

import java.util.ArrayList;
import java.util.Optional;

public class XMLNode {
  private ArrayList<XMLNode> edges;
  private NodeType type;
  private NodeVariant variant;

  public XMLNode(NodeType type) {
    this.type = type;
    this.edges = new ArrayList<>();
  }

  public void addEdge(XMLNode n) throws DOMNodeEdgeDuplicationException, DOMNodeRuleTypeViolationException, DOMNodeRulePluralityViolationException, DOMNodeRuleNonExistentException {
    if (this.edges.contains(n)) {
      throw new DOMNodeEdgeDuplicationException(this, n);
    }
    if (!this.type.checkRule(n.getType())) {
      throw new DOMNodeRuleTypeViolationException(this, n);
    }
    if (!this.type.checkPlurality(n.getType())) {
      for (XMLNode vertex : edges) {
        if (vertex.getType() == n.getType()) {
          throw new DOMNodeRulePluralityViolationException(this, n);
        }
      }
    }
    edges.add(n);
  }

  public Optional<XMLNode> getEdge(NodeType type) {
    for (XMLNode edge : this.edges) {
      if (edge.getType() == type) {
        return Optional.of(edge);
      }
    }
    return Optional.empty();
  }

  public void replaceEdges(XMLNode newNode) throws DOMNodeEdgeDuplicationException, DOMNodeRuleNonExistentException, DOMNodeRuleTypeViolationException, DOMNodeRulePluralityViolationException {
    this.edges.clear();
    for (XMLNode n : newNode.getEdges()) {
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

  public ArrayList<XMLNode> getEdges() {
    return edges;
  }
}
