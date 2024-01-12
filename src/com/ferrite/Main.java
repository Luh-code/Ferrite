package com.ferrite;

import com.ferrite.dom.XMLNode;
import com.ferrite.dom.NodeType;
import com.ferrite.dom.exceptions.DOMNodeEdgeDuplicationException;
import com.ferrite.dom.exceptions.DOMNodeRuleNonExistentException;
import com.ferrite.dom.exceptions.DOMNodeRulePluralityViolationException;
import com.ferrite.dom.exceptions.DOMNodeRuleTypeViolationException;

public class Main {
	public static void main(String[] args) {
		XMLNode a = new XMLNode(NodeType.TRANSITION);
		XMLNode b = new XMLNode(NodeType.STATE);
    XMLNode c = new XMLNode(NodeType.STATE);
    try {
      a.addEdge(b);
      //a.addEdge(c);
    } catch (DOMNodeEdgeDuplicationException | DOMNodeRuleTypeViolationException |
             DOMNodeRulePluralityViolationException | DOMNodeRuleNonExistentException e) {
      throw new RuntimeException(e);
    }
  }
}