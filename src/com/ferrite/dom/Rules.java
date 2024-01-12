package com.ferrite.dom;

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
  }),
  VERSIONED(NONE, () -> new Rules[]{}, () -> new NodeSettings[]{
          new NodeSettings(NodeType.FERRITE_MAJOR),
          new NodeSettings(NodeType.FERRITE_MINOR),
          new NodeSettings(NodeType.MAJOR),
          new NodeSettings(NodeType.MINOR),
  }),
  ALIASED(NONE, () -> new Rules[]{}, () -> new NodeSettings[]{
          new NodeSettings(NodeType.ALIAS)
  }),
  SYSTEM(NONE,() -> new Rules[]{ GENERAL, VERSIONED, ALIASED }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.MACHINE).setArrayable(),
          new NodeSettings(NodeType.STATE).setArrayable()
  }),
  FERRITE_MAJOR(INTEGER,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}),
  FERRITE_MINOR(INTEGER,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}),
  MAJOR(INTEGER,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}),
  MINOR(INTEGER,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}),
  ALIAS(STRING,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}),
  MACHINE(NONE,() -> new Rules[]{ GENERAL, VERSIONED, ALIASED }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.TRIGGER).setArrayable(),
          new NodeSettings(NodeType.OUTPUT).setArrayable(),
          new NodeSettings(NodeType.STATE).setArrayable()
  }),
  EXTERNAL(BOOLEAN,() -> new Rules[]{ }, () -> new NodeSettings[]{}),
  PATH(STRING,() -> new Rules[]{ }, () -> new NodeSettings[]{}),
  STATE(NONE,() -> new Rules[]{ GENERAL, ALIASED }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.ORIGIN),
          new NodeSettings(NodeType.ENTRY),
          new NodeSettings(NodeType.BEGIN),
          new NodeSettings(NodeType.END),
          new NodeSettings(NodeType.TRANSITION).setArrayable(),
          new NodeSettings(NodeType.STATE)
  }),
  TRIGGER(NONE,() -> new Rules[]{ GENERAL, ALIASED }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.TYPE),
          new NodeSettings(NodeType.CUSTOM).setArrayable(),
          new NodeSettings(NodeType.ACTIVE),
          new NodeSettings(NodeType.TIME),
          new NodeSettings(NodeType.RUNNING)
  }),
  OUTPUT(NONE,() -> new Rules[]{ GENERAL, ALIASED }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.TYPE),
          new NodeSettings(NodeType.CUSTOM).setArrayable(),
          new NodeSettings(NodeType.ACTIVE),
          new NodeSettings(NodeType.TIME),
          new NodeSettings(NodeType.RUNNING),
          new NodeSettings(NodeType.VALUE)
  }),
  ACTIVE(STRING, () -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}),
  TIME(FLOAT,  () -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}),
  RUNNING(BOOLEAN, () -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}),
  ORIGIN(BOOLEAN,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}),
  ENTRY(BOOLEAN,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}),
  BEGIN(NONE,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{
        new NodeSettings(NodeType.TRIGGER).setArrayable(),
        new NodeSettings(NodeType.OUTPUT).setArrayable(),
  }),
  END(NONE, () -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{
        new NodeSettings(NodeType.TRIGGER).setArrayable(),
        new NodeSettings(NodeType.OUTPUT).setArrayable(),
  }),
  TRANSITION(NONE,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.STATE),
          new NodeSettings(NodeType.IF).setArrayable()
  }),
  TYPE(STRING,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}),
  IF(NONE,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.EQUALS).setArrayable(),
          new NodeSettings(NodeType.LESSER).setArrayable(),
          new NodeSettings(NodeType.GREATER).setArrayable()
  }),
  EQUALS(NONE,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.TRIGGER),
          new NodeSettings(NodeType.VALUE)
  }),
  LESSER(NONE,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.TRIGGER),
          new NodeSettings(NodeType.VALUE)
  }),
  GREATER(NONE,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{
          new NodeSettings(NodeType.TRIGGER),
          new NodeSettings(NodeType.VALUE)
  }),
  VALUE(STRING,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}),
  QUERY(STRING,() -> new Rules[]{ }, () -> new NodeSettings[]{}),
  CUSTOM(NONE,() -> new Rules[]{ GENERAL }, () -> new NodeSettings[]{}); // has to always remain empty here, as rules for a custom node are handled later down the line

  private Supplier<NodeSettings[]> settings;
  private Supplier<Rules[]> extensions;
  private NodeVariantType nodeVariantType;

  Rules(NodeVariantType nodeVariantType, Supplier<Rules[]> extensions, Supplier<NodeSettings[]> settings) {
    this.nodeVariantType = nodeVariantType;
    this.extensions = extensions;
    this.settings = settings;
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
}
