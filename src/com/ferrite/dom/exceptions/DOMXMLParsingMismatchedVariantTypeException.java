package com.ferrite.dom.exceptions;

public class DOMXMLParsingMismatchedVariantTypeException extends DOMXMLException {
  public DOMXMLParsingMismatchedVariantTypeException(NumberFormatException e) {
    super(String.format("Tried to parse wrong variantType: '%s'", e.getMessage()));
  }
}
