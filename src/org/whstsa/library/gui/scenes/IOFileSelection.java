package org.whstsa.library.gui.scenes;

import javafx.stage.FileChooser;
import org.whstsa.library.LibraryDB;

import java.io.File;

public class IOFileSelection {

    private static final String[] DEFAULT_FILETYPES = new String[0];
    private final FileChooser dialog = new FileChooser();
    private FileChooser.ExtensionFilter filter;
    private LibraryDB libraryDB;

    public IOFileSelection(LibraryDB libraryDB, String... acceptedFiletypes) {
        this.filter = new FileChooser.ExtensionFilter("JSON files", acceptedFiletypes);
        this.libraryDB = libraryDB;
        dialog.setSelectedExtensionFilter(this.filter);
        this.dialog.setTitle("Open a JSON data file");
        this.dialog.setInitialDirectory(new File(libraryDB.getConfig().getProperty("initialDirectory")));
    }

    public IOFileSelection(LibraryDB libraryDB) {
        this(libraryDB, DEFAULT_FILETYPES);
    }

    public File getFile() {
        try {
            return dialog.showOpenDialog(this.libraryDB.getStage());
        } catch (Exception ex) {
            LibraryDB.LOGGER.debug("Directory " + libraryDB.getConfig().getProperty("initialDirectory") + " couldn't be found, switching to home.");
            this.dialog.setInitialDirectory(new File(System.getProperty("user.home")));
            libraryDB.getConfig().setProperty("initialDirectory", System.getProperty("user.home"));
            return getFile();
        }
    }

}
