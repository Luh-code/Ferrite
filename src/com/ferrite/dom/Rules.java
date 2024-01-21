package com.ferrite.dom;

import com.ferrite.dom.exceptions.TreeWalker.TreeWalkerException;
import com.ferrite.dom.exceptions.TreeWalker.TreeWalkerNodeNotFoundException;
import com.ferrite.dom.treewalker.instructions.*;

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
          new TreeWalkerSearchInstruction("FROM state GET 'origin'=='true'", false),
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

    // go to end
    Optional<DOMNode> end = node.getEdge(NodeType.END);
    if (end.isEmpty()) {
      throw new RuntimeException("State needs to have a child of type END");
    }
    instructions.add(new TreeWalkerGetInstruction(true));
    instructions.add(new TreeWalkerMoveInstruction(end.get(), 0, true));
    // end is expected to move up itself

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
      // transition is expected to move up itself
    }

    instructions.add(new TreeWalkerLoopInstruction(transitonLoopInstructuions.toArray(TreeWalkerInstruction[]::new), node));


    return instructions.toArray(TreeWalkerInstruction[]::new);
  }),
  TRIGGER(NONE,() -> new Rules[]{ GENERAL, ALIASED }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.TYPE),
          new NodeSettings(NodeType.CUSTOM).setArrayable(),
          new NodeSettings(NodeType.ACTIVE),
          new NodeSettings(NodeType.TIME),
          new NodeSettings(NodeType.RUNNING)
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
  }, (DOMNode node) -> new TreeWalkerInstruction[]{
          new TreeWalkerDemoInstruction("Reached Begin!!!!"),
          new TreeWalkerMoveInstruction(null, 1, false)
  }),
  END(NONE, () -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{
        new NodeSettings(NodeType.TRIGGER).setArrayable(),
        new NodeSettings(NodeType.OUTPUT).setArrayable(),
  }, (DOMNode node) -> new TreeWalkerInstruction[]{}),
  TRANSITION(NONE,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.STATE),
          new NodeSettings(NodeType.IF).setArrayable()
  }, (DOMNode node) -> new TreeWalkerInstruction[]{}),
  TYPE(STRING,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}, (DOMNode node) -> new TreeWalkerInstruction[]{}),
  IF(NONE,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.EQUALS).setArrayable(),
          new NodeSettings(NodeType.LESSER).setArrayable(),
          new NodeSettings(NodeType.GREATER).setArrayable(),
          new NodeSettings(NodeType.VALUE)
  }, (DOMNode node) -> new TreeWalkerInstruction[]{}),
  EQUALS(NONE,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.TRIGGER),
          new NodeSettings(NodeType.VALUE)
  }, (DOMNode node) -> new TreeWalkerInstruction[]{}),
  LESSER(NONE,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.TRIGGER),
          new NodeSettings(NodeType.VALUE)
  }, (DOMNode node) -> new TreeWalkerInstruction[]{}),
  GREATER(NONE,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.TRIGGER),
          new NodeSettings(NodeType.VALUE)
  }, (DOMNode node) -> new TreeWalkerInstruction[]{}),
  VALUE(STRING,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}, (DOMNode node) -> new TreeWalkerInstruction[]{}),
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
