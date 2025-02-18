package com.morkato.bmt.annotation;

import com.morkato.bmt.CommandCallback;
import com.morkato.bmt.commands.Context;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Command {
  private final String name;
  private final CommandCallback callback;
  private String description;
  private List<String> aliases = new ArrayList<>();

  public Command(
    @NotNull String name,
    @NotNull CommandCallback callback
  ) {
    this.name = name;
    this.callback = callback;
  }

  public void invoke(Context ctx) throws Throwable {
    this.callback.accept(ctx);
  }
  public String[] getAliases() {
    return (String[]) aliases.toArray();
  }
  public void alias(
    @NotNull String alias
  ) {
    this.aliases.add(alias);
  }

  public String getName() {
    return this.name;
  }

  public String getDescription() {
    return this.description;
  }
  public Command setDescription(String newvl) {
    description = newvl;
    return this;
  }
}
