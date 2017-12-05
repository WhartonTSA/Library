package org.whstsa.library.gui.scenes;

import java.io.File;

import javafx.stage.FileChooser;
import org.whstsa.library.LibraryDB;

public class IOFileSelection {
	
	private static final String[] DEFAULT_FILETYPES = new String[0];
	
	private FileChooser.ExtensionFilter filter;
	private LibraryDB libraryDB;
	
	private final FileChooser dialog = new FileChooser();
	
	public IOFileSelection(LibraryDB libraryDB, String ...acceptedFiletypes) {
		this.filter = new FileChooser.ExtensionFilter("JSON pls", acceptedFiletypes);
		this.libraryDB = libraryDB;
		dialog.setSelectedExtensionFilter(this.filter);
	}
	
	public IOFileSelection(LibraryDB libraryDB) {
		this(libraryDB, DEFAULT_FILETYPES);
	}
	
	public File getFile() {
		return dialog.showOpenDialog(this.libraryDB.getStage());
	}
	
}
