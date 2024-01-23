package com.ferrite.dom;

import com.ferrite.dom.exceptions.*;
import com.ferrite.dom.exceptions.TreeWalker.TreeWalkerException;
import com.ferrite.dom.exceptions.TreeWalker.TreeWalkerNodeNotFoundException;
import com.ferrite.dom.exceptions.query.QueryEmptyResultException;
import com.ferrite.dom.exceptions.query.QueryInvalidSyntaxException;
import com.ferrite.dom.query.QueryEngine;
import com.ferrite.dom.treewalker.instructions.*;
import org.w3c.dom.Node;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.ferrite.dom.NodeVariantType.*;

// Sub-enum for soc for setting the rule of each NodeType
// Not sanitized at compile time! Sanitization occurs at runtime whilst tree construction.
// NodeType.CUSTOM undergoes a post-sanitization
enum Rules {
  GENERAL(NONE,() -> new Rules[]{}, () -> new NodeSettings[]{
          new NodeSettings(NodeType.EXTERNAL),
          new NodeSettings(NodeType.PATH),
          new NodeSettings(NodeType.QUERY)
  }, (DOMNode node) -> new TreeWalkerInstruction[]{}),
  VERSIONED(NONE, () -> new Rules[]{}, () -> new NodeSettings[]{
          new NodeSettings(NodeType.FERRITE_MAJOR),
          new NodeSettings(NodeType.FERRITE_MINOR),
          new NodeSettings(NodeType.MAJOR),
          new NodeSettings(NodeType.MINOR),
  }, (DOMNode node) -> new TreeWalkerInstruction[]{}),
  ALIASED(NONE, () -> new Rules[]{}, () -> new NodeSettings[]{
          new NodeSettings(NodeType.ALIAS)
  }, (DOMNode node) -> new TreeWalkerInstruction[]{}),
  SYSTEM(NONE,() -> new Rules[]{ GENERAL, VERSIONED, ALIASED }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.MACHINE).setArrayable(),
          new NodeSettings(NodeType.STATE).setArrayable()
  }, (DOMNode node) -> new TreeWalkerInstruction[]{
          new TreeWalkerSearchInstruction("FROM state GET 'origin'=='true'", false, false),
          new TreeWalkerGetInstruction(false)
  }),
  FERRITE_MAJOR(INTEGER,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}, (DOMNode node) -> new TreeWalkerInstruction[]{}),
  FERRITE_MINOR(INTEGER,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}, (DOMNode node) -> new TreeWalkerInstruction[]{}),
  MAJOR(INTEGER,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}, (DOMNode node) -> new TreeWalkerInstruction[]{}),
  MINOR(INTEGER,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}, (DOMNode node) -> new TreeWalkerInstruction[]{}),
  ALIAS(STRING,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}, (DOMNode node) -> new TreeWalkerInstruction[]{}),
  MACHINE(NONE,() -> new Rules[]{ GENERAL, VERSIONED, ALIASED }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.TRIGGER).setArrayable(),
          new NodeSettings(NodeType.OUTPUT).setArrayable(),
          new NodeSettings(NodeType.STATE).setArrayable()
  }, (DOMNode node) -> new TreeWalkerInstruction[]{}),
  EXTERNAL(BOOLEAN,() -> new Rules[]{ }, () -> new NodeSettings[]{}, (DOMNode node) -> new TreeWalkerInstruction[]{}),
  PATH(STRING,() -> new Rules[]{ }, () -> new NodeSettings[]{}, (DOMNode node) -> new TreeWalkerInstruction[]{}),
  STATE(NONE,() -> new Rules[]{ GENERAL, ALIASED }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.ORIGIN),
          new NodeSettings(NodeType.ENTRY),
          new NodeSettings(NodeType.BEGIN),
          new NodeSettings(NodeType.END),
          new NodeSettings(NodeType.TRANSITION).setArrayable(),
          new NodeSettings(NodeType.STATE)
  }, (DOMNode node) -> {
    ArrayList<TreeWalkerInstruction> instructions = new ArrayList<>();

    // go to begin
    Optional<DOMNode> begin = node.getEdge(NodeType.BEGIN);
    if (begin.isEmpty()) {
      throw new RuntimeException("State needs to have a child of type BEGIN");
    }
    instructions.add(new TreeWalkerMoveInstruction(begin.get(), 0, false));
    instructions.add(new TreeWalkerGetInstruction(false));
    // begin is expected to move up itself

    // LATE INSTRUCTIONS (inverted ORDER )

    /*// go to end
    Optional<DOMNode> end = node.getEdge(NodeType.END);
    if (end.isEmpty()) {
      throw new RuntimeException("State needs to have a child of type END");
    }
    instructions.add(new TreeWalkerGetInstruction(true));
    instructions.add(new TreeWalkerMoveInstruction(end.get(), 0, true));
    // end is expected to move up itself*/

    /*Optional<DOMNode> state = node.getEdge(NodeType.STATE);
    if (state.isEmpty()) {
      throw new RuntimeException("State needs to have a child of type STATE");
    }
    instructions.add(new TreeWalkerMoveInstruction(state.get(), 0, true));
    instructions.add(new TreeWalkerGetInstruction(true));*/

    ArrayList<TreeWalkerInstruction> transitonLoopInstructuions = new ArrayList<>();

    ArrayList<DOMNode> transitions = node.getEdges(NodeType.TRANSITION);
    for (DOMNode transition : transitions) {
      transitonLoopInstructuions.add(new TreeWalkerGetInstruction(false));
      transitonLoopInstructuions.add(new TreeWalkerMoveInstruction(transition, 0, false));
      transitonLoopInstructuions.add(new TreeWalkerIOUpdateInstruction(false));
      // transition is expected to move up itself
    }

    instructions.add(new TreeWalkerLoopInstruction(transitonLoopInstructuions.toArray(TreeWalkerInstruction[]::new), new TreeWalkerInstruction[]{
            new TreeWalkerGetInstruction(false),
    }, node));


    return instructions.toArray(TreeWalkerInstruction[]::new);
  }),
  TRIGGER(NONE,() -> new Rules[]{ GENERAL, ALIASED }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.TYPE),
          new NodeSettings(NodeType.CUSTOM).setArrayable(),
          new NodeSettings(NodeType.ACTIVE),
          new NodeSettings(NodeType.TIME),
          new NodeSettings(NodeType.RUNNING),
          new NodeSettings(NodeType.VALUE)
  }, (DOMNode node) -> new TreeWalkerInstruction[]{}),
  OUTPUT(NONE,() -> new Rules[]{ GENERAL, ALIASED }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.TYPE),
          new NodeSettings(NodeType.CUSTOM).setArrayable(),
          new NodeSettings(NodeType.ACTIVE),
          new NodeSettings(NodeType.TIME),
          new NodeSettings(NodeType.RUNNING),
          new NodeSettings(NodeType.VALUE)
  }, (DOMNode node) -> new TreeWalkerInstruction[]{}),
  ACTIVE(STRING, () -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}, (DOMNode node) -> new TreeWalkerInstruction[]{}),
  TIME(FLOAT,  () -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}, (DOMNode node) -> new TreeWalkerInstruction[]{}),
  RUNNING(BOOLEAN, () -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}, (DOMNode node) -> new TreeWalkerInstruction[]{}),
  ORIGIN(BOOLEAN,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}, (DOMNode node) -> new TreeWalkerInstruction[]{}),
  ENTRY(BOOLEAN,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}, (DOMNode node) -> new TreeWalkerInstruction[]{}),
  BEGIN(NONE,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{
        new NodeSettings(NodeType.TRIGGER).setArrayable(),
        new NodeSettings(NodeType.OUTPUT).setArrayable(),
  }, (DOMNode node) -> {
    ArrayList<TreeWalkerInstruction> instructions = new ArrayList<>();

    instructions.add(new TreeWalkerIOUpdateInstruction(false));

    ArrayList<TreeWalkerInstruction> modificationInstructions = new ArrayList<>();
    int backs = 0;
    for (DOMNode edge : node.getEdges()) {
      Optional<DOMNode> query = edge.getEdge(NodeType.QUERY);
      if (query.isEmpty()) {
        continue;
      }
      QueryEngine qe = new QueryEngine();
      try {
        qe.queryTop(node, query.get().getVariant().getString());
      } catch (QueryInvalidSyntaxException | QueryEmptyResultException | DOMNodeVariantTypeMismatchException e) {
        throw new RuntimeException(e);
      }
      DOMNode queriedNode = qe.getResult();

      ArrayList<DOMNode> allButQuery = edge.getEdges();
      allButQuery.remove(query.get());
      modificationInstructions.add(new TreeWalkerMoveInstruction(null, 1, true)); // 3
      modificationInstructions.add(new TreeWalkerNodeModificationInsrtuction(allButQuery.toArray(DOMNode[]::new), true)); // 2
      modificationInstructions.add(new TreeWalkerMoveInstruction(queriedNode, 0, true)); // 1
      backs++;
    }

    instructions.add(new TreeWalkerMoveInstruction(null, 1+backs, true)); // 4
    instructions.addAll(modificationInstructions);

    return instructions.toArray(TreeWalkerInstruction[]::new);
  }),
  END(NONE, () -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{
        new NodeSettings(NodeType.TRIGGER).setArrayable(),
        new NodeSettings(NodeType.OUTPUT).setArrayable(),
  }, (DOMNode node) -> {
    ArrayList<TreeWalkerInstruction> instructions = new ArrayList<>();

    instructions.add(new TreeWalkerIOUpdateInstruction(false));

    ArrayList<TreeWalkerInstruction> modificationInstructions = new ArrayList<>();
    for (DOMNode edge : node.getEdges()) {
      Optional<DOMNode> query = edge.getEdge(NodeType.QUERY);
      if (query.isEmpty()) {
        continue;
      }
      QueryEngine qe = new QueryEngine();
      try {
        qe.queryTop(node, query.get().getVariant().getString());
      } catch (QueryInvalidSyntaxException | QueryEmptyResultException | DOMNodeVariantTypeMismatchException e) {
        throw new RuntimeException(e);
      }
      DOMNode queriedNode = qe.getResult();

      ArrayList<DOMNode> allButQuery = edge.getEdges();
      allButQuery.remove(query.get());
      modificationInstructions.add(new TreeWalkerMoveInstruction(node, 0, true)); // 3
      modificationInstructions.add(new TreeWalkerNodeModificationInsrtuction(allButQuery.toArray(DOMNode[]::new), true)); // 2
      modificationInstructions.add(new TreeWalkerMoveInstruction(queriedNode, 0, true)); // 1
    }

    instructions.add(new TreeWalkerIOUpdateInstruction(true));

    instructions.add(new TreeWalkerMoveInstruction(null, 1, true)); // 4
    instructions.addAll(modificationInstructions);

    return instructions.toArray(TreeWalkerInstruction[]::new);
  }),
  TRANSITION(NONE,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.STATE),
          new NodeSettings(NodeType.IF).setArrayable()
  }, (DOMNode node) -> {
    ArrayList<TreeWalkerInstruction> instructions = new ArrayList<>();

    // Check conditions via ifs
    boolean res = false;
    for (DOMNode edge : node.getEdges()) {
      TreeWalkerMarkerInstruction temp = null;
      switch (edge.getType()) {
        case IF -> {
          temp = (TreeWalkerMarkerInstruction) edge.getInstructions(edge)[0];
        }
      }
      if (temp != null) {
        if (temp.getValue()) {
          res = true;
          break;
        }
      }
    }

    // Transition state into the destination state is condition is met
    if (res) {
      Optional<DOMNode> state = node.getEdge(NodeType.STATE);
      if (state.isEmpty()) {
        throw new RuntimeException("Invalid transition: no destination state defined");
      } else {
        Optional<DOMNode> query = state.get().getEdge(NodeType.QUERY);
        if (query.isEmpty()) {
          throw new RuntimeException("Invalid transition: the destination state should not be directly defined, but referenced via a query");
        }
        try {
          instructions.add(new TreeWalkerSearchInstruction(query.get().getVariant().getString(), true, true));
        } catch (DOMNodeVariantTypeMismatchException e) {
          throw new RuntimeException(e);
        }
      }
    }

    instructions.add(new TreeWalkerGetInstruction(true));
    instructions.add(new TreeWalkerSearchInstruction("FROM end", false, true));
    instructions.add(new TreeWalkerMoveInstruction(null, 1, true));

    return instructions.toArray(TreeWalkerInstruction[]::new);
  }),
  TYPE(STRING,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}, (DOMNode node) -> new TreeWalkerInstruction[]{}),
  IF(NONE,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.EQUALS).setArrayable(),
          new NodeSettings(NodeType.NOT_EQUALS).setArrayable(),
          new NodeSettings(NodeType.LESSER).setArrayable(),
          new NodeSettings(NodeType.GREATER).setArrayable(),
          new NodeSettings(NodeType.VALUE)
  }, (DOMNode node) -> {
    boolean res = false;
    for (DOMNode edge : node.getEdges()) {
      TreeWalkerMarkerInstruction temp = null;
      switch (edge.getType()) {
        case EQUALS, NOT_EQUALS, LESSER, GREATER, VALUE -> {
          temp = (TreeWalkerMarkerInstruction) edge.getInstructions(edge)[0];
        }
      }
      if (temp != null) {
        if (!temp.getValue()) {
          res = false;
          break;
        }
        res = true;
      }
    }

    return new TreeWalkerMarkerInstruction[] { new TreeWalkerMarkerInstruction(res) };
  }),
  EQUALS(NONE,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.TRIGGER),
          new NodeSettings(NodeType.VALUE)
  }, (DOMNode node) -> {
    Optional<DOMNode> trigger = node.getEdge(NodeType.TRIGGER);
    if (trigger.isEmpty()) {
      throw new RuntimeException("Tried to compare without a trigger");
    }
    Optional<DOMNode> triggerValue = trigger.get().getEdge(NodeType.VALUE);
    if (triggerValue.isEmpty()) {
      DOMNode temp = new DOMNode(NodeType.VALUE);
      temp.setVariant(new NodeVariant(""));
      try {
        trigger.get().addEdge(temp);
      } catch (DOMNodeEdgeDuplicationException | DOMNodeRuleTypeViolationException |
               DOMNodeRulePluralityViolationException | DOMNodeRuleNonExistentException e) {
        throw new RuntimeException(e);
      }
      triggerValue = Optional.of(temp);
      //throw new RuntimeException("Tried to compare to a trigger without a value to compare to");
    }
    Optional<DOMNode> value = node.getEdge(NodeType.VALUE);
    if (value.isEmpty()) {
      throw new RuntimeException("Tried to compare to a trigger without a value to compare to");
    }
    if (triggerValue.get().getVariant().isEqual(value.get().getVariant())) {
      return new TreeWalkerInstruction[] { new TreeWalkerMarkerInstruction(true) };
    }
    return new TreeWalkerInstruction[] { new TreeWalkerMarkerInstruction(false) };
  }),
  NOT_EQUALS(NONE,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.TRIGGER),
          new NodeSettings(NodeType.VALUE)
  }, (DOMNode node) -> {
    Optional<DOMNode> trigger = node.getEdge(NodeType.TRIGGER);
    if (trigger.isEmpty()) {
      throw new RuntimeException("Tried to compare without a trigger");
    }
    Optional<DOMNode> triggerValue = trigger.get().getEdge(NodeType.VALUE);
    if (triggerValue.isEmpty()) {
      throw new RuntimeException("Tried to compare to a trigger without a value to compare to");
    }
    Optional<DOMNode> value = node.getEdge(NodeType.VALUE);
    if (value.isEmpty()) {
      throw new RuntimeException("Tried to compare to a trigger without a value to compare to");
    }
    if (triggerValue.get().getVariant().notEquals(value.get().getVariant())) {
      return new TreeWalkerInstruction[] { new TreeWalkerMarkerInstruction(true) };
    }
    return new TreeWalkerInstruction[] { new TreeWalkerMarkerInstruction(false) };
  }),
  LESSER(NONE,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.TRIGGER),
          new NodeSettings(NodeType.VALUE)
  }, (DOMNode node) -> {
    Optional<DOMNode> trigger = node.getEdge(NodeType.TRIGGER);
    if (trigger.isEmpty()) {
      throw new RuntimeException("Tried to compare without a trigger");
    }
    Optional<DOMNode> triggerValue = trigger.get().getEdge(NodeType.VALUE);
    if (triggerValue.isEmpty()) {
      throw new RuntimeException("Tried to compare to a trigger without a value to compare to");
    }
    Optional<DOMNode> value = node.getEdge(NodeType.VALUE);
    if (value.isEmpty()) {
      throw new RuntimeException("Tried to compare to a trigger without a value to compare to");
    }
    if (triggerValue.get().getVariant().lesser(value.get().getVariant())) {
      return new TreeWalkerInstruction[] { new TreeWalkerMarkerInstruction(true) };
    }
    return new TreeWalkerInstruction[] { new TreeWalkerMarkerInstruction(false) };
  }),
  GREATER(NONE,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.TRIGGER),
          new NodeSettings(NodeType.VALUE)
  }, (DOMNode node) -> {
    Optional<DOMNode> trigger = node.getEdge(NodeType.TRIGGER);
    if (trigger.isEmpty()) {
      throw new RuntimeException("Tried to compare without a trigger");
    }
    Optional<DOMNode> triggerValue = trigger.get().getEdge(NodeType.VALUE);
    if (triggerValue.isEmpty()) {
      throw new RuntimeException("Tried to compare to a trigger without a value to compare to");
    }
    Optional<DOMNode> value = node.getEdge(NodeType.VALUE);
    if (value.isEmpty()) {
      throw new RuntimeException("Tried to compare to a trigger without a value to compare to");
    }
    if (triggerValue.get().getVariant().greater(value.get().getVariant())) {
      return new TreeWalkerInstruction[] { new TreeWalkerMarkerInstruction(true) };
    }
    return new TreeWalkerInstruction[] { new TreeWalkerMarkerInstruction(false) };
  }),
  VALUE(STRING,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}, (DOMNode node) -> {
    if (node.getVariant().isEqual("true")) {
      return new TreeWalkerInstruction[] { new TreeWalkerMarkerInstruction(true) };
    }
    return new TreeWalkerInstruction[] { new TreeWalkerMarkerInstruction(false) };
  }),
  QUERY(STRING,() -> new Rules[]{ }, () -> new NodeSettings[]{}, (DOMNode node) -> new TreeWalkerInstruction[]{}),
  CUSTOM(NONE,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}, (DOMNode node) -> new TreeWalkerInstruction[]{}); // has to always remain empty here, as rules for a custom node are handled later down the line

  private Supplier<NodeSettings[]> settings;
  private Supplier<Rules[]> extensions;
  private NodeVariantType nodeVariantType;
  private Function<DOMNode, TreeWalkerInstruction[]> instructions;

  Rules(NodeVariantType nodeVariantType, Supplier<Rules[]> extensions, Supplier<NodeSettings[]> settings, Function<DOMNode, TreeWalkerInstruction[]> instructions) {
    this.nodeVariantType = nodeVariantType;
    this.extensions = extensions;
    this.settings = settings;
    this.instructions = instructions;
  }

  public NodeSettings[] getSettings() {
    return this.settings.get();
  }

  public NodeVariantType getNodeVariantType() {
    return nodeVariantType;
  }

  public Supplier<Rules[]> getExtensions() {
    return extensions;
  }

  public TreeWalkerInstruction[] applyInstructions(DOMNode node) {
    return this.instructions.apply(node);
  }

  public static void throwException(TreeWalkerException exception) throws TreeWalkerException {
    throw exception;
  }
}
