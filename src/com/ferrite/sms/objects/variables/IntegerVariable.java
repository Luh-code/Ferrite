package com.ferrite.sms.objects.variables;

import com.ferrite.sms.StateMachineObject;
import com.ferrite.sms.dependencies.Dependency;

public class IntegerVariable extends StateMachineObject {
  private int value;
  public IntegerVariable(Dependency[] dependencies, int value) {
    super(dependencies);
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }
}
