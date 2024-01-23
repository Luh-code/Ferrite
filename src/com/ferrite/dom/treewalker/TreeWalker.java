package com.ferrite.dom.treewalker;

import com.ferrite.controller.Controller;
import com.ferrite.dom.DOMNode;
import com.ferrite.dom.treewalker.instructions.TreeWalkerGetInstruction;
import com.ferrite.dom.treewalker.instructions.TreeWalkerInstruction;

import java.util.ArrayDeque;
import java.util.Stack;

public class TreeWalker implements Runnable {
  private DOMNode position;
  private ArrayDeque<TreeWalkerInstruction> instructions;
  private ArrayDeque<TreeWalkerInstruction> lateInstructions;
  private LimitedStack<DOMNode> past;

  private Controller controller;

  public TreeWalker(Controller controller) {
    this.instructions = new ArrayDeque<>();
    this.lateInstructions = new ArrayDeque<>();
    this.past = new LimitedStack<>(15);
    this.controller = controller;
  }

  public void dispatch(DOMNode root) {
    this.position = root;
    addInstruction(new TreeWalkerGetInstruction(false));
    Thread t = new Thread(this);
    //t.start();
    run();
  }

  private void loop() {
    while (!instructions.isEmpty() || !lateInstructions.isEmpty()) {
      if (!instructions.isEmpty()) {
        executeInstruction();
      } else {
        executeLateInstruction();
      }
    }
  }

  private void executeInstruction() {
    print(String.format("executing %s", this.instructions.peek().getClass().getTypeName()));
    this.instructions.pop().act(this);
  }
  private void executeLateInstruction() {
    print(String.format("executing %s (late)", this.lateInstructions.peek().getClass().getTypeName()));
    this.lateInstructions.pop().act(this);
  }

  public void print(String message) {
    System.out.printf("TreeWalker '%s': %s\n", this, message);
  }

  public void addInstruction(TreeWalkerInstruction instruction) {
    this.instructions.push(instruction);
  }

  public void addLateInstruction(TreeWalkerInstruction instruction) {
    this.lateInstructions.push(instruction);
  }
  public void addLateEndInstruction(TreeWalkerInstruction instruction) {
    this.lateInstructions.addLast(instruction);
  }

  public void sortedAddInstruction(TreeWalkerInstruction instruction) {
    if (instruction.getLate()) {
      addLateInstruction(instruction);
      return;
    }
    addInstruction(instruction);
  }

  public TreeWalkerInstruction popInstruction() {
    return this.instructions.pop();
  }

  public TreeWalkerInstruction popLateInstruction() {
    return this.lateInstructions.pop();
  }

  public int instructionCount() {
    return this.instructions.size();
  }

  public int lateInstructionCount() {
    return this.lateInstructions.size();
  }

  public DOMNode getPosition() {
    return position;
  }

  public void setPosition(DOMNode position) {
    this.past.push(this.position);
    this.position = position;
  }

  @Override
  public void run() {
    loop();
    print("ended execution");
  }
  public DOMNode peekPast() {
    return past.peek();
  }
  public DOMNode popPast() {
    return past.pop();
  }
  public int pastSize() {
    return past.size();
  }

  public Controller getController() {
    return controller;
  }
}
