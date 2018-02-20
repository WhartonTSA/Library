package org.whstsa.library.api;

public interface Operator<K, T, V> {
    V mutate(K controller, T data);
}
