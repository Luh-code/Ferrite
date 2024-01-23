package com.ferrite.dom.treewalker.instructions;

import com.ferrite.dom.DOMNode;
import com.ferrite.dom.NodeType;
import com.ferrite.dom.NodeVariant;
import com.ferrite.dom.exceptions.*;
import com.ferrite.dom.exceptions.query.QueryEmptyResultException;
import com.ferrite.dom.exceptions.query.QueryInvalidSyntaxException;
import com.ferrite.dom.query.QueryEngine;
import com.ferrite.dom.treewalker.TreeWalker;

import java.util.ArrayList;
import java.util.Optional;

public class TreeWalkerIOUpdateInstruction implements TreeWalkerInstruction {
  private final boolean late;

  public TreeWalkerIOUpdateInstruction(boolean late) {
    this.late = late;
  }

  @Override
  public void act(TreeWalker walker) {
    QueryEngine qe = new QueryEngine();

    // get all triggers
    try {
      qe.queryTop(walker.getPosition(), "FROM trigger");
    } catch (QueryInvalidSyntaxException e) {
      throw new RuntimeException(e);
    } catch (QueryEmptyResultException ignored) { }
    ArrayList<DOMNode> triggers = qe.getSelected();

    // set all digital triggers to updated values
    for (DOMNode trigger : triggers) {
      Optional<DOMNode> type = trigger.getEdge(NodeType.TYPE);
      if (type.isEmpty()) {
        throw new RuntimeException("A trigger needs a type");
      }
      try {
        if (!type.get().getVariant().getString().equalsIgnoreCase("digital")) {
          continue;
        }
      } catch (DOMNodeVariantTypeMismatchException e) {
        throw new RuntimeException(e);
      }

      Optional<DOMNode> alias = trigger.getEdge(NodeType.ALIAS);
      if (alias.isEmpty()) {
        throw new RuntimeException("A trigger needs to be aliased");
      }
      int input = -1;
      try {
         input = Integer.parseInt(alias.get().getVariant().getString());
      } catch (DOMNodeVariantTypeMismatchException e) {
        throw new RuntimeException(e);
      }

      Optional<DOMNode> value = trigger.getEdge(NodeType.VALUE);
      if (value.isEmpty()) {
        DOMNode temp = new DOMNode(NodeType.VALUE);
        temp.setVariant(new NodeVariant("false"));
        try {
          trigger.addEdge(temp);
        } catch (DOMNodeEdgeDuplicationException | DOMNodeRuleTypeViolationException |
                 DOMNodeRulePluralityViolationException | DOMNodeRuleNonExistentException e) {
          throw new RuntimeException(e);
        }
        value = Optional.of(temp);
      }
      try {
        value.get().getVariant().setString(String.valueOf(walker.getController().getInput(input)));
      } catch (DOMNodeVariantTypeMismatchException e) {
        throw new RuntimeException(e);
      }
    }

    // get all outputs
    try {
      qe.queryTop(walker.getPosition(), "FROM output");
    } catch (QueryInvalidSyntaxException e) {
      throw new RuntimeException(e);
    } catch (QueryEmptyResultException ignored) { }
    ArrayList<DOMNode> outputs = qe.getSelected();

    // set all digital outputs to updated values
    for (DOMNode output : outputs) {
      Optional<DOMNode> type = output.getEdge(NodeType.TYPE);
      if (type.isEmpty()) {
        throw new RuntimeException("An output needs a type");
      }
      try {
        if (!type.get().getVariant().getString().equalsIgnoreCase("digital")) {
          continue;
        }
      } catch (DOMNodeVariantTypeMismatchException e) {
        throw new RuntimeException(e);
      }

      Optional<DOMNode> alias = output.getEdge(NodeType.ALIAS);
      if (alias.isEmpty()) {
        throw new RuntimeException("An output needs to be aliased");
      }
      int outputBit = -1;
      try {
        outputBit = Integer.parseInt(alias.get().getVariant().getString());
      } catch (DOMNodeVariantTypeMismatchException e) {
        throw new RuntimeException(e);
      }

      Optional<DOMNode> value = output.getEdge(NodeType.VALUE);
      if (value.isEmpty()) {
        DOMNode temp = new DOMNode(NodeType.VALUE);
        try {
          output.addEdge(temp);
        } catch (DOMNodeEdgeDuplicationException | DOMNodeRuleTypeViolationException |
                 DOMNodeRulePluralityViolationException | DOMNodeRuleNonExistentException e) {
          throw new RuntimeException(e);
        }
        value = Optional.of(temp);
      }
      try {
        walker.getController().setOutput(outputBit, Boolean.parseBoolean(value.get().getVariant().getString()));
      } catch (DOMNodeVariantTypeMismatchException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public boolean getLate() {
    return this.late;
  }
}
