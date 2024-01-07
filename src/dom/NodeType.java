package dom;

import dom.exceptions.DOMNodeRuleNonExistentException;
import dom.exceptions.DOMNodeRulePermittedChildDuplicationException;

public enum NodeType {
  SYSTEM(Rules.SYSTEM),

  FERRITE_MAJOR(Rules.FERRITE_MAJOR),

  FERRITE_MINOR(Rules.FERRITE_MINOR),

  MAJOR(Rules.MAJOR),

  MINOR(Rules.MINOR),

  ALIAS(Rules.ALIAS),

  MACHINE(Rules.MACHINE),

  EXTERNAL(Rules.EXTERNAL),

  PATH(Rules.PATH),

  STATE(Rules.STATE),

  TRIGGER(Rules.TRIGGER),

  OUTPUT(Rules.OUTPUT),

  ORIGIN(Rules.ORIGIN),

  ENTRY(Rules.ENTRY),

  BEGIN(Rules.BEGIN),

  TRANSITION(Rules.TRANSITION),

  TYPE(Rules.TYPE),

  IF(Rules.IF),

  EQUALS(Rules.EQUALS),

  LESSER(Rules.LESSER),

  GREATER(Rules.GREATER),

  VALUE(Rules.VALUE),

  QUERY(Rules.QUERY),

  CUSTOM(Rules.CUSTOM); // Rule should later be set to whatever is appropriate in sanitization step!

  private Rules rules;

  NodeType(Rules rules) {
    // Pre-sanitization step
    this.rules = rules;
  }

  public NodeRule getRule() {
    try {
      return new NodeRule(this, this.rules.getSettings());
    } catch (DOMNodeRulePermittedChildDuplicationException e) {
      throw new RuntimeException(e);
    }
  }

  // Checks if a node is allowed as a child
  public boolean checkRule(NodeType node) {
    for (NodeSettings nodeSettings : this.getRule().getPermittedChildren()) {
      if (nodeSettings.getNodeType() == node) {
        return true;
      }
    }
    return false;
  }

  public boolean checkPlurality(NodeType node) throws DOMNodeRuleNonExistentException {
    for (NodeSettings nodeSettings : this.getRule().getPermittedChildren()) {
      if (nodeSettings.getNodeType() == node) {
        return nodeSettings.isArrayable();
      }
    }
    throw new DOMNodeRuleNonExistentException(this, node);
  }
}
