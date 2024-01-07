package dom.exceptions;

import dom.NodeType;

public class DOMNodeRulePermittedChildDuplicationException extends DOMException {
  public DOMNodeRulePermittedChildDuplicationException(NodeType rootNodetype, NodeType childNodeType) {
    super(String.format("Tried to add '%s' as a permitted child type multiple times to '%s'", childNodeType.toString(), rootNodetype.toString()));
  }
}
