package core.basesyntax;

import java.util.Collection;

public interface MyMap<K, V> {
    void put(K key, V value);

    V getValue(K key);

    int getSize();

    int getCapacity();

    boolean containsKey(Object key);

    Collection<V> values();

    void clear();
}
