package org.morkato.database;

import org.postgresql.util.PSQLException;

import java.util.Objects;

public class PSQLViolations {
  public static boolean isDuplicateKeyViolation(PSQLException exception, String constraintname) {
    final String message = exception.getMessage();
    final String part = "duplicate key value violates unique constraint \"" + constraintname + "\"";
    if (Objects.isNull(message))
      return false;
    return message.contains(part);
  }
}
