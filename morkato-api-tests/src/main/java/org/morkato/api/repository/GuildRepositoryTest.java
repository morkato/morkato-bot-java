package org.morkato.api.repository;

import org.morkato.api.entity.values.GuildDefaultValue;
import org.morkato.api.exception.guild.GuildIdInvalidError;
import org.morkato.api.exception.guild.GuildNotFoundError;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.*;
import org.morkato.api.entity.guild.Guild;
import org.morkato.api.repository.queries.GuildCreateQuery;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class GuildRepositoryTest {
  protected GuildRepository repository;
  public abstract GuildRepository createRepository();
  public abstract void cleanRepository();
  public abstract void snapshotRepository();

  private void assertGuildEqualsWithDefaultValue(Guild guild, GuildDefaultValue values) {
    Assertions.assertTrue(values.isHumanInitialLife(guild),
      () -> "Eu esperava de inicio o valor: " + values.getHumanInitialLife() + ", mas recebi: " + guild.getHumanInitialLife());
    Assertions.assertTrue(values.isOniInitialLife(guild),
      () -> "Eu esperava de inicio o valor: " + values.getOniInitialLife() + ", mas recebi: " + guild.getOniInitialLife());
    Assertions.assertTrue(values.isHybridInitialLife(guild),
      () -> "Eu esperava de inicio o valor: " + values.getHybridInitialLife() + ", mas recebi: " + guild.getHybridInitialLife());
    Assertions.assertTrue(values.isBreathInitial(guild),
      () -> "Eu esperava de inicio o valor: " + values.getBreathInitial() + ", mas recebi: " + guild.getBreathInitial());
    Assertions.assertTrue(values.isBloodInitial(guild),
      () -> "Eu esperava de inicio o valor: " + values.getBloodInitial() + ", mas recebi: " + guild.getBloodInitial());
    Assertions.assertTrue(values.isAbilityRoll(guild),
      () -> "Eu esperava de inicio o valor: " + values.getAbilityRoll() + ", mas recebi: " + guild.getAbilityRoll());
    Assertions.assertTrue(values.isFamilyRoll(guild),
      () -> "Eu esperava de inicio o valor: " + values.getFamilyRoll() + ", mas recebi: " + guild.getFamilyRoll());
  }

  @BeforeAll
  public void setup() {
    repository = createRepository();
  }

  @BeforeEach
  public void clean() {
    try {
      cleanRepository();
    } catch (Throwable exc) {
      System.out.println("Clean error:" + exc.getClass().getName() + ": " + exc.getMessage());
      throw exc;
    }
  }

  @BeforeEach
  public void snapshot() {
    snapshotRepository();
  }

  @Test
  @DisplayName("Teste ID da guild inválido")
  public void testGuildIdIsInvalid() {
    /* DATA */
    final String id = "1234abc";
    /* ASSERT & VALIDATE */
    ExecutionException exc = Assertions.assertThrows(ExecutionException.class, () -> {
      repository.fetch(id).get();
    });
    Throwable cause = exc.getCause();
    Assertions.assertInstanceOf(GuildIdInvalidError.class, cause,
      "Esperava GuildIdInvalidError, mas recebi: " + cause.getClass().getName());
  }

  @Test
  @DisplayName("Teste ID não existente")
  public void testGuildNotFound() {
    /* DATA */
    final String id = "123456789123456789";
    /* ASSERT & VALIDATE */
    ExecutionException exc = Assertions.assertThrows(ExecutionException.class, () -> {
      repository.fetch(id).get();
    });
    Throwable cause = exc.getCause();
    Assertions.assertInstanceOf(GuildNotFoundError.class, cause,
      () -> "Esperava o erro: GuildNotFound, mas foi retornado: " + cause.getClass().getName());
    GuildNotFoundError expected = (GuildNotFoundError)cause;
    Assertions.assertEquals(id, expected.getId(),
      () -> "O ID que gerou o erro é diferente do esperado: " + id + ", mas foi retornado: " + expected.getId());
  }

  @Test
  @DisplayName("Teste de criação e obtenção de uma guilda válida")
  public void testGuildCreateAndGet() throws Throwable {
    /* DATA */
    final String id = "987654321987654321";
    final GuildCreateQuery query = new GuildCreateQuery()
      .setId(id);
    final GuildDefaultValue values = GuildDefaultValue.getDefault();
    /* ASSERT & VALIDATE */
    Guild guild = repository.create(query).get();
    System.out.println("Created Guild: " + Guild.representation(guild));
    Assertions.assertEquals(id, guild.getId(),
      () -> "Eu esperava o ID: " + id + ", mas recebi: " + guild.getId());
    this.assertGuildEqualsWithDefaultValue(guild, values);
    Guild same = repository.fetch(id).get();
    System.out.println("Recovered Guild: " + Guild.representation(same));
    Assertions.assertEquals(id, same.getId(), "O ID que eu esperava para recuperar dados era: " + id + ", mas eu recebi: " + same.getId());
    this.assertGuildEqualsWithDefaultValue(same, values);
  }

  @Test
  @DisplayName("Teste de criação e obtenção de uma guilda válida com valores não default")
  public void testGuildCreateAndGetWithoutDefaultValues() throws Throwable {
    /* DATA */
    final String id = "987654321987654321";
    final GuildDefaultValue values = new GuildDefaultValue()
      .setHumanInitialLife(new BigDecimal(75000).setScale(0, RoundingMode.HALF_UP))
      .setOniInitialLife(new BigDecimal(50000).setScale(0, RoundingMode.HALF_UP))
      .setHybridInitialLife(new BigDecimal(35000).setScale(0, RoundingMode.HALF_UP))
      .setBreathInitial(new BigDecimal(25000).setScale(0, RoundingMode.HALF_UP))
      .setBloodInitial(new BigDecimal(45000).setScale(0, RoundingMode.HALF_UP))
      .setAbilityRoll(new BigDecimal(10).setScale(0, RoundingMode.HALF_UP))
      .setFamilyRoll(new BigDecimal(10).setScale(0, RoundingMode.HALF_UP));
    final GuildCreateQuery query = new GuildCreateQuery()
      .setId(id)
      .setHumanInitialLife(values.getHumanInitialLife())
      .setOniInitialLife(values.getOniInitialLife())
      .setHybridInitialLife(values.getHybridInitialLife())
      .setBreathInitial(values.getBreathInitial())
      .setBloodInitial(values.getBloodInitial())
      .setAbilityRoll(values.getAbilityRoll())
      .setFamilyRoll(values.getFamilyRoll());
    /* ASSERT & VALIDATE */
    Guild guild = repository.create(query).get();
    System.out.println("Created Guild: " + Guild.representation(guild));
    this.assertGuildEqualsWithDefaultValue(guild, values);
    Guild recovered = repository.fetch(id).get();
    System.out.println("Recovered Guild: " + Guild.representation(recovered));
    this.assertGuildEqualsWithDefaultValue(recovered, values);
  }

  @Test
  @DisplayName("Teste de criação e exclusão de uma guilda")
  public void testGuildCreateAndDelete() throws Throwable {
    /* DATA */
    final String id = "987654321987654321";
    final GuildCreateQuery query = new GuildCreateQuery()
      .setId(id);
    /* ASSERT & VALIDATE */
    Guild guild = repository.create(query).get();
    System.out.println("Created Guild: " + Guild.representation(guild));
    Guild recovered = repository.fetch(id).get();
    System.out.println("Recovered Guild: " + Guild.representation(guild));
    repository.delete(guild).get();
    System.out.println("To deleted Guild: " + Guild.representation(guild));
    ExecutionException exc = Assertions.assertThrows(ExecutionException.class, () -> {
      repository.fetch(id).get();
    });
    Throwable cause = exc.getCause();
    Assertions.assertInstanceOf(GuildNotFoundError.class, cause,
      () -> "Esperava o erro: GuildNotFound, mas foi retornado: " + cause.getClass().getName());
    GuildNotFoundError expected = (GuildNotFoundError)cause;
    Assertions.assertEquals(id, expected.getId(),
      () -> "O ID que gerou o erro é diferente do esperado: " + id + ", mas foi retornado: " + expected.getId());
  }
}
