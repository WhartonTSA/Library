package org.whstsa.library.gui.components;

import java.util.function.Consumer;

public interface RequiredElement extends Element {
    boolean isRequired();

    void setRequired(boolean required);

    boolean isSatisfied();

    void setOnSatisfactionUpdate(Consumer<Boolean> onSatisfactionUpdate);
}
