package com.ferrite.dom;

import com.ferrite.dom.exceptions.DOMNodeVariantIllegalComparisonException;
import com.ferrite.dom.exceptions.DOMNodeVariantIllegalVariantTypeException;
import com.ferrite.dom.exceptions.DOMNodeVariantTypeMismatchException;

import java.util.function.Function;

public class NodeVariant {
  private Object variant;
  private NodeVariantType type;
  private Function<String, Boolean> compGreater;
  private Function<String, Boolean> compLesser;
  private Function<String, Boolean> compEquals;
  private Function<String, Boolean> compNotEquals;

  public NodeVariant(int value) {
    this.variant = value;
    try {
      this.type = getType();
    } catch (DOMNodeVariantIllegalVariantTypeException e) {
      throw new RuntimeException(e);
    }
    this.compGreater = new Function<String, Boolean>() {
      @Override
      public Boolean apply(String s) {
        return (Integer)variant > Integer.parseInt(s);
      }
    };
    this.compLesser = new Function<String, Boolean>() {
      @Override
      public Boolean apply(String s) {
        return (Integer)variant < Integer.parseInt(s);
      }
    };
    this.compEquals = new Function<String, Boolean>() {
      @Override
      public Boolean apply(String s) {
        return (Integer)variant == Integer.parseInt(s);
      }
    };
    this.compNotEquals = new Function<String, Boolean>() {
      @Override
      public Boolean apply(String s) {
        return (Integer)variant != Integer.parseInt(s);
      }
    };
  }
  public NodeVariant(float value) {
    this.variant = value;
    try {
      this.type = getType();
    } catch (DOMNodeVariantIllegalVariantTypeException e) {
      throw new RuntimeException(e);
    }
    this.compGreater = new Function<String, Boolean>() {
      @Override
      public Boolean apply(String s) {
        return (Float)variant > Float.parseFloat(s);
      }
    };
    this.compLesser = new Function<String, Boolean>() {
      @Override
      public Boolean apply(String s) {
        return (Float)variant < Float.parseFloat(s);
      }
    };
    this.compEquals = new Function<String, Boolean>() {
      @Override
      public Boolean apply(String s) {
        return (Float)variant == Float.parseFloat(s);
      }
    };
    this.compNotEquals = new Function<String, Boolean>() {
      @Override
      public Boolean apply(String s) {
        return (Float)variant != Float.parseFloat(s);
      }
    };
  }
  public NodeVariant(String value) {
    this.variant = value;
    try {
      this.type = getType();
    } catch (DOMNodeVariantIllegalVariantTypeException e) {
      throw new RuntimeException(e);
    }
    this.compGreater = new Function<String, Boolean>() {
      @Override
      public Boolean apply(String s) {
        try {
          throw new DOMNodeVariantIllegalComparisonException(type.name(), ">");
        } catch (DOMNodeVariantIllegalComparisonException e) {
          throw new RuntimeException(e);
        }
      }
    };
    this.compLesser = new Function<String, Boolean>() {
      @Override
      public Boolean apply(String s) {
        try {
          throw new DOMNodeVariantIllegalComparisonException(type.name(), "<");
        } catch (DOMNodeVariantIllegalComparisonException e) {
          throw new RuntimeException(e);
        }
      }
    };
    this.compEquals = new Function<String, Boolean>() {
      @Override
      public Boolean apply(String s) {
        return variant.equals(s);
      }
    };
    this.compNotEquals = new Function<String, Boolean>() {
      @Override
      public Boolean apply(String s) {
        return !compEquals.apply(s);
      }
    };
  }
  public NodeVariant(boolean value) {
    this.variant = value;
    try {
      this.type = getType();
    } catch (DOMNodeVariantIllegalVariantTypeException e) {
      throw new RuntimeException(e);
    }
    this.compGreater = new Function<String, Boolean>() {
      @Override
      public Boolean apply(String s) {
        try {
          throw new DOMNodeVariantIllegalComparisonException(type.name(), ">");
        } catch (DOMNodeVariantIllegalComparisonException e) {
          throw new RuntimeException(e);
        }
      }
    };
    this.compLesser = new Function<String, Boolean>() {
      @Override
      public Boolean apply(String s) {
        try {
          throw new DOMNodeVariantIllegalComparisonException(type.name(), "<");
        } catch (DOMNodeVariantIllegalComparisonException e) {
          throw new RuntimeException(e);
        }
      }
    };
    this.compEquals = new Function<String, Boolean>() {
      @Override
      public Boolean apply(String s) {
        return (Boolean)variant == Boolean.parseBoolean(s);
      }
    };
    this.compNotEquals = new Function<String, Boolean>() {
      @Override
      public Boolean apply(String s) {
        return !compEquals.apply(s);
      }
    };
  }
  public NodeVariant() {
    this.variant = null;
    try {
      this.type = getType();
    } catch (DOMNodeVariantIllegalVariantTypeException e) {
      throw new RuntimeException(e);
    }
  }

  private NodeVariantType getType() throws DOMNodeVariantIllegalVariantTypeException {
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

  public boolean equals(String value) {
    return this.compEquals.apply(value);
  }
  public boolean notEquals(String value) {
    return this.compNotEquals.apply(value);
  }
  public boolean greater(String value) {
    return this.compGreater.apply(value);
  }
  public boolean lesser(String value) {
    return this.compLesser.apply(value);
  }
}
