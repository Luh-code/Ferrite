package com.ferrite.dom;

import com.ferrite.dom.exceptions.DOMNodeRulePermittedChildDuplicationException;

import java.util.ArrayList;

public class NodeRule {
  private NodeType type;
  private ArrayList<NodeSettings> permittedChildren;
  private NodeVariantType variantType;

  public NodeRule(NodeType type, NodeSettings[] permittedChildren, NodeVariantType variantType) throws DOMNodeRulePermittedChildDuplicationException {
    this.type = type;
    this.permittedChildren = new ArrayList<>();
    this.addPermittedChildren(permittedChildren);
    this.variantType = variantType;
  }

  public void addPermittedChild(NodeSettings type) throws DOMNodeRulePermittedChildDuplicationException {
    if (permittedChildren.contains(type)) {
      throw new DOMNodeRulePermittedChildDuplicationException(this.type, type.getNodeType());
    }
    this.permittedChildren.add(type);
  }

  public void addPermittedChildren(NodeSettings[] types) throws DOMNodeRulePermittedChildDuplicationException {
    for (NodeSettings type : types) {
      addPermittedChild(type);
    }
  }

  public NodeSettings[] getPermittedChildren() {
    return this.permittedChildren.toArray(new NodeSettings[0]);
  }

  public NodeVariantType getVariantType() {
    return variantType;
  }
}
