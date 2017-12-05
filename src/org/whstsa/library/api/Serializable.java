package org.whstsa.library.api;

import org.json.JSONObject;

public interface Serializable {
    /**
     * Serializes this object to JSON
     *
     * @return the serialized object
     */
    JSONObject toJSON();
}
