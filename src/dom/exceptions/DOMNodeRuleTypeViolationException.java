package dom.exceptions;

import dom.Node;

public class DOMNodeRuleTypeViolationException extends DOMException {

  public DOMNodeRuleTypeViolationException(Node root, Node vertex) {
    super(String.format(
            "A NodeRule violation occurred, trying to create edge between node '%s' of type '%s' and node '%s' of type '%s'",
            root.toString(), root.getType().toString(), vertex.toString(), vertex.getType().toString()));
  }
}
