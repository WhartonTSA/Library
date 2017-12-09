package org.whstsa.library.gui;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.whstsa.library.LibraryDB;
import org.whstsa.library.gui.api.Gui;
import org.whstsa.library.gui.api.GuiJXML;//TODO
import org.whstsa.library.gui.api.GuiMain;
import org.whstsa.library.util.Logger;

public class InterfaceManager {
	
	private LibraryDB libraryDB;
	
	private Scene oldScene;
	private GuiJXML oldGui;
	private GuiJXML currentGui;
	
	private Map<String, Scene> sceneCache = new HashMap<>();
	
	private static final Logger LOGGER = new Logger(LibraryDB.LOGGER, "GUIMGR");
	
	public InterfaceManager(LibraryDB libraryDB) {
		this.libraryDB = libraryDB;
	}
	
	public Stage getStage() {
		return this.libraryDB.getStage();
	}
	
	private void displayScene(Scene scene) {
		this.oldScene = this.getCurrentScene();
		this.getStage().setScene(scene);
	}
	
	/**
	 * Sets the new scene
	 * 
	 * @param gui The scene to set the stage with
	 * @return The old scene, or null if there is no old scene.
	 */
	public Scene display(GuiJXML gui) {//TODO
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
		this.getStage().setResizable(false);
		this.getStage().centerOnScreen();
		this.getStage().show();
	}

	public LibraryDB getTardyDB() {
		return this.libraryDB;
	}
}
