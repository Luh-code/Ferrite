package dom;

public class NodeSettings {
  private NodeType nodeType;
  private boolean arrayable;

  public NodeSettings(NodeType nodeType, boolean arrayable) {
    this.nodeType = nodeType;
    this.arrayable = arrayable;
  }

  public NodeSettings(NodeType nodeType) {
    this.nodeType = nodeType;
    this.arrayable = false;
  }

  public NodeType getNodeType() {
    return nodeType;
  }

  public void setNodeType(NodeType nodeType) {
    this.nodeType = nodeType;
  }

  public boolean isArrayable() {
    return arrayable;
  }

  public void setArrayable(boolean arrayable) {
    this.arrayable = arrayable;
  }

  public NodeSettings setArrayable() {
    this.setArrayable(true);
    return this;
  }
}
