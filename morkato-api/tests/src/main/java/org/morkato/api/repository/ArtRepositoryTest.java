package org.morkato.api.repository;

import static org.assertj.core.api.Assertions.*;

import org.morkato.api.entity.art.ArtId;
import org.morkato.api.exception.art.ArtNotFoundException;
import org.morkato.api.repository.art.ArtIdQuery;
import org.morkato.api.repository.art.ArtRepository;
import org.morkato.api.entity.guild.GuildId;
import org.junit.jupiter.api.*;
import org.morkato.api.repository.attack.AttackIdQuery;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class ArtRepositoryTest {
  protected abstract ArtRepository createRepository();
  protected abstract GuildId createGuild();
  protected abstract void clean();

  private ArtRepository repository;

  @BeforeAll
  public void start() {
    this.repository = this.createRepository();
  }

  @BeforeEach
  public void update() {
    this.clean();
  }

  @Test
  @DisplayName("Teste de obtenão de um arte inexistente")
  public void testArtNotFound() {
    final String VALID_ART_ID = "11111111111111111";
    final GuildId guild = this.createGuild();
    final ArtId artId = new ArtIdQuery(guild, VALID_ART_ID);

    assertThatExceptionOfType(ArtNotFoundException.class)
      .as("Uma arte que não existe, não deve ser obtida no repositório. Pelo menos, eu acho.")
      .isThrownBy(() -> repository.fetch(artId))
      .extracting(ArtNotFoundException::get)
  }
}
