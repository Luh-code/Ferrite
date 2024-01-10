package com.ferrite.sms.exceptions;

import com.ferrite.sms.StateMachineObject;

public class SMSUnknownSMOException extends SMSException {
  public SMSUnknownSMOException(Class<? extends StateMachineObject> clazz) {
    super(String.format("Unrecognized SMO Type '%s'", clazz.getTypeName()));
  }
}
