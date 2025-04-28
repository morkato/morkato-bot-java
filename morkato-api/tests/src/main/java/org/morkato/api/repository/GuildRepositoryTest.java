package org.morkato.api.repository;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.morkato.api.entity.guild.GuildId;
import org.morkato.api.entity.guild.GuildPayload;
import org.morkato.api.exception.guild.GuildAlreadyExistsError;
import org.morkato.api.exception.guild.GuildNotFoundException;
import org.morkato.api.repository.guild.GuildCreationQuery;
import org.morkato.api.repository.guild.GuildRepository;
import org.morkato.api.values.GuildDefaultValue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class GuildRepositoryTest {
  protected abstract GuildRepository getRepository();
  protected abstract void clean();

  private static final GuildDefaultValue defaults = GuildDefaultValue.getDefault();
  private GuildRepository repository;

  @BeforeAll
  public void start() {
    this.repository = this.getRepository();
  }

  @BeforeEach
  public void update() {
    this.clean();
  }

  private GuildCreationQuery getCreationQuery(String guildId) {
    return new GuildCreationQuery()
      .setId(guildId);
  }

  @Test
  @DisplayName("Test Get Guild Not Found")
  public void testGetGuildNotFound() {
    final String GUILD_ID = "111111111111111";
    assertThatExceptionOfType(GuildNotFoundException.class)
      .as("Uma guilda ausente não deveria ser encontrada, certo? ID testado: %s", GUILD_ID)
      .isThrownBy(() -> repository.fetch(GUILD_ID))
      .withNoCause()
      .isNotNull()
      .extracting(GuildNotFoundException::getId)
      .as("Verificar ID da GuildNotFoundError")
      .isEqualTo(GUILD_ID)
    ;
  }

  @Test
  @DisplayName("Teste de criação de guildas sem argumentos")
  public void testGuildCreate() {
    final String GUILD_ID = "55555555555555555";
    final GuildCreationQuery query = this.getCreationQuery(GUILD_ID);

    assertThat(repository.create(query))
      .as("Ao criar uma nova guilda, ela deve atribuir os valores defaults, além de ID? né não?")
      .isNotNull()
      .extracting(
        GuildPayload::getId, GuildPayload::getHumanInitialLife,
        GuildPayload::getOniInitialLife, GuildPayload::getHybridInitialLife,
        GuildPayload::getBreathInitial, GuildPayload::getBloodInitial,
        GuildPayload::getAbilityRoll, GuildPayload::getFamilyRoll
      ).containsExactly(
        GUILD_ID, defaults.getHumanInitialLife(),
        defaults.getOniInitialLife(), defaults.getHybridInitialLife(),
        defaults.getBreathInitial(), defaults.getBloodInitial(),
        defaults.getAbilityRoll(), defaults.getFamilyRoll()
      );
  }

  @Test
  @DisplayName("Teste de criação de uma guilda já existente")
  public void testGuildAlreadyExists() {
    final String GUILD_ID = "55555555555555555";
    final GuildCreationQuery query = this.getCreationQuery(GUILD_ID);
    final GuildPayload created = repository.create(query);

    assertThatExceptionOfType(GuildAlreadyExistsError.class)
      .as("Uma guilda que já existe não pode ser recriada, né?")
      .isThrownBy(() -> repository.create(query))
      .isNotNull()
      .withNoCause()
      .extracting(GuildAlreadyExistsError::getGuild)
      .extracting(GuildId::getId)
      .isEqualTo(created.getId())
    ;
  }

  @Test
  @DisplayName("Teste de recuperação da guilda")
  public void testRecoveryGuild() {
    final String GUILD_ID = "55555555555555555";
    final GuildCreationQuery query = this.getCreationQuery(GUILD_ID);
    final GuildPayload created = repository.create(query);

    assertThat(repository.fetch(GUILD_ID))
      .as("Quando uma guilda é criada, ela deve retornar a mesma guilda criada, né não?")
      .isNotNull()
      .extracting(
        GuildPayload::getId, GuildPayload::getHumanInitialLife,
        GuildPayload::getOniInitialLife, GuildPayload::getHybridInitialLife,
        GuildPayload::getBreathInitial, GuildPayload::getBloodInitial,
        GuildPayload::getAbilityRoll, GuildPayload::getFamilyRoll
      ).containsExactly(
        created.getId(), created.getHumanInitialLife(),
        created.getOniInitialLife(), created.getHybridInitialLife(),
        created.getBreathInitial(), created.getBloodInitial(),
        created.getAbilityRoll(), created.getFamilyRoll()
      );
  }

  @Test
  @DisplayName("Teste de exclusão de guilda")
  public void testDeleteGuild() {
    final String GUILD_ID = "55555555555555555";
    final GuildCreationQuery query = this.getCreationQuery(GUILD_ID);
    final GuildPayload created = repository.create(query);
    repository.delete(created);

    assertThatExceptionOfType(GuildNotFoundException.class)
      .as("Uma guilda criada e deletada deveria não persistir no banco, correto?")
      .isThrownBy(() -> repository.fetch(created))
      .isNotNull()
      .withNoCause()
      .extracting(GuildNotFoundException::getId)
      .isEqualTo(created.getId())
    ;
  }
}
