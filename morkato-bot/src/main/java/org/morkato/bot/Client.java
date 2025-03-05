package org.morkato.bot;

import org.morkato.bmt.Main;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.lang.management.ManagementFactory;

public class Client {
  public static void main(String[] args) throws Exception {
    Main.runApplication(Client.class);
  }

  static class CustomSignalHandler implements SignalHandler {
    @Override
    public void handle(Signal signal) {
      System.out.println("🔴 Sinal capturado: " + signal.getName());
    }
  }
}
