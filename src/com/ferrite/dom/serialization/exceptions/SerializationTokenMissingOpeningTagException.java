package com.ferrite.dom.serialization.exceptions;

import com.ferrite.dom.serialization.XMLToken;

public class SerializationTokenMissingOpeningTagException extends SerializationException {
  public SerializationTokenMissingOpeningTagException(XMLToken token) {
    super(String.format("The token '%s' is missing an opening tag", token.toString()));
  }
}
