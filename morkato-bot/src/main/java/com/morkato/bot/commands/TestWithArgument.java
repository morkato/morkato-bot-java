package com.morkato.bot.commands;

import com.morkato.bmt.annotation.ExtensionRegistry;
import com.morkato.bmt.commands.Command;
import com.morkato.bmt.commands.Context;
import com.morkato.bmt.commands.StringView;

@ExtensionRegistry(extension = com.morkato.bot.extension.Extension.class)
public class TestWithArgument implements Command<TestWithArgument.TestArg> {
  public record TestArg(
    String text
  ) {}
  @Override
  public void invoke(Context<TestWithArgument.TestArg> context) throws Throwable {
    context.send("Argument: **" + context.getArgs() + "**").queue();
  }
}
