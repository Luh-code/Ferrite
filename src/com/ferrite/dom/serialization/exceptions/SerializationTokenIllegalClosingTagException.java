package com.ferrite.dom.serialization.exceptions;

public class SerializationTokenIllegalClosingTagException extends SerializationException {
  public SerializationTokenIllegalClosingTagException(String tag) {
    super(String.format("Found closing tag without opening tag: '%s'", tag));
  }
}
