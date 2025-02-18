package com.morkato.utility;

public class Main {
  public record a(int b) {}
  public static void main(String[] args) {
    System.out.println(ClassUtility.isSubclass(a.class, Exception.class));
  }
}
