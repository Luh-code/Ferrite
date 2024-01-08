package com.ferrite.dom;

public class NodeSettings {
  private NodeType nodeType;
  private boolean arrayable;
  private boolean attributable;

  public NodeSettings(NodeType nodeType, boolean arrayable, boolean attributable) {
    this.nodeType = nodeType;
    this.arrayable = arrayable;
    this.attributable = attributable;
  }

  public NodeSettings(NodeType nodeType) {
    this.nodeType = nodeType;
    this.arrayable = false;
    this.attributable = false;
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

  public boolean isAttributable() {
    return attributable;
  }

  public void setAttributable(boolean attributable) {
    this.attributable = attributable;
  }

  public NodeSettings setAttributable() {
    this.attributable = true;
    return this;
  }
}
