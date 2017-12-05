package org.whstsa.library.api;

/**
 * Created by eric on 11/18/17.
 */
public interface Loadable {
    /**
     * Registers this object with the Loader so it can be accessed by other
     * objects that do not have this object in scope. It is imperative that
     * this method is called whenever a Loadable object is constructed and
     * initialized to prevent errors from occurring.
     */
    void load();
}
