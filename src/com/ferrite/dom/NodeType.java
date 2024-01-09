package com.ferrite.dom;

import com.ferrite.dom.exceptions.DOMNodeRuleNonExistentException;
import com.ferrite.dom.exceptions.DOMNodeRulePermittedChildDuplicationException;

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

  /**
   * Returns the equivalent NodeType to the provided String, ignoring case
   * @param string String to be matched
   * @return The NodeType corresponding to the String, if not found null
   */
  public static NodeType fromString(String string) {
    for (NodeType value : NodeType.values()) {
      if (value.name().equalsIgnoreCase(string)) {
        return value;
      }
    }
    return null;
  }
}