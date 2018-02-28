package org.whstsa.library.api;

public interface ComputedProperty<T, P> {
    T get(P value);
}
