package org.morkato.bmt.registration;

public interface MapRegistryManagement<K, V> {
  V get(K key);
}
