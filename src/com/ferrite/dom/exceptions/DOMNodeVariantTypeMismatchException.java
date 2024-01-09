package com.ferrite.dom.exceptions;

public class DOMNodeVariantTypeMismatchException extends DOMException {
  public DOMNodeVariantTypeMismatchException(Class requested, Class actual) {
    super(String.format("The requested type '%s' is different from the real type '%s'", requested.getTypeName(), actual.getTypeName()));
  }
}
