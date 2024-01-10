package com.ferrite.sms;

import com.ferrite.sms.exceptions.SMSUnknownSMOException;
import com.ferrite.sms.objects.Machine;
import com.ferrite.sms.objects.SMOType;
import com.ferrite.sms.objects.State;
import com.ferrite.sms.objects.System;

public class StateMachineObject {
  private SMOType type;

  public static <T extends StateMachineObject> SMOType getSMOType(T smo) throws SMSUnknownSMOException {
    switch (smo) {
      case State state -> {
        return SMOType.STATE;
      }
      case Machine machine -> {
        return SMOType.MACHINE;
      }
      case System system -> {
        return SMOType.SYSTEM;
      }
      default -> {
        throw new SMSUnknownSMOException(smo.getClass());
      }
    }
  }
}
