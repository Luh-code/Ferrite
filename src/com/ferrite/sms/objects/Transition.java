package com.ferrite.sms.objects;

import com.ferrite.sms.StateMachineObject;
import com.ferrite.sms.dependencies.Dependency;

public class Transition extends StateMachineObject {
  public Transition(Dependency[] dependencies) {
    super(dependencies);
  }
}
