package org.morkato.bot.commands;

import org.morkato.bmt.annotation.AutoInject;
import org.morkato.bmt.annotation.Component;
import org.morkato.bmt.context.TextCommandContext;
import org.morkato.bmt.components.Command;
import org.morkato.api.entity.art.Art;
import org.morkato.bot.parameters.ArtCommandData;
import org.morkato.bot.parameters.ArtOption;
import org.morkato.bot.parsers.ArtParser;
import java.util.Objects;

@Component
public class ArtCommand implements Command<Art> {
  @AutoInject
  ArtParser parser;
  @Override
  public void invoke(TextCommandContext<Art> ctx) {
//    ArtCommandData data = ctx.getDefinedArguments();
//    ArtOption option = data.option();
//    String query = data.query();
//    if (Objects.isNull(option))
//      /* Caso não haja prefixo definido */
//      option = ArtOption.GET;
//    /* TODO: Adicionar suporte a todas opções */
//    if (!Objects.equals(option, ArtOption.GET))
//      return;
//    Art art = parser.parse(ctx, query); /* Obtém a arte com base no nome e/ou ID */
//    /* TODO: Adicionar suporte às embeds */
//    if (Objects.isNull(art))
//      throw new RuntimeException("A arte (Respiração, Kekkijutsu ou Estilo de Luta): **" + query + "** não existe.");
//    ctx.sendMessage("" + art).queue();
    ctx.sendMessage("EXECUTOU o comando principal.").queue();
  }
}
