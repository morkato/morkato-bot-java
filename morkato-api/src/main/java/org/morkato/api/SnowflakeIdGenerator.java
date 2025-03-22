package org.morkato.api;

public class SnowflakeIdGenerator {
  private static final SnowflakeIdGenerator DEFAULT_GENERATOR = new SnowflakeIdGenerator(1716973200000L, 1);
  private static final int WORKER_BITS = 10;
  private static final int SEQUENCE_BITS = 10;
  private static final long SEQUENCE_MASK = (1 << SEQUENCE_BITS) - 1;
  private final long epoch;
  private final int worker;
  private long sequence = 0L;

  public static long staticNext() {
    return DEFAULT_GENERATOR.next();
  }

  public static String staticNextAsString() {
    return DEFAULT_GENERATOR.nextAsString();
  }

  public SnowflakeIdGenerator(long epoch, int worker) {
    this.epoch = epoch;
    this.worker = worker;
  }

  private int nextInSequence() {
    ++sequence;
    return (int)(sequence % 1024);
  }

  public synchronized long next() {
    long now = System.currentTimeMillis() - this.epoch;
    int seq = this.nextInSequence();
    long result = 0L;
    result = (now << 23);
    result |= ((long)worker << 10);
    result |= seq;
    return result;
  }

  public String nextAsString() {
    return String.valueOf(this.next());
  }
}
