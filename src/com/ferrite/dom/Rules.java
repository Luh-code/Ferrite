package com.ferrite.dom;

import com.ferrite.dom.treewalker.instructions.TreeWalkerDemoInstruction;
import com.ferrite.dom.treewalker.instructions.TreeWalkerGetInstruction;
import com.ferrite.dom.treewalker.instructions.TreeWalkerInstruction;
import com.ferrite.dom.treewalker.instructions.TreeWalkerSearchInstruction;
import org.w3c.dom.Node;

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
  }, new TreeWalkerInstruction[]{}),
  VERSIONED(NONE, () -> new Rules[]{}, () -> new NodeSettings[]{
          new NodeSettings(NodeType.FERRITE_MAJOR),
          new NodeSettings(NodeType.FERRITE_MINOR),
          new NodeSettings(NodeType.MAJOR),
          new NodeSettings(NodeType.MINOR),
  }, new TreeWalkerInstruction[]{}),
  ALIASED(NONE, () -> new Rules[]{}, () -> new NodeSettings[]{
          new NodeSettings(NodeType.ALIAS)
  }, new TreeWalkerInstruction[]{}),
  SYSTEM(NONE,() -> new Rules[]{ GENERAL, VERSIONED, ALIASED }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.MACHINE).setArrayable(),
          new NodeSettings(NodeType.STATE).setArrayable()
  }, new TreeWalkerInstruction[]{
          new TreeWalkerSearchInstruction("FROM state GET 'origin'=='true'"),
          new TreeWalkerGetInstruction()
  }),
  FERRITE_MAJOR(INTEGER,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}, new TreeWalkerInstruction[]{}),
  FERRITE_MINOR(INTEGER,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}, new TreeWalkerInstruction[]{}),
  MAJOR(INTEGER,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}, new TreeWalkerInstruction[]{}),
  MINOR(INTEGER,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}, new TreeWalkerInstruction[]{}),
  ALIAS(STRING,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}, new TreeWalkerInstruction[]{}),
  MACHINE(NONE,() -> new Rules[]{ GENERAL, VERSIONED, ALIASED }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.TRIGGER).setArrayable(),
          new NodeSettings(NodeType.OUTPUT).setArrayable(),
          new NodeSettings(NodeType.STATE).setArrayable()
  }, new TreeWalkerInstruction[]{}),
  EXTERNAL(BOOLEAN,() -> new Rules[]{ }, () -> new NodeSettings[]{}, new TreeWalkerInstruction[]{}),
  PATH(STRING,() -> new Rules[]{ }, () -> new NodeSettings[]{}, new TreeWalkerInstruction[]{}),
  STATE(NONE,() -> new Rules[]{ GENERAL, ALIASED }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.ORIGIN),
          new NodeSettings(NodeType.ENTRY),
          new NodeSettings(NodeType.BEGIN),
          new NodeSettings(NodeType.END),
          new NodeSettings(NodeType.TRANSITION).setArrayable(),
          new NodeSettings(NodeType.PROXY),
          new NodeSettings(NodeType.STATE)
  }, new TreeWalkerInstruction[]{
          new TreeWalkerDemoInstruction("Found origin!!!")
  }),
  PROXY(BOOLEAN, () -> new Rules[] { GENERAL }, () -> new NodeSettings[] {}, new TreeWalkerInstruction[]{}),
  TRIGGER(NONE,() -> new Rules[]{ GENERAL, ALIASED }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.TYPE),
          new NodeSettings(NodeType.CUSTOM).setArrayable(),
          new NodeSettings(NodeType.ACTIVE),
          new NodeSettings(NodeType.TIME),
          new NodeSettings(NodeType.RUNNING)
  }, new TreeWalkerInstruction[]{}),
  OUTPUT(NONE,() -> new Rules[]{ GENERAL, ALIASED }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.TYPE),
          new NodeSettings(NodeType.CUSTOM).setArrayable(),
          new NodeSettings(NodeType.ACTIVE),
          new NodeSettings(NodeType.TIME),
          new NodeSettings(NodeType.RUNNING),
          new NodeSettings(NodeType.VALUE)
  }, new TreeWalkerInstruction[]{}),
  ACTIVE(STRING, () -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}, new TreeWalkerInstruction[]{}),
  TIME(FLOAT,  () -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}, new TreeWalkerInstruction[]{}),
  RUNNING(BOOLEAN, () -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}, new TreeWalkerInstruction[]{}),
  ORIGIN(BOOLEAN,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}, new TreeWalkerInstruction[]{}),
  ENTRY(BOOLEAN,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}, new TreeWalkerInstruction[]{}),
  BEGIN(NONE,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{
        new NodeSettings(NodeType.TRIGGER).setArrayable(),
        new NodeSettings(NodeType.OUTPUT).setArrayable(),
  }, new TreeWalkerInstruction[]{}),
  END(NONE, () -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{
        new NodeSettings(NodeType.TRIGGER).setArrayable(),
        new NodeSettings(NodeType.OUTPUT).setArrayable(),
  }, new TreeWalkerInstruction[]{}),
  TRANSITION(NONE,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.STATE),
          new NodeSettings(NodeType.IF).setArrayable()
  }, new TreeWalkerInstruction[]{}),
  TYPE(STRING,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}, new TreeWalkerInstruction[]{}),
  IF(NONE,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.EQUALS).setArrayable(),
          new NodeSettings(NodeType.LESSER).setArrayable(),
          new NodeSettings(NodeType.GREATER).setArrayable()
  }, new TreeWalkerInstruction[]{}),
  EQUALS(NONE,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.TRIGGER),
          new NodeSettings(NodeType.VALUE)
  }, new TreeWalkerInstruction[]{}),
  LESSER(NONE,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.TRIGGER),
          new NodeSettings(NodeType.VALUE)
  }, new TreeWalkerInstruction[]{}),
  GREATER(NONE,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.TRIGGER),
          new NodeSettings(NodeType.VALUE)
  }, new TreeWalkerInstruction[]{}),
  VALUE(STRING,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}, new TreeWalkerInstruction[]{}),
  QUERY(STRING,() -> new Rules[]{ }, () -> new NodeSettings[]{}, new TreeWalkerInstruction[]{}),
  CUSTOM(NONE,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}, new TreeWalkerInstruction[]{}); // has to always remain empty here, as rules for a custom node are handled later down the line

  private Supplier<NodeSettings[]> settings;
  private Supplier<Rules[]> extensions;
  private NodeVariantType nodeVariantType;
  private TreeWalkerInstruction[] instructions;

  Rules(NodeVariantType nodeVariantType, Supplier<Rules[]> extensions, Supplier<NodeSettings[]> settings, TreeWalkerInstruction[] instructions) {
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

  public TreeWalkerInstruction[] getInstructions() {
    return this.instructions;
  }
}
