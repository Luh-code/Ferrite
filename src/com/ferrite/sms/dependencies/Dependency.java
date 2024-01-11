package com.ferrite.sms.dependencies;

import com.ferrite.dom.DOMNode;
import com.ferrite.sms.StateMachineObject;

public class Dependency {
  private DOMNode domDependency;
  private StateMachineObject smo;
  private int domHash;

  public Dependency (DOMNode domDependency, StateMachineObject smo) {
    this.domDependency = domDependency;
    this.smo = smo;
  }

  private void hash() {
    this.domHash = domDependency.hashCode();
  }

  private boolean isDOMHashUpToDate() {
    return domHash == domDependency.hashCode();
  }


  public void updateSMO(boolean force) {
    if (!force && isDOMHashUpToDate()) {
      return;
    }

  }

  public int getDomHash() {
    return domHash;
  }
}
