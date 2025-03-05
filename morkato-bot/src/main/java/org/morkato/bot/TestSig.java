package org.morkato.bot;

import sun.misc.SignalHandler;
import sun.misc.Signal;

import java.lang.management.ManagementFactory;
import java.util.List;

public class TestSig {
  public static void main(String[] args) {
    Signal.handle(new Signal("TERM"), new CustomSignalHandler());
    Signal.handle(new Signal("INT"), new CustomSignalHandler());
    Signal.handle(new Signal("HUP"), new CustomSignalHandler());
    String pid = ManagementFactory.getRuntimeMXBean().getName();
    System.out.println(pid);
    while (true) {
      try {
        Thread.sleep(1000);
      } catch(InterruptedException e) {
        break;
      }
      System.out.println("Loop");
    }
    System.out.println("Terminado.");
  }
  static class CustomSignalHandler implements SignalHandler {
    @Override
    public void handle(Signal signal) {
      System.out.println("🔴 Sinal capturado: " + signal.getName());
    }
  }
}
