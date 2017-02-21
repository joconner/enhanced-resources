package com.joconner.i18n;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * @author joconner
 */
public class JsonResourceBundleTest {

    @Test(expected = NullPointerException.class)
    public void handleGetObjectWithNullKey() throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("com.joconner.i18n.res.Test", Locale.JAPANESE, new JsonResourceBundleControl());
        JsonResourceBundle jsonBundle = null;
        if (bundle instanceof JsonResourceBundle) {
            jsonBundle = (JsonResourceBundle) bundle;
            jsonBundle.handleGetObject(null);
        }
    }

    @Test
    public void handleGetObject() throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("com.joconner.i18n.res.Test", Locale.JAPANESE, new JsonResourceBundleControl());
        assertTrue(bundle instanceof JsonResourceBundle);

        JsonResourceBundle jsonBundle = (JsonResourceBundle) bundle;
        String greeting = (String) jsonBundle.handleGetObject("GREETING_MORNING");
        assertEquals("おはようございます！", greeting);

        greeting = (String) jsonBundle.handleGetObject("GREETINGNOON");
        assertNull(greeting);
    }


    @Test
    public void getKeys() throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("com.joconner.i18n.res.Test", Locale.JAPANESE, new JsonResourceBundleControl());
        assertTrue(bundle instanceof JsonResourceBundle);
        JsonResourceBundle jsonBundle = (JsonResourceBundle) bundle;

        Enumeration<String> keys = jsonBundle.getKeys();
        assertNotNull(keys);
        List<String> keyList = new ArrayList<>();
        while (keys.hasMoreElements()) {
            keyList.add(keys.nextElement());
        }
        assertEquals(3, keyList.size());
    }

    @Test
    public void getString() throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("com.joconner.i18n.res.Test", Locale.JAPANESE, new JsonResourceBundleControl());
        assertNotNull(bundle);
        String greeting = bundle.getString("GREETING_MORNING");
        assertEquals("おはようございます！", greeting);
        greeting = bundle.getString("GREETING_NOON");
        assertEquals("Good afternoon!", greeting);
        greeting = bundle.getString("GREETING_NIGHT");
        assertEquals("こんばんは！", greeting);

    }


}