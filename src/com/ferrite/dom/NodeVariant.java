package com.ferrite.dom;

import com.ferrite.dom.exceptions.DOMNodeVariantIllegalVariantTypeException;
import com.ferrite.dom.exceptions.DOMNodeVariantTypeMismatchException;

public class NodeVariant {
  private Object variant;

  public NodeVariant(int value) {
    this.variant = value;
  }
  public NodeVariant(float value) {
    this.variant = value;
  }
  public NodeVariant(String value) {
    this.variant = value;
  }
  public NodeVariant(boolean value) {
    this.variant = value;
  }
  public NodeVariant() {
    this.variant = null;
  }

  public NodeVariantType getType() throws DOMNodeVariantIllegalVariantTypeException {
    switch (variant) {
      case null -> {
        return NodeVariantType.NONE;
      }
      case Integer i -> {
        return NodeVariantType.INTEGER;
      }
      case Float f -> {
        return NodeVariantType.FLOAT;
      }
      case Boolean b -> {
        return NodeVariantType.BOOLEAN;
      }
      case String s -> {
        return NodeVariantType.STRING;
      }
      default -> {
        throw new DOMNodeVariantIllegalVariantTypeException(variant.getClass());
      }
    }
  }

  public int getInt() throws DOMNodeVariantIllegalVariantTypeException, DOMNodeVariantTypeMismatchException {
    if (getType() != NodeVariantType.INTEGER) {
      throw new DOMNodeVariantTypeMismatchException(Integer.class, variant.getClass());
    }
    return (int) variant;
  }
  public float getFloat() throws DOMNodeVariantIllegalVariantTypeException, DOMNodeVariantTypeMismatchException {
    if (getType() != NodeVariantType.FLOAT) {
      throw new DOMNodeVariantTypeMismatchException(Float.class, variant.getClass());
    }
    return (float) variant;
  }
  public boolean getBoolean() throws DOMNodeVariantIllegalVariantTypeException, DOMNodeVariantTypeMismatchException {
    if (getType() != NodeVariantType.BOOLEAN) {
      throw new DOMNodeVariantTypeMismatchException(Boolean.class, variant.getClass());
    }
    return (boolean) variant;
  }
  public String getString() throws DOMNodeVariantIllegalVariantTypeException, DOMNodeVariantTypeMismatchException {
    if (getType() != NodeVariantType.STRING) {
      throw new DOMNodeVariantTypeMismatchException(String.class, variant.getClass());
    }
    return (String) variant;
  }
}
