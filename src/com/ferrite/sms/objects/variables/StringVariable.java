package com.ferrite.sms.objects.variables;

import com.ferrite.sms.StateMachineObject;
import com.ferrite.sms.dependencies.Dependency;

public class StringVariable extends StateMachineObject {
  private String value;

  public StringVariable(Dependency[] dependencies, String value) {
    super(dependencies);
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
