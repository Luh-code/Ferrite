package com.ferrite.dom.treewalker.instructions;

import com.ferrite.dom.DOMNode;
import com.ferrite.dom.exceptions.query.QueryEmptyResultException;
import com.ferrite.dom.exceptions.query.QueryInvalidSyntaxException;
import com.ferrite.dom.query.QueryEngine;
import com.ferrite.dom.treewalker.TreeWalker;

public class TreeWalkerSearchInstruction implements TreeWalkerInstruction {
  private String query;
  private boolean late;

  public TreeWalkerSearchInstruction(String query, boolean late) {
    this.query = query;
    this.late = late;
  }

  @Override
  public void act(TreeWalker walker) {
    QueryEngine qe = new QueryEngine();
    try {
      qe.query(walker.getPosition(), this.query);
    } catch (QueryInvalidSyntaxException | QueryEmptyResultException e) {
      throw new RuntimeException(e);
    }
    DOMNode result = qe.getResult();
    walker.setPosition(result);
  }

  @Override
  public boolean getLate() {
    return this.late;
  }
}
