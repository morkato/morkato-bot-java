package org.morkato.bmt.utility;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class StringView {
  private static final Map<Character, Character> quotes = new HashMap<>();
  private final String buffer;
  private int idx = 0;
  private int prev = 0;
  static {
    quotes.put('\"', '\"');
  }
  public StringView(
    @NotNull String buffer
  ) {
    this.buffer = buffer;
  }
  public boolean eof() {
    return idx >= length();
  }
  public void undo() {
    this.idx = prev;
  }
  public char current() {
    try {
      return this.buffer.charAt(idx);
    } catch (IndexOutOfBoundsException exc) {
      return '\0';
    }
  }
  public char get() {
    if (this.eof()) {
      return '\0';
    }
    prev = idx;
    idx += 1;
    return this.current();
  }
  public String read(int length) {
    if (idx + length >= this.length())
      return null;
    String current = buffer.substring(idx, idx + length);
    prev = idx;
    idx += length;
    return current;
  }
  public String quotedWord() {
    StringBuffer buffer = new StringBuffer(0);
    this.skipWhitespace();
    char current = this.current();
    if (current == '\0')
      return null;
    int prev = idx;
    Character closeQuote = quotes.get(current);
    boolean isQuoted = closeQuote != null;
    if (!isQuoted)
      buffer.append(current);
    while (!this.eof()) {
      current = this.get();
      if (current == '\0') {
        if (isQuoted)
          // TODO: Add a custom error for this context.
          throw new RuntimeException();
        break;
      }
      if (current == '\\') {
        char next = this.get();
        if (next == '\0') {
          if (isQuoted)
            // TODO: Add a custom error for this context.
            throw new RuntimeException();
          break;
        }
        if (next == closeQuote){
          buffer.append(next);
        } else {
          this.undo();
          buffer.append(current);
        }
        continue;
      }
      if (isQuoted && current == closeQuote) {
        char next = this.get();
        boolean valid = next == '\0' || Character.isWhitespace(next);
        if (!valid)
          // TODO: Add a custom error for this context.
          throw new RuntimeException();
        break;
      }
      if (Character.isWhitespace(current) && !isQuoted)
        break;
      buffer.append(current);
    }
    this.prev = prev;
    return buffer.toString();
  }
  public String word() {
    StringBuffer buffer = new StringBuffer(0);
    this.skipWhitespace();
    int prev = idx;
    char at = this.current();
    while (at != '\0') {
      buffer.append(at);
      at = this.get();
      if (Character.isWhitespace(at)) {
        break;
      }
    }
    this.prev = prev;
    return !buffer.isEmpty() ? buffer.toString() : null;
  }
  public String rest() {
    String rest = buffer.substring(idx, length());
    prev = idx;
    idx = length();
    return rest;
  }
  public int length() {
    return buffer.length();
  }

  public void skipWhitespace() {
    int prev = this.idx;
    char current = this.current();
    while (Character.isWhitespace(current)) {
      if (current == '\0')
        break;
      current = this.get();
    }
    this.prev = prev;
  }

  @Override
  public String toString() {
    char current = this.current();;
    return "StringView(current=" + (current == '\0' ? "<eof character>" : current)  + ", index=" + this.idx + ", buffer=\"" + this.buffer + "\")";
  }
}
