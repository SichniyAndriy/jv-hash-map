package core.basesyntax;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTORY = .75f;
    private int size;
    private int capacity;
    private Node<K, V>[] dataTable;

    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        dataTable = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > capacity * LOAD_FACTORY) {
            resize();
        }
        if (checkKey(key, value)) {
            putNewNode(getHash(key), key, value, dataTable);
            ++size;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = dataTable[getIndex(key)];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public boolean containsKey(Object key) {
        Node<K, V> node = dataTable[getIndex((K)key)];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    @Override
    public Collection<V> values() {
        Collection<V> collection = new ArrayList<>();
        for (Node<K, V> node: dataTable) {
            while (node != null) {
                collection.add(node.value);
                node = node.next;
            }
        }
        return collection;
    }

    @Override
    public void clear() {
        capacity = DEFAULT_CAPACITY;
        dataTable = new Node[capacity];
        size = 0;
    }

    private boolean checkKey(K key, V value) {
        Node<K, V> node = dataTable[getIndex(key)];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return false;
            }
            node = node.next;
        }
        return true;
    }

    private int getHash(K key) {
        return key != null ? key.hashCode() : 0;
    }

    private int getIndex(K key) {
        int hash = getHash(key);
        return (hash < 0 ? hash * -1 : hash) % capacity;
    }

    private void putNewNode(int hash, K key, V value, Node<K, V>[] data) {
        int index = getIndex(key);
        data[index] = new Node<>(hash, key, value,
                (data[index] == null ? null : data[index]));
    }

    private void resize() {
        capacity <<= 1;
        Node<K, V>[] newDataTable = new Node[capacity];
        for (Node<K, V> node: dataTable) {
            while (node != null) {
                putNewNode(node.hash, node.key, node.value, newDataTable);
                node = node.next;
            }
        }
        dataTable = newDataTable;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private final Node<K, V> next;

        private Node(int hash, K k, V v, Node<K, V> next) {
            this.hash = hash;
            key = k;
            value = v;
            this.next = next;
        }
    }
}
