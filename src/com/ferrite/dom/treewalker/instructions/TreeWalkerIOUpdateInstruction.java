package com.ferrite.dom.treewalker.instructions;

import com.ferrite.controller.Controller;
import com.ferrite.dom.DOMNode;
import com.ferrite.dom.NodeType;
import com.ferrite.dom.NodeVariant;
import com.ferrite.dom.exceptions.*;
import com.ferrite.dom.exceptions.query.QueryEmptyResultException;
import com.ferrite.dom.exceptions.query.QueryInvalidSyntaxException;
import com.ferrite.dom.query.QueryEngine;
import com.ferrite.dom.treewalker.TreeWalker;
import com.sun.source.tree.Tree;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Vector;

public class TreeWalkerIOUpdateInstruction implements TreeWalkerInstruction {
  private final boolean late;
  private TreeWalker walker;

  public TreeWalkerIOUpdateInstruction(boolean late) {
    this.late = late;
  }

  private void handleDigitalTrigger(DOMNode trigger) {
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

  private void handleTimerTrigger(DOMNode trigger) {
    Optional<DOMNode> alias = trigger.getEdge(NodeType.ALIAS);
    if (alias.isEmpty()) {
      throw new RuntimeException("A trigger needs to be aliased");
    }

    String timerKey = trigger.getName();

    Optional<DOMNode> timeout = trigger.getEdge(NodeType.VALUE);
    if (timeout.isEmpty()) {
      DOMNode temp = new DOMNode(NodeType.VALUE);
      temp.setVariant(new NodeVariant("0"));
      try {
        trigger.addEdge(temp);
      } catch (DOMNodeEdgeDuplicationException | DOMNodeRuleTypeViolationException |
               DOMNodeRulePluralityViolationException | DOMNodeRuleNonExistentException e) {
        throw new RuntimeException(e);
      }
      timeout = Optional.of(temp);
    }

    if (!walker.getController().checkIfTimerExists(timerKey)) {
      walker.getController().createTimer(timerKey);
    }

    try {
      if (Integer.parseInt(timeout.get().getVariant().getString()) > 1 && walker.getController().checkTimer(timerKey) == 0 ) {
        walker.getController().startTimer(timerKey, Integer.parseInt(timeout.get().getVariant().getString()));
      }
    } catch (DOMNodeVariantTypeMismatchException e) {
      throw new RuntimeException(e);
    }

    try {
      timeout.get().getVariant().setString(String.valueOf(walker.getController().checkTimer(timerKey)));
    } catch (DOMNodeVariantTypeMismatchException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void act(TreeWalker walker) {
    this.walker = walker;
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
        switch (type.get().getVariant().getString().toLowerCase()) {
          case "digital" -> {
            handleDigitalTrigger(trigger);
          }
          case "timer" -> {
            handleTimerTrigger(trigger);
          }
        }
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

    walker.print(String.format("Input: %s", Controller.bitSetToString(walker.getController().getInput())));
    walker.print(String.format("Output: %s", Controller.bitSetToString(walker.getController().getOutput())));
    walker.print(String.format("Timer: %s", Controller.timerArrayToString(walker.getController().getTimer())));
  }

  @Override
  public boolean getLate() {
    return this.late;
  }
}
