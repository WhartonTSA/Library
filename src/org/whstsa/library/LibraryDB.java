package org.whstsa.library;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;
import org.whstsa.library.api.Callback;
import org.whstsa.library.db.IOFileDelegate;
import org.whstsa.library.db.Loader;
import org.whstsa.library.gui.InterfaceManager;
import org.whstsa.library.gui.api.GuiMain;
import org.whstsa.library.gui.factories.DialogUtils;
import org.whstsa.library.gui.scenes.IOFileSelection;
import org.whstsa.library.util.CommandWatcher;
import org.whstsa.library.util.Logger;
import org.whstsa.library.util.Readline;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Scanner;

/**
 * Created by eric on 11/19/17.
 */
public class LibraryDB extends Application {

    private Stage stage;
    private InterfaceManager interfaceManager;
    private IOFileSelection jsonFileBrowser;

    public void start(Stage stage) {
        this.stage = stage;
        stage.getIcons().add(new Image("file:LibraryManagerIcon.png"));
        this.stage.setResizable(true);
        this.interfaceManager = new InterfaceManager(this);
        this.jsonFileBrowser = new IOFileSelection(this, "json");
        if (TESTING) {
            try {
                new Tester();
            } catch (Exception e) {
            }
        }
        this.loadJSON((arg0) -> {
            LOGGER.debug("JSON Loaded.");
            this.interfaceManager.display(new GuiMain(this));
        });
        //new CommandWatcher(System.in, System.out).start();
    }

    public Stage getStage() {
        return this.stage;
    }

    public InterfaceManager getInterfaceManager() {
        return this.interfaceManager;
    }

    private void loadJSON(Callback<Object> callback) {
        LOGGER.debug("Splashing JSON GUI");
        File rawJSON = this.jsonFileBrowser.getFile();
        try {
            FILE_DELEGATE = new IOFileDelegate(rawJSON);
            JSONObject root = FILE_DELEGATE.parse();
            Loader.getLoader().load(root);
        } catch (UncheckedIOException | IOException | NullPointerException | JSONException ex) {
            Alert alert = DialogUtils.createDialog("Invalid file", "You have provided an invalid file. Please check that you chose the correct file, or try a new database.", null, Alert.AlertType.ERROR, (Callback<Boolean> arg1) -> {
                arg1.callback(true);
                LOGGER.debug("Prompting for new JSON GUI");
                this.loadJSON(callback);
            });
            alert.show();
            return;
        }
        callback.callback(null);
    }

    public static final Scanner SCANNER = new Scanner(System.in);
    public static final Readline READER = new Readline(System.in, System.out);

    public static final boolean TESTING = false;

    public static final Logger LOGGER = new Logger();

    private static IOFileDelegate FILE_DELEGATE;

    public static IOFileDelegate getFileDelegate() {
        return FILE_DELEGATE;
    }

    private static JSONObject getJSONCommandLine() throws IOException, JSONException {
        String path = READER.question("What is the path to the JSON file? (can be relative)");
        boolean relative = READER.getBoolean("Is this file relative?");
        FILE_DELEGATE = new IOFileDelegate(path, relative);
        return FILE_DELEGATE.parse();
    }

}