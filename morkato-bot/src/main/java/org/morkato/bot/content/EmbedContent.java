package org.morkato.bot.content;

import org.morkato.bmt.annotation.ContentRequire;
import org.morkato.bmt.annotation.Component;
import org.morkato.bmt.content.ContentProvider;
import org.morkato.bmt.content.ContentReference;

@ContentRequire("content/embeds.yml")
@Component
public class EmbedContent {
  private static final String DEFAULT_RESPIRATION_TITLE = "Respiração: {}";
  private static final String DEFAULT_KEKKIJUTSU_TITLE = "Kekkijutsu: {}";
  private static final String DEFAULT_FIGHTING_STYLE_TITLE = "Estilo de Luta: {}";
  private final ContentReference RESPIRATION_TITLE;
  private final ContentReference KEKKIJUTSU_TITLE;
  private final ContentReference FIGHTING_STYLE_TITLE;

  public EmbedContent(ContentProvider provider) {
    this.RESPIRATION_TITLE = provider.getContentReference("ptBR.respirationTitle", DEFAULT_RESPIRATION_TITLE);
    this.KEKKIJUTSU_TITLE = provider.getContentReference("ptBR.kekkijutsuTitle", DEFAULT_KEKKIJUTSU_TITLE);
    this.FIGHTING_STYLE_TITLE = provider.getContentReference("ptBR.fightstyleTitle", DEFAULT_FIGHTING_STYLE_TITLE);
  }
}
