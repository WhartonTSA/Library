package org.whstsa.library.api;

import java.util.List;

public interface Operator<K, T, V> {
    V mutate(K controller, T data);
}
