package org.whstsa.library.gui.components;

import javafx.scene.Node;

public interface Element {
    Node getComputedElement();
    String getID();
    Object getResult();
    void setID(String id);
}
