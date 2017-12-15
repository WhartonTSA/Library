package org.whstsa.library.gui.components;

import javafx.scene.Node;

public interface Element {
    Node getComputedElement();
    String getID();
    Object getResult();
    String getString();
    boolean getBoolean();
    void setID(String id);
}
