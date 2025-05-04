package org.morkato.bot.commands;

import org.morkato.bmt.components.CommandHandler;
import org.morkato.bmt.context.CommandContext;
import org.morkato.utility.mcisid.McisidUtil;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class McisidInspect implements CommandHandler<String> {
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
  public static String mapModel(int model) {
    return switch (model) {
      case 0 -> "GENERIC";
      case 1 -> "RPG";
      case 15 -> "ART";
      case 16 -> "ATTACK";
      case 17 -> "ABILITY";
      case 18 -> "FAMILY";
      default -> "UNKNOWN";
    };
  }
  @Override
  public void invoke(CommandContext<String> ctx) {
    final String mcisid = ctx.getDefinedArguments();
    final int identifier = McisidUtil.getIdentifier(mcisid);
    if (identifier != 0)
      throw new RuntimeException("A versão do ID: " + mcisid + "/" + identifier + " não é reconhecida, portanto, não existe.");
    final int model = McisidUtil.getV1OriginModel(mcisid);
    final int seqnext = McisidUtil.getV1Sequence(mcisid);
    final long instant = McisidUtil.getV1InstantInMilis(mcisid);
    final Instant createdAt = Instant.ofEpochMilli(TimeUnit.SECONDS.toMillis(1716994800) + instant);
    if (model == -1)
        throw new RuntimeException("O modelo do ID: " + mcisid + "/unknown é inválido.");
    ctx.reply()
      .setContent("**Versão do ID:** " + mcisid + "/" + identifier
                  + "\n**Modelo mapeado do ID (reflexão):** " + mapModel(model)
                  + "\n**Coleção:** " + seqnext
                  + "\n**Criado em (Gerado em):** " + formatter.format(createdAt.atZone(ZoneId.systemDefault())))
      .mentionRepliedUser(false)
      .queue();
  }
}
