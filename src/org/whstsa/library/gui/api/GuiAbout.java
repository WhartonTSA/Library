package org.whstsa.library.gui.api;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.whstsa.library.gui.components.LabelElement;
import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.gui.text.AboutText;

public class GuiAbout implements Gui {
    @Override
    public Scene draw() {

        LabelElement title = GuiUtils.createTitle("About");

        Text content = AboutText.getText();
        TextFlow mainTextFlow = new TextFlow(content);
        mainTextFlow.setMaxWidth(800);

        VBox container = GuiUtils.createVBox(15, title, mainTextFlow);
        container.setSpacing(10);

        return new Scene(container, 800, 512);
    }

    @Override
    public String getUUID() {
        return "GUI_ABOUT";
    }
}
