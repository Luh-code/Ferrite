package com.ferrite.dom;

import com.ferrite.dom.exceptions.DOMNodeEdgeDuplicationException;
import com.ferrite.dom.exceptions.DOMNodeRuleNonExistentException;
import com.ferrite.dom.exceptions.DOMNodeRulePluralityViolationException;
import com.ferrite.dom.exceptions.DOMNodeRuleTypeViolationException;

import java.util.ArrayList;

public class Node {
  private ArrayList<Node> edges;
  private NodeType type;

  public Node(NodeType type) {
    this.type = type;
    this.edges = new ArrayList<>();
  }

  public void addEdge(Node n) throws DOMNodeEdgeDuplicationException, DOMNodeRuleTypeViolationException, DOMNodeRulePluralityViolationException, DOMNodeRuleNonExistentException {
    if (this.edges.contains(n)) {
      throw new DOMNodeEdgeDuplicationException(this, n);
    }
    if (!this.type.checkRule(n.getType())) {
      throw new DOMNodeRuleTypeViolationException(this, n);
    }
    if (!this.type.checkPlurality(n.getType())) {
      for (Node vertex : edges) {
        if (vertex.getType() == n.getType()) {
          throw new DOMNodeRulePluralityViolationException(this, n);
        }
      }
    }
    edges.add(n);
  }

  public NodeType getType() {
    return this.type;
  }
}
