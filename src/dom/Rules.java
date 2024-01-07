package dom;

// Sub-enum for soc for setting the rule of each NodeType
// Not sanitized at compile time! Sanitization occurs at runtime whilst tree construction.
// NodeType.CUSTOM undergoes a post-sanitization
enum Rules {
  GENERAL( // TODO: fix this
          new NodeSettings(NodeType.EXTERNAL),
          new NodeSettings(NodeType.PATH),
          new NodeSettings(NodeType.QUERY)
  ),
  SYSTEM(
          new NodeSettings(NodeType.FERRITE_MAJOR),
          new NodeSettings(NodeType.FERRITE_MINOR)
  ),
  FERRITE_MAJOR,
  FERRITE_MINOR,
  MAJOR,
  MINOR,
  ALIAS,
  MACHINE(
          new NodeSettings(NodeType.TRIGGER).setArrayable(),
          new NodeSettings(NodeType.OUTPUT).setArrayable(),
          new NodeSettings(NodeType.STATE).setArrayable()
  ),
  EXTERNAL,
  PATH,
  STATE(
          new NodeSettings(NodeType.ORIGIN),
          new NodeSettings(NodeType.ENTRY),
          new NodeSettings(NodeType.BEGIN),
          new NodeSettings(NodeType.TRANSITION).setArrayable()
  ),
  TRIGGER(
          new NodeSettings(NodeType.TYPE),
          new NodeSettings(NodeType.CUSTOM).setArrayable()
  ),
  OUTPUT(
          new NodeSettings(NodeType.TYPE),
          new NodeSettings(NodeType.CUSTOM).setArrayable()
  ),
  ORIGIN,
  ENTRY,
  BEGIN,
  TRANSITION(
          new NodeSettings(NodeType.STATE),
          new NodeSettings(NodeType.IF).setArrayable()
  ),
  TYPE,
  IF(
          new NodeSettings(NodeType.EQUALS).setArrayable(),
          new NodeSettings(NodeType.LESSER).setArrayable(),
          new NodeSettings(NodeType.GREATER).setArrayable()
  ),
  EQUALS(
          new NodeSettings(NodeType.TRIGGER),
          new NodeSettings(NodeType.VALUE)
  ),
  LESSER(
          new NodeSettings(NodeType.TRIGGER),
          new NodeSettings(NodeType.VALUE)
  ),
  GREATER(
          new NodeSettings(NodeType.TRIGGER),
          new NodeSettings(NodeType.VALUE)
  ),
  VALUE,
  QUERY,
  CUSTOM; // has to always remain empty here, as rules for a custom node are handled later down the line

  private NodeSettings[] settings;

  Rules(NodeSettings... settings) {
    this.settings = settings;
    //this.rules = Arrays.stream(this.rules).toList().addAll(Rules.GENERAL.getRules());
  }

  public NodeSettings[] getSettings() {
    return this.settings;
  }
}
