import dom.Node;
import dom.NodeType;
import dom.exceptions.DOMNodeEdgeDuplicationException;
import dom.exceptions.DOMNodeRuleNonExistentException;
import dom.exceptions.DOMNodeRulePluralityViolationException;
import dom.exceptions.DOMNodeRuleTypeViolationException;

public class Main {
	public static void main(String[] args) {
		NodeType test = NodeType.MACHINE;
		Node a = new Node(NodeType.MACHINE);
		Node b = new Node(NodeType.STATE);
    try {
      a.addEdge(b);
    } catch (DOMNodeEdgeDuplicationException | DOMNodeRuleTypeViolationException |
             DOMNodeRulePluralityViolationException | DOMNodeRuleNonExistentException e) {
      throw new RuntimeException(e);
    }
  }
}