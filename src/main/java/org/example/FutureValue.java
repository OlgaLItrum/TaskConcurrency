package org.example;

public class FutureValue {

  private final int value;
  private final String infoAboutTask;


  @Override
  public String toString() {
    return "FutureReturn{" +
      "value=" + value +
      ", infoAboutTask='" + infoAboutTask + '\'' +
      '}';
  }

  public FutureValue(int value, String infoAboutTask) {
    this.value = value;
    this.infoAboutTask = infoAboutTask;
  }
}
