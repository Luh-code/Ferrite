package com.ferrite.sms.objects;

import com.ferrite.sms.StateMachineObject;
import com.ferrite.sms.dependencies.Dependency;

public class Trigger extends StateMachineObject {

  public Trigger(Dependency[] dependencies) {
    super(dependencies);
  }
}
