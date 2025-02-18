package com.morkato.service;

import com.morkato.service.database.DatabaseManager;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MorkatoService {
  private class Art {
    private Long id;
    private String name;
  }
  public static void main(String[] args) throws Throwable {
    DatabaseManager database = new DatabaseManager("jdbc:postgresql://localhost:8080/morkato-dev", "morkato", "morkato");
    database.migrate();
    DataSource source = database.getDataSource();
    Connection conn = source.getConnection();
    DSLContext dsl = DSL.using(conn,SQLDialect.POSTGRES);
    var rs = dsl.select(DSL.table("arts"), DSL.field("id"), DSL.field("name")).fetchInto(Art.class);
    for (Art art : rs) {
      System.out.println(art.name);
    }
  }
}
