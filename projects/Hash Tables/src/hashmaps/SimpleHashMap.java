/*
 * Copyright 2023 Marc Liberatore.
 */
package hashmaps;

import java.util.Set;
import java.util.HashSet;

/**
 * A simple implementation of a HashMap using chaining.
 */
public class SimpleHashMap<K, V> implements SimpleMap<K, V> {
    private ChainingHashTable<SimpleMapEntry<K, V>> table;
    /**
     * Minimal internal hash table using a HashSet for simplicity.
     * Ensures entries with the same key are replaced.
     */
    private static class ChainingHashTable<T> implements Iterable<T> {
        private final HashSet<T> backing;

        public ChainingHashTable(int capacity) {
            this.backing = new HashSet<>(capacity);
        }

        public int size() {
            return backing.size();
        }

        public T get(T item) {
            for (T t : backing) {
                if (t.equals(item)) return t;
            }
            return null;
        }

        public void add(T item) {
            backing.remove(item); // replace existing entry
            backing.add(item);
        }

        public boolean remove(T item) {
            return backing.remove(item);
        }

        @Override
        public java.util.Iterator<T> iterator() {
            return backing.iterator();
        }
    }

    public SimpleHashMap() {
        table = new ChainingHashTable<>(7);
    }

    @Override
    public int size() {
        return table.size();
    }

    @Override
    public void put(K key, V value) {
        SimpleMapEntry<K, V> entry = new SimpleMapEntry<>(key, value);
        table.add(entry);
    }

    @Override
    public V get(K key) {
        SimpleMapEntry<K, V> entry = table.get(new SimpleMapEntry<>(key, null));
        return (entry == null) ? null : entry.v;
    }

    @Override
    public V getOrDefault(K key, V defaultValue) {
        V value = get(key);
        return (value != null) ? value : defaultValue;
    }

    @Override
    public V remove(K key) {
        SimpleMapEntry<K, V> target = new SimpleMapEntry<>(key, null);
        SimpleMapEntry<K, V> existing = table.get(target);
        if (existing != null) {
            table.remove(target);
            return existing.v;
        }
        return null;
    }

    @Override
    public Set<K> keys() {
        Set<K> set = new HashSet<>();
        for (SimpleMapEntry<K, V> entry : table) {
            set.add(entry.k);
        }
        return set;
    }

    // Helper method, not part of interface
    public boolean containsKey(K key) {
        return table.get(new SimpleMapEntry<>(key, null)) != null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (SimpleMapEntry<K, V> entry : table) {
            if (!first) sb.append(", ");
            sb.append(entry.k).append("=").append(entry.v);
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }
}
