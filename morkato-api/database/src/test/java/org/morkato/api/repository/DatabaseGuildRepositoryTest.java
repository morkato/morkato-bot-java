package org.morkato.api.repository;

import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DatabaseGuildRepositoryTest extends GuildRepositoryTest {
  @Override
  protected GuildRepository getRepository() {
    return null;
  }

  @Override
  protected void clean() {

  }
}
