package com.ferrite.dom.serialization.exceptions;

public class SerializationMismatchedAttributeValueCountException extends SerializationException {
  public SerializationMismatchedAttributeValueCountException(int nameCount, int valueCount) {
    super(String.format("Mismatched amount of attribute names and values: %d(names)!=%d(values)", nameCount, valueCount));
  }
}
