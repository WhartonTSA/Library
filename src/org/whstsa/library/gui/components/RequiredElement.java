package org.whstsa.library.gui.components;

import java.util.function.Consumer;

public interface RequiredElement extends Element {
    boolean isRequired();
    boolean isSatisfied();
    void setOnSatisfactionUpdate(Consumer<Boolean> onSatisfactionUpdate);
    void setRequired(boolean required);
}
