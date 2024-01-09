package com.ferrite.dom.exceptions;

public class DOMXMLParsingIllegalNoneTypeVariantSettingException extends DOMXMLException {
  public DOMXMLParsingIllegalNoneTypeVariantSettingException(String value) {
    super(String.format("Tried setting nodeVariant of type NONE to '%s'", value));
  }
}
