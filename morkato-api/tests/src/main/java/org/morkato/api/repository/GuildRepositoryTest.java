package org.morkato.api.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class GuildRepositoryTest {
  protected abstract GuildRepository getRepository();
  protected abstract void clean();

  private GuildRepository repository;

  @BeforeAll
  public void start() {
    this.repository = this.getRepository();
  }

  @BeforeEach
  public void update() {
    this.clean();
  }

  /* TODO: Adicionar testes. */
}
