package com.ferrite.dom;

import com.ferrite.dom.exceptions.DOMNodeVariantIllegalComparisonException;
import com.ferrite.dom.exceptions.DOMNodeVariantIllegalVariantTypeException;
import com.ferrite.dom.exceptions.DOMNodeVariantTypeMismatchException;

import java.util.function.Function;

// please ignore the outrageous DRY violations, I can't arsed fixing that right now

public class NodeVariant {
  private Object variant;
  private final NodeVariantType type;
  private final Function<String, Boolean> compGreater;
  private final Function<String, Boolean> compLesser;
  private final Function<String, Boolean> compEquals;
  private final Function<String, Boolean> compNotEquals;

  public NodeVariant(int value) {
    this.variant = value;
    try {
      this.type = computeType();
    } catch (DOMNodeVariantIllegalVariantTypeException e) {
      throw new RuntimeException(e);
    }
    this.compGreater = s -> (Integer)variant > Integer.parseInt(s);
    this.compLesser = s -> (Integer)variant < Integer.parseInt(s);
    this.compEquals = s -> (Integer)variant == Integer.parseInt(s);
    this.compNotEquals = s -> (Integer)variant != Integer.parseInt(s);
  }
  public NodeVariant(float value) {
    this.variant = value;
    try {
      this.type = computeType();
    } catch (DOMNodeVariantIllegalVariantTypeException e) {
      throw new RuntimeException(e);
    }
    this.compGreater = s -> (Float)variant > Float.parseFloat(s);
    this.compLesser = s -> (Float)variant < Float.parseFloat(s);
    this.compEquals = s -> (Float)variant == Float.parseFloat(s);
    this.compNotEquals = s -> (Float)variant != Float.parseFloat(s);
  }
  public NodeVariant(String value) {
    this.variant = value;
    try {
      this.type = computeType();
    } catch (DOMNodeVariantIllegalVariantTypeException e) {
      throw new RuntimeException(e);
    }
    this.compGreater = s -> {
      try {
        throw new DOMNodeVariantIllegalComparisonException(type.name(), ">");
      } catch (DOMNodeVariantIllegalComparisonException e) {
        throw new RuntimeException(e);
      }
    };
    this.compLesser = s -> {
      try {
        throw new DOMNodeVariantIllegalComparisonException(type.name(), "<");
      } catch (DOMNodeVariantIllegalComparisonException e) {
        throw new RuntimeException(e);
      }
    };
    this.compEquals = s -> variant.equals(s);
    this.compNotEquals = s -> !compEquals.apply(s);
  }
  public NodeVariant(boolean value) {
    this.variant = value;
    try {
      this.type = computeType();
    } catch (DOMNodeVariantIllegalVariantTypeException e) {
      throw new RuntimeException(e);
    }
    this.compGreater = s -> {
      try {
        throw new DOMNodeVariantIllegalComparisonException(type.name(), ">");
      } catch (DOMNodeVariantIllegalComparisonException e) {
        throw new RuntimeException(e);
      }
    };
    this.compLesser = s -> {
      try {
        throw new DOMNodeVariantIllegalComparisonException(type.name(), "<");
      } catch (DOMNodeVariantIllegalComparisonException e) {
        throw new RuntimeException(e);
      }
    };
    this.compEquals = s -> (Boolean)variant == Boolean.parseBoolean(s);
    this.compNotEquals = s -> !compEquals.apply(s);
  }

  private NodeVariantType computeType() throws DOMNodeVariantIllegalVariantTypeException {
    switch (variant) {
      case null -> {
        return NodeVariantType.NONE;
      }
      case Integer ignored -> {
        return NodeVariantType.INTEGER;
      }
      case Float ignored -> {
        return NodeVariantType.FLOAT;
      }
      case Boolean ignored -> {
        return NodeVariantType.BOOLEAN;
      }
      case String ignored -> {
        return NodeVariantType.STRING;
      }
      default -> throw new DOMNodeVariantIllegalVariantTypeException(variant.getClass());
    }
  }

  public NodeVariantType getType() {
    return this.type;
  }

  @SuppressWarnings("unused")
  public int getInt() throws DOMNodeVariantTypeMismatchException {
    if (getType() != NodeVariantType.INTEGER) {
      throw new DOMNodeVariantTypeMismatchException(Integer.class, variant.getClass());
    }
    return (int) variant;
  }
  @SuppressWarnings("unused")
  public void setInt(int value) throws DOMNodeVariantTypeMismatchException {
    if (getType() != NodeVariantType.INTEGER) {
      throw new DOMNodeVariantTypeMismatchException(Integer.class, variant.getClass());
    }
    this.variant = value;
  }
  @SuppressWarnings("unused")
  public float getFloat() throws DOMNodeVariantTypeMismatchException {
    if (getType() != NodeVariantType.FLOAT) {
      throw new DOMNodeVariantTypeMismatchException(Float.class, variant.getClass());
    }
    return (float) variant;
  }
  @SuppressWarnings("unused")
  public void setFloat(float value) throws DOMNodeVariantTypeMismatchException {
    if (getType() != NodeVariantType.FLOAT) {
      throw new DOMNodeVariantTypeMismatchException(Float.class, variant.getClass());
    }
    this.variant = value;
  }
  @SuppressWarnings("unused")
  public boolean getBoolean() throws DOMNodeVariantTypeMismatchException {
    if (getType() != NodeVariantType.BOOLEAN) {
      throw new DOMNodeVariantTypeMismatchException(Boolean.class, variant.getClass());
    }
    return (boolean) variant;
  }
  @SuppressWarnings("unused")
  public void setBoolean(boolean value) throws DOMNodeVariantTypeMismatchException {
    if (getType() != NodeVariantType.BOOLEAN) {
      throw new DOMNodeVariantTypeMismatchException(Boolean.class, variant.getClass());
    }
    this.variant = value;
  }
  @SuppressWarnings("unused")
  public String getString() throws DOMNodeVariantTypeMismatchException {
    if (getType() != NodeVariantType.STRING) {
      throw new DOMNodeVariantTypeMismatchException(String.class, variant.getClass());
    }
    return (String) variant;
  }
  @SuppressWarnings("unused")
  public void setString(String value) throws DOMNodeVariantTypeMismatchException {
    if (getType() != NodeVariantType.STRING) {
      throw new DOMNodeVariantTypeMismatchException(String.class, variant.getClass());
    }
    this.variant = value;
  }

  public String getAsString() {
    return String.valueOf(this.variant);
  }

  public void setVariant(NodeVariant value) throws DOMNodeVariantTypeMismatchException, DOMNodeVariantIllegalVariantTypeException {
    if (getType() != value.getType()) {
      throw new DOMNodeVariantTypeMismatchException(String.class, variant.getClass());
    }
    this.variant = value.variant;
  }

  @SuppressWarnings("unused")
  public boolean isEqual(String value) {
    return this.compEquals.apply(value);
  }
  public boolean isEqual(NodeVariant value) {
    return this.compEquals.apply(value.getAsString());
  }
  @SuppressWarnings("unused")
  public boolean notEquals(String value) {
    return this.compNotEquals.apply(value);
  }
  public boolean notEquals(NodeVariant value) {
    return this.compNotEquals.apply(value.getAsString());
  }
  @SuppressWarnings("unused")
  public boolean greater(String value) {
    return this.compGreater.apply(value);
  }
  public boolean greater(NodeVariant value) {
    return this.compGreater.apply(value.getAsString());
  }
  @SuppressWarnings("unused")
  public boolean lesser(String value) {
    return this.compLesser.apply(value);
  }
  public boolean lesser(NodeVariant value) {
    return this.compLesser.apply(value.getAsString());
  }
}
