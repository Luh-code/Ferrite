package com.ferrite.sms.objects;

import com.ferrite.sms.StateMachineObject;
import com.ferrite.sms.dependencies.Dependency;

import java.util.ArrayList;

public class Machine extends StateMachineObject {
  private ArrayList<State> states;
  private ArrayList<Output> outputs;
  private ArrayList<Trigger> triggers;

  public Machine(Dependency[] dependencies) {
    super(dependencies);
  }

}
