package org.whstsa.library.gui;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.whstsa.library.LibraryDB;
import org.whstsa.library.gui.api.Gui;
import org.whstsa.library.util.Logger;

import java.util.HashMap;
import java.util.Map;

public class InterfaceManager {

    private static final Logger LOGGER = new Logger(LibraryDB.LOGGER, "GUIMGR");
    private LibraryDB libraryDB;
    private Scene oldScene;
    private Gui oldGui;
    private Gui currentGui;
    private Map<String, Scene> sceneCache = new HashMap<>();

    public InterfaceManager(LibraryDB libraryDB) {
        this.libraryDB = libraryDB;
    }

    public Stage getStage() {
        return this.libraryDB.getStage();
    }

    private void displayScene(Scene scene) {
        this.displayScene(scene, 0);
    }

    private void displayScene(Scene scene, int tries) {
        if (tries > 5) {
            Logger.DEFAULT_LOGGER.error("Failed to display scene!");
            return;
        }
        try {
            this.oldScene = this.getCurrentScene();
            this.getStage().setScene(scene);
        } catch (IllegalStateException ex) {
            Logger.DEFAULT_LOGGER.warn("Couldn't set the scene");
            ex.printStackTrace();
            this.displayScene(scene, tries++);
        }
    }

    /**
     * Sets the new scene
     *
     * @param gui The scene to set the stage with
     * @return The old scene, or null if there is no old scene.
     */
    public Scene display(Gui gui) {
        final String guiName = gui.getClass().getSimpleName() + " (" + gui.getUUID() + ")";
        LOGGER.debug("Switching to " + guiName);
        Scene scene;
        if (this.sceneCache.containsKey(gui.getUUID())) {
            LOGGER.debug("Using cached " + guiName);
            scene = this.sceneCache.get(gui.getUUID());
        } else {
            LOGGER.debug("Using fresh " + guiName);
            scene = gui.draw();
            this.sceneCache.put(gui.getUUID(), scene);
        }
        this.oldGui = this.currentGui;
        this.currentGui = gui;
        this.displayScene(scene);
        this.show();
        return oldScene;
    }

    public void showPreviousGUI() {
        this.display(this.oldGui);
    }

    public Scene getOldScene() {
        return this.oldScene;
    }

    public Gui getOldGUI() {
        return this.oldGui;
    }

    public Gui getCurrentGUI() {
        return this.currentGui;
    }

    public Scene getCurrentScene() {
        return this.getStage().getScene();
    }

    public void show() {
        if (this.currentGui.getUUID().contains("GUI_LIBRARY_MANAGER")) {
            this.getStage().setResizable(true);
        } else {
            this.getStage().setResizable(false);
        }
        this.getStage().centerOnScreen();
        this.getStage().show();
    }

    public LibraryDB getTardyDB() {
        return this.libraryDB;
    }
}
