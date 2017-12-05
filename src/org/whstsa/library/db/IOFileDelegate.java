package org.whstsa.library.db;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by eric on 11/19/17.
 */
public class IOFileDelegate {

    private File file;

    public IOFileDelegate(File file) {
        this.file = file;
    }

    public IOFileDelegate(String path, boolean relative) {
        this(new File(buildPath(path, relative).toUri()));
    }

    public IOFileDelegate(String path) {
        this(path, false);
    }

    private static Path buildPath(String path, boolean relative) {
        if (!relative) {
            return Paths.get(path);
        }
        URL jarContainer = IOFileDelegate.class.getProtectionDomain().getCodeSource().getLocation();
        try {
            URL resolvedURL = new URL(jarContainer, path);
            return Paths.get(resolvedURL.toURI());
        } catch (MalformedURLException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    public JSONObject parse() throws IOException, JSONException {
        return new JSONObject(this.getText());
    }

    public String getText() throws IOException {
        StringBuilder builder = new StringBuilder();
        this.getLines().forEach(builder::append);
        return builder.toString();
    }

    public Stream<String> getLines() throws IOException {
        try {
            return Files.lines(this.file.toPath());
        } catch (NoSuchFileException ex) {
            JSONObject skeleton = new JSONObject();
            JSONObject childSkeleton = new JSONObject();
            skeleton.put("books", childSkeleton);
            skeleton.put("libraries", childSkeleton);
            skeleton.put("people", childSkeleton);
            this.save(skeleton);
            return Arrays.asList(skeleton.toString(4).split("\n")).stream();
        }
    }

    public void save(JSONObject object) throws IOException {
        this.save(object.toString(4));
    }

    public void save(String text) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(this.file, false));
        writer.write(text);
        writer.close();
    }

}
