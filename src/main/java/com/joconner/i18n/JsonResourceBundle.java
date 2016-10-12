package com.joconner.i18n;


import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.io.*;
import java.util.*;


/**
 * A JsonResourceBundle understands JSON-based resource files. In a JsonResourceBundle, each key must have a string
 * value or an object value. Implement a JsonResourceBundle by creating a text file containing key-value pairs.
 * The key-value pairs are part of a larger JSON object. Values can be either simple
 * strings or more complex objects. If values are object-values, those objects must have a "value" key that contains
 * a simple text value. The following example bundle contains 2 simple key-value pairs and 1 object value. Note that
 * key3's associated string is in the "value" field of the associated object:
 *
 * {
 *     "key1": "Hello world!",
 *     "key2": "Good afternoon!"
 *     "key3": {
 *         "value": "Good evening!",
 *         "meta-maxLength": 25,
 *         "meta-tone": "casual"
 *     }
 * }
 *
 * You use and create JsonResourceBundle objects by
 * 1) create a JSON resource file as described above
 * 2) create a ResourceBundle using ResourceBundle.getBundle and provide a JsonResourceControl
 *
 * @author joconner
 */
public class JsonResourceBundle extends ResourceBundle {

    private JsonObject jsonResource;

    private JsonResourceBundle() {

    }

    /**
     * Constructor will typically be called from a ResourceBundle.Control subclass, specifically the
     * JsonResourceControl.
     *
     * @param reader
     * @throws IOException
     */
    public JsonResourceBundle(Reader reader) throws IOException {
        JsonValue jsonValue = null;

        if (reader == null) {
            throw new NullPointerException("Reader is null.");
        }
        jsonValue = Json.parse(reader);
        if (jsonValue.isObject()) {
            jsonResource = (JsonObject) jsonValue;
        } else {
            throw new IOException("The requested file is not the correct format for json bundles.");
        }
    }

    @Override
    protected Object handleGetObject(String key) {
        if (key == null) {
            throw new NullPointerException("The key is null.");
        }
        JsonValue value = jsonResource.get(key);
        String strValue = null;
        if (value != null) {
            if (value.isString()) {
                strValue = value.asString();
            } else if (value.isObject()) {
                JsonObject valueObject = (JsonObject) value;
                strValue = valueObject.getString("value", null);
            }
        }
        return strValue;
    }

    @Override
    public Enumeration<String> getKeys() {
        Set<String> keySet = new HashSet<>();
        keySet.addAll(parent.keySet());
        if (jsonResource != null) {
            keySet.addAll(jsonResource.names());
        }

        return new Enumeration<String>() {
            Iterator<String> iterator = keySet.iterator();

            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            @Override
            public String nextElement() {
                return iterator.next();
            }
        };
    }

}
