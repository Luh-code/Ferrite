package com.ferrite.serialization.exceptions;

import com.ferrite.serialization.XMLToken;

public class SerializationTokenMissingOpeningTagException extends SerializationException {
  public SerializationTokenMissingOpeningTagException(XMLToken token) {
    super(String.format("The token '%s' is missing an opening tag", token.toString()));
  }
}
