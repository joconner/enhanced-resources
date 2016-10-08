package com.joconner.i18n;



import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;

import static com.sun.tools.doclint.Entity.ge;
import static javafx.scene.input.KeyCode.M;


/**
 * Created by joconner on 10/4/16.
 */
public class JsonResourceBundle extends ResourceBundle {

    private JsonObject jsonObject;


    private JsonResourceBundle() {

    }

    JsonResourceBundle(InputStream stream) {
        JsonValue jsonValue = null;

        try (Reader jsonBundleReader = new InputStreamReader(stream, "UTF-8")) {
            jsonValue = Json.parse(jsonBundleReader);
            if(jsonValue.isObject()) {
                jsonObject = (JsonObject)jsonValue;
            } else {
                throw new IOException("The requested file is not the correct format for joconner.json bundles.");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected Object handleGetObject(String key) {
        JsonValue value = jsonObject.get(key);
        String strValue = null;
        if (value == null) {
            strValue = null;
        } else if (value.isString()) {
            strValue = value.asString();
        } else if (value.isObject()) {
            JsonObject valueObject = (JsonObject) value;
            strValue = valueObject.getString("value", null);
        }
        return strValue;
    }

    @Override
    public Enumeration<String> getKeys() {
        Set<String> keySet = new HashSet<>();
        keySet.addAll(parent.keySet());
        keySet.addAll(jsonObject.names());

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
