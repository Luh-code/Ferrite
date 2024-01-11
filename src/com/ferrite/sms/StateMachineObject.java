package com.ferrite.sms;

import com.ferrite.sms.dependencies.Dependency;
import com.ferrite.sms.exceptions.SMSUnknownSMOException;
import com.ferrite.sms.objects.Machine;
import com.ferrite.sms.objects.SMOType;
import com.ferrite.sms.objects.State;
import com.ferrite.sms.objects.System;

import java.util.ArrayList;
import java.util.List;

public class StateMachineObject {
  private SMOType type;
  private ArrayList<Dependency> dependencies;
  private int hash;

  public StateMachineObject(Dependency[] dependencies) {
    this.dependencies = new ArrayList<>(List.of(dependencies));
  }

  private int computeDependencyHash() {
    int result = 1;
    for (Dependency dependency : dependencies) {
      result = 31 * result + dependency.getDomHash();
    }
    return result;
  }

  public boolean checkDependencyHash() {
    return this.hash == computeDependencyHash();
  }

  public void recomputeDependencyHash() {
    this.hash = computeDependencyHash();
  }

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
