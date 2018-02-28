package org.whstsa.library.gui.components;

import javafx.scene.Node;

public interface Element {
    Node getComputedElement();

    String getID();

    void setID(String id);

    Object getResult();

    String getString();

    boolean getBoolean();
}
