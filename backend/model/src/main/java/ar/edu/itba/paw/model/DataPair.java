package ar.edu.itba.paw.model;
import java.util.Objects;

public class DataPair<K,V> {

    private final K key;
    private final V value;

    public DataPair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public boolean equals(Object o) {
            return o instanceof DataPair && Objects.equals(key, ((DataPair<?,?>)o).key) && Objects.equals(value, ((DataPair<?,?>)o).value);
        }

        public int hashCode() {
            return 31 * Objects.hashCode(key) + Objects.hashCode(value);
        }

        public String toString() {
            return key + "=" + value;
        }

}
