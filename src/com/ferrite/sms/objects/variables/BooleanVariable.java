package com.ferrite.sms.objects.variables;

import com.ferrite.sms.StateMachineObject;
import com.ferrite.sms.dependencies.Dependency;

public class BooleanVariable extends StateMachineObject {
  private boolean value;

  public BooleanVariable(Dependency[] dependencies, boolean value) {
    super(dependencies);
    this.value = value;
  }

  public boolean isValue() {
    return value;
  }

  public void setValue(boolean value) {
    this.value = value;
  }
}
