package com.ferrite.sms.exceptions;

import com.ferrite.FerriteException;

public class SMSException extends FerriteException {
  protected SMSException(String message) {
    super(message);
  }
}
