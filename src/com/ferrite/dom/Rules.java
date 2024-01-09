package com.ferrite.dom;

import java.util.function.Supplier;

import static com.ferrite.dom.NodeVariantType.*;

// Sub-enum for soc for setting the rule of each NodeType
// Not sanitized at compile time! Sanitization occurs at runtime whilst tree construction.
// NodeType.CUSTOM undergoes a post-sanitization
enum Rules {
  GENERAL(NONE,() -> new NodeSettings[]{
          new NodeSettings(NodeType.EXTERNAL),
          new NodeSettings(NodeType.PATH),
          new NodeSettings(NodeType.QUERY)
  }),
  SYSTEM(NONE, () -> new NodeSettings[]{
          new NodeSettings(NodeType.FERRITE_MAJOR),
          new NodeSettings(NodeType.FERRITE_MINOR)
  }),
  FERRITE_MAJOR(INTEGER, () -> new NodeSettings[]{}),
  FERRITE_MINOR(INTEGER, () -> new NodeSettings[]{}),
  MAJOR(INTEGER, () -> new NodeSettings[]{}),
  MINOR(INTEGER, () -> new NodeSettings[]{}),
  ALIAS(STRING, () -> new NodeSettings[]{}),
  MACHINE(NONE, () -> new NodeSettings[]{
          new NodeSettings(NodeType.TRIGGER).setArrayable(),
          new NodeSettings(NodeType.OUTPUT).setArrayable(),
          new NodeSettings(NodeType.STATE).setArrayable()
  }),
  EXTERNAL(NONE, () -> new NodeSettings[]{}),
  PATH(STRING, () -> new NodeSettings[]{}),
  STATE(NONE, () -> new NodeSettings[]{
          new NodeSettings(NodeType.ORIGIN),
          new NodeSettings(NodeType.ENTRY),
          new NodeSettings(NodeType.BEGIN),
          new NodeSettings(NodeType.TRANSITION).setArrayable()
  }),
  TRIGGER(NONE, () -> new NodeSettings[]{
          new NodeSettings(NodeType.TYPE),
          new NodeSettings(NodeType.CUSTOM).setArrayable()
  }),
  OUTPUT(NONE, () -> new NodeSettings[]{
          new NodeSettings(NodeType.TYPE),
          new NodeSettings(NodeType.CUSTOM).setArrayable()
  }),
  ORIGIN(BOOLEAN, () -> new NodeSettings[]{}),
  ENTRY(BOOLEAN, () -> new NodeSettings[]{}),
  BEGIN(BOOLEAN, () -> new NodeSettings[]{}),
  TRANSITION(NONE, () -> new NodeSettings[]{
          new NodeSettings(NodeType.STATE),
          new NodeSettings(NodeType.IF).setArrayable()
  }),
  TYPE(STRING, () -> new NodeSettings[]{}),
  IF(NONE, () -> new NodeSettings[]{
          new NodeSettings(NodeType.EQUALS).setArrayable(),
          new NodeSettings(NodeType.LESSER).setArrayable(),
          new NodeSettings(NodeType.GREATER).setArrayable()
  }),
  EQUALS(NONE, () -> new NodeSettings[]{
          new NodeSettings(NodeType.TRIGGER),
          new NodeSettings(NodeType.VALUE)
  }),
  LESSER(NONE, () -> new NodeSettings[]{
          new NodeSettings(NodeType.TRIGGER),
          new NodeSettings(NodeType.VALUE)
  }),
  GREATER(NONE, () -> new NodeSettings[]{
          new NodeSettings(NodeType.TRIGGER),
          new NodeSettings(NodeType.VALUE)
  }),
  VALUE(STRING, () -> new NodeSettings[]{}),
  QUERY(STRING, () -> new NodeSettings[]{}),
  CUSTOM(NONE, () -> new NodeSettings[]{}); // has to always remain empty here, as rules for a custom node are handled later down the line

  private Supplier<NodeSettings[]> settings;
  private NodeVariantType nodeVariantType;

  Rules(NodeVariantType nodeVariantType, Supplier<NodeSettings[]> settings) {
    this.nodeVariantType = nodeVariantType;
    this.settings = settings;
  }

  public NodeSettings[] getSettings() {
    return this.settings.get();
  }

  public NodeVariantType getNodeVariantType() {
    return nodeVariantType;
  }
}
