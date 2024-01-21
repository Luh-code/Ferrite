package com.ferrite.dom;

import com.ferrite.dom.exceptions.DOMNodeRuleNonExistentException;
import com.ferrite.dom.exceptions.DOMNodeRulePermittedChildDuplicationException;
import com.ferrite.dom.treewalker.instructions.TreeWalkerInstruction;

import java.util.ArrayList;
import java.util.List;

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

  PROXY(Rules.PROXY),

  TRIGGER(Rules.TRIGGER),

  OUTPUT(Rules.OUTPUT),

  ACTIVE(Rules.ACTIVE),

  TIME(Rules.TIME),

  RUNNING(Rules.RUNNING),

  ORIGIN(Rules.ORIGIN),

  ENTRY(Rules.ENTRY),

  BEGIN(Rules.BEGIN),

  END(Rules.END),

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
    ArrayList<NodeSettings> settings = new ArrayList<>(List.of(this.rules.getSettings()));
    for (Rules r : this.rules.getExtensions().get()) {
      for (NodeSettings s : r.getSettings()){
        if (!settings.contains(s)) {
          settings.add(s);
        }
      }
    }
    try {
      return new NodeRule(this, settings.toArray(NodeSettings[]::new), this.rules.getNodeVariantType());
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

  public TreeWalkerInstruction[] getInstructions(DOMNode node) {
    ArrayList<TreeWalkerInstruction> instructions = new ArrayList<>();
    for (Rules r : this.rules.getExtensions().get()) {
      instructions.addAll(List.of(r.applyInstructions(node)));
    }
    instructions.addAll(List.of(this.rules.applyInstructions(node)));
    return instructions.toArray(TreeWalkerInstruction[]::new);
  }
}
