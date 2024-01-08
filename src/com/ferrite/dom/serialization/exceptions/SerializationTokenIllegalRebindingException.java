package com.ferrite.dom.serialization.exceptions;

import com.ferrite.dom.Node;
import com.ferrite.dom.serialization.XMLToken;

public class SerializationTokenIllegalRebindingException extends SerializationException {
  public SerializationTokenIllegalRebindingException(XMLToken token, Node oldNode, Node newNode) {
    super(String.format("Tried to illegally rebind node '%s' from token '%s' to node '%s'",
            oldNode.toString(), token.toString(), newNode.toString()));
  }
}
