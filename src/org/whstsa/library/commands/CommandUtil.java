package org.whstsa.library.commands;

import org.json.JSONObject;

/**
 * Created by eric on 11/19/17.
 */
public class CommandUtil {

    public static JSONObject createErrorResponse(String message) {
        JSONObject response = new JSONObject();
        response.put("error", message);
        return response;
    }

}
