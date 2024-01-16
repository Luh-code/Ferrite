package com.ferrite;

import com.ferrite.dom.DOMNode;
import com.ferrite.dom.NodeType;
import com.ferrite.dom.exceptions.DOMNodeEdgeDuplicationException;
import com.ferrite.dom.exceptions.DOMNodeRuleNonExistentException;
import com.ferrite.dom.exceptions.DOMNodeRulePluralityViolationException;
import com.ferrite.dom.exceptions.DOMNodeRuleTypeViolationException;

public class Main {
	public static void main(String[] args) {
		DOMNode a = new DOMNode(NodeType.TRANSITION);
		DOMNode b = new DOMNode(NodeType.STATE);
    DOMNode c = new DOMNode(NodeType.STATE);
    try {
      a.addEdge(b);
      //a.addEdge(c);
    } catch (DOMNodeEdgeDuplicationException | DOMNodeRuleTypeViolationException |
             DOMNodeRulePluralityViolationException | DOMNodeRuleNonExistentException e) {
      throw new RuntimeException(e);
    }
  }
}