package com.ferrite;

import com.ferrite.dom.exceptions.DOMException;

public class FerriteException extends Throwable {
  protected FerriteException(String message)  {
    super(message);
  }
}
