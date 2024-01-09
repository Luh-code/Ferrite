package com.ferrite.dom.exceptions;

public class DOMNodeVariantIllegalVariantTypeException extends DOMException {
  public DOMNodeVariantIllegalVariantTypeException(Class clazz) {
    super(String.format("The type '%s' is not allowed an a NodeVariant", clazz.getTypeName()));
  }
}
