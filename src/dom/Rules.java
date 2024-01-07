package dom;

import java.util.function.Supplier;

// Sub-enum for soc for setting the rule of each NodeType
// Not sanitized at compile time! Sanitization occurs at runtime whilst tree construction.
// NodeType.CUSTOM undergoes a post-sanitization
enum Rules {
  GENERAL(() -> new NodeSettings[]{
          new NodeSettings(NodeType.EXTERNAL),
          new NodeSettings(NodeType.PATH),
          new NodeSettings(NodeType.QUERY)
  }),
  SYSTEM(() -> new NodeSettings[]{
          new NodeSettings(NodeType.FERRITE_MAJOR),
          new NodeSettings(NodeType.FERRITE_MINOR)
  }),
  FERRITE_MAJOR(() -> new NodeSettings[]{}),
  FERRITE_MINOR(() -> new NodeSettings[]{}),
  MAJOR(() -> new NodeSettings[]{}),
  MINOR(() -> new NodeSettings[]{}),
  ALIAS(() -> new NodeSettings[]{}),
  MACHINE(() -> new NodeSettings[]{
          new NodeSettings(NodeType.TRIGGER).setArrayable(),
          new NodeSettings(NodeType.OUTPUT).setArrayable(),
          new NodeSettings(NodeType.STATE).setArrayable()
  }),
  EXTERNAL(() -> new NodeSettings[]{}),
  PATH(() -> new NodeSettings[]{}),
  STATE(() -> new NodeSettings[]{
          new NodeSettings(NodeType.ORIGIN),
          new NodeSettings(NodeType.ENTRY),
          new NodeSettings(NodeType.BEGIN),
          new NodeSettings(NodeType.TRANSITION).setArrayable()
  }),
  TRIGGER(() -> new NodeSettings[]{
          new NodeSettings(NodeType.TYPE),
          new NodeSettings(NodeType.CUSTOM).setArrayable()
  }),
  OUTPUT(() -> new NodeSettings[]{
          new NodeSettings(NodeType.TYPE),
          new NodeSettings(NodeType.CUSTOM).setArrayable()
  }),
  ORIGIN(() -> new NodeSettings[]{}),
  ENTRY(() -> new NodeSettings[]{}),
  BEGIN(() -> new NodeSettings[]{}),
  TRANSITION(() -> new NodeSettings[]{
          new NodeSettings(NodeType.STATE),
          new NodeSettings(NodeType.IF).setArrayable()
  }),
  TYPE(() -> new NodeSettings[]{}),
  IF(() -> new NodeSettings[]{
          new NodeSettings(NodeType.EQUALS).setArrayable(),
          new NodeSettings(NodeType.LESSER).setArrayable(),
          new NodeSettings(NodeType.GREATER).setArrayable()
  }),
  EQUALS(() -> new NodeSettings[]{
          new NodeSettings(NodeType.TRIGGER),
          new NodeSettings(NodeType.VALUE)
  }),
  LESSER(() -> new NodeSettings[]{
          new NodeSettings(NodeType.TRIGGER),
          new NodeSettings(NodeType.VALUE)
  }),
  GREATER(() -> new NodeSettings[]{
          new NodeSettings(NodeType.TRIGGER),
          new NodeSettings(NodeType.VALUE)
  }),
  VALUE(() -> new NodeSettings[]{}),
  QUERY(() -> new NodeSettings[]{}),
  CUSTOM(() -> new NodeSettings[]{}); // has to always remain empty here, as rules for a custom node are handled later down the line

  private Supplier<NodeSettings[]> settings;

  Rules(Supplier<NodeSettings[]> settings) {
    this.settings = settings;
    //this.rules = Arrays.stream(this.rules).toList().addAll(Rules.GENERAL.getRules());
  }

  public NodeSettings[] getSettings() {
    return this.settings.get();
  }
}
