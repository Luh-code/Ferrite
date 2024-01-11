package com.ferrite.sms.objects;

import com.ferrite.sms.StateMachineObject;
import com.ferrite.sms.dependencies.Dependency;
import com.ferrite.sms.objects.variables.BooleanVariable;

import java.util.ArrayList;

public class State extends StateMachineObject {
  private BooleanVariable origin;
  private BooleanVariable entry;
  private ArrayList<? extends StateMachineObject> begin; // Only supposed to hold Outputs and Triggers
  private ArrayList<Transition> transitions;
  private ArrayList<? extends StateMachineObject> end; // Only supposed to hold Outputs and Triggers

  public State(Dependency[] dependencies) {
    super(dependencies);
  }
}
