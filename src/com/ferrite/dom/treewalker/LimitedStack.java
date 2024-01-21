package com.ferrite.dom.treewalker;

import java.util.ArrayDeque;
import java.util.Deque;

public class LimitedStack<T> {

  private final int maxSize;
  private final Deque<T> stack;

  public LimitedStack(int maxSize) {
    if (maxSize <= 0) {
      throw new IllegalArgumentException("Max size must be greater than zero.");
    }
    this.maxSize = maxSize;
    this.stack = new ArrayDeque<>(maxSize);
  }

  public void push(T item) {
    if (stack.size() == maxSize) {
      stack.removeFirst(); // Remove the oldest element
    }
    stack.addLast(item);
  }

  public T pop() {
    if (isEmpty()) {
      throw new IllegalStateException("Stack is empty");
    }
    return stack.removeLast();
  }

  public T peek() {
    if (isEmpty()) {
      throw new IllegalStateException("Stack is empty");
    }
    return stack.peekLast();
  }

  public boolean isEmpty() {
    return stack.isEmpty();
  }

  public int size() {
    return stack.size();
  }
}

