package com.ferrite.dom.exceptions;

public class DOMNodeVariantIllegalComparisonException extends DOMException {
  public DOMNodeVariantIllegalComparisonException(String type, String op) {
    super(String.format("Cannot compare type '%s' using the operator '%s'", type, op));
  }
}
