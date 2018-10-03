package io.chino.api.common;

/**
 * A simple class that represents a key-value pair
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 */
public class Pair<K, V> {

    private K theKey;
    private V theValue;

    public Pair(K key, V value) {
        theKey = key;
        theValue = value;
    }

    public K getKey() {
        return theKey;
    }

    public void setKey(K key) {
        this.theKey = key;
    }

    public V getValue() {
        return theValue;
    }

    public void setValue(V value) {
        this.theValue = value;
    }
}
