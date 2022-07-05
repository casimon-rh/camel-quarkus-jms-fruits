package com.redhat;

import java.io.Serializable;
import java.util.Objects;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Fruit implements Serializable {
  private static final long serialVersionUID = 1L;
  private String name;


  public Fruit() {
  }

  public Fruit(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Fruit other = (Fruit) obj;
    return Objects.equals(name, other.name);
  }
}
