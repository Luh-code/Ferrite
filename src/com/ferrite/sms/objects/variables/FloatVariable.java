package com.ferrite.sms.objects.variables;

import com.ferrite.sms.StateMachineObject;
import com.ferrite.sms.dependencies.Dependency;

public class FloatVariable extends StateMachineObject {
  private float value;

  public FloatVariable(Dependency[] dependencies, float value) {
    super(dependencies);
    this.value = value;
  }

  public float getValue() {
    return value;
  }

  public void setValue(float value) {
    this.value = value;
  }
}
