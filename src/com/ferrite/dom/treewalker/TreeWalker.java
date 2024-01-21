package com.ferrite.dom.treewalker;

import com.ferrite.dom.DOMNode;
import com.ferrite.dom.treewalker.instructions.TreeWalkerGetInstruction;
import com.ferrite.dom.treewalker.instructions.TreeWalkerInstruction;

import java.util.ArrayDeque;

public class TreeWalker implements Runnable {
  private DOMNode position;
  private ArrayDeque<TreeWalkerInstruction> instructions;

  public TreeWalker() {
    this.instructions = new ArrayDeque<>();
  }

  public void dispatch(DOMNode root) {
    this.position = root;
    addInstruction(new TreeWalkerGetInstruction());
    Thread t = new Thread(this);
    t.start();
  }

  private void loop() {
    while (!instructions.isEmpty()) {
      executeInstruction();
    }
  }

  private void executeInstruction() {
    print(String.format("TreeWalker '%s': executing %s", this, this.instructions.peek().getClass().getTypeName()));
    this.instructions.pop().act(this);
  }

  public void print(String message) {
    System.out.printf("TreeWalker '%s': %s\n", this, message);
  }

  public void addInstruction(TreeWalkerInstruction instruction) {
    this.instructions.push(instruction);
  }

  public DOMNode getPosition() {
    return position;
  }

  public void setPosition(DOMNode position) {
    this.position = position;
  }

  @Override
  public void run() {
    loop();
    print("ended execution");
  }
}
