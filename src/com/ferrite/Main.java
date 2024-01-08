package com.ferrite;

import com.ferrite.dom.Node;
import com.ferrite.dom.NodeType;
import com.ferrite.dom.exceptions.DOMNodeEdgeDuplicationException;
import com.ferrite.dom.exceptions.DOMNodeRuleNonExistentException;
import com.ferrite.dom.exceptions.DOMNodeRulePluralityViolationException;
import com.ferrite.dom.exceptions.DOMNodeRuleTypeViolationException;

public class Main {
	public static void main(String[] args) {
		Node a = new Node(NodeType.TRANSITION);
		Node b = new Node(NodeType.STATE);
    Node c = new Node(NodeType.STATE);
    try {
      a.addEdge(b);
      //a.addEdge(c);
    } catch (DOMNodeEdgeDuplicationException | DOMNodeRuleTypeViolationException |
             DOMNodeRulePluralityViolationException | DOMNodeRuleNonExistentException e) {
      throw new RuntimeException(e);
    }
  }
}