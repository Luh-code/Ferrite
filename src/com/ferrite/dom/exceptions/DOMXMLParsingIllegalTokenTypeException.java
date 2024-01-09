package com.ferrite.dom.exceptions;

import com.ferrite.serialization.XMLToken;

public class DOMXMLParsingIllegalTokenTypeException extends DOMXMLException {
  public <T extends XMLToken> DOMXMLParsingIllegalTokenTypeException(T token) {
    super(String.format("The token '%s' is not of a valid token type for parsing",
            token.toString()));
  }
}
