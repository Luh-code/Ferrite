package com.ferrite.dom.serialization.exceptions;

public class SerializationTokenMissingClosingTagException extends SerializationException {
  public SerializationTokenMissingClosingTagException(String unclosedTag) {
    super(String.format("The tag '%s' was not closed", unclosedTag));
  }
}
