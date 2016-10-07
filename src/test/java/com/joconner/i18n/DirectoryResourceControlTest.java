package com.joconner.i18n;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.Assert.*;

/**
 * @author joconner
 */
public class DirectoryResourceControlTest {

    private DirectoryResourceControl control = new DirectoryResourceControl();

    @Test
    public void getFormats() throws Exception {
        List<String> supportedFormats = control.getFormats("foo");
        String [] expectedFormats = {"java.class", "java.properties", "joconner.json"};
        assertArrayEquals(expectedFormats, supportedFormats.toArray());
    }




    @Test
    public void toBundleName() throws Exception {
        Locale[] locales = {Locale.CHINA, Locale.forLanguageTag("en-US-Windows")};
        String[] baseNames = {"Message", "foo.Message", "foo.bar.Message"};
        String [] expected = {"zh-cn.Message", "foo.zh-cn.Message", "foo.bar.zh-cn.Message",
                              "en-us-windows.Message", "foo.en-us-windows.Message", "foo.bar.en-us-windows.Message"};
        int x = 0;
        for (Locale locale: locales) {
        for (String baseName : baseNames) {
                String bundleName = control.toBundleName(baseName, locale);
                assertEquals(expected[x++], bundleName);
            }
        }
    }

    @Test
    public void toResourceName() throws Exception {
        String [] bundleNames = {"Message", "zh-cn.Message", "foo.zh-cn.Message", "foo.bar.zh-cn.Message",
                "en-us-windows.Message", "foo.en-us-windows.Message", "foo.bar.en-us-windows.Message"};

        String [] expected = {"Message.properties", "zh-cn/Message.properties", "foo/zh-cn/Message.properties", "foo/bar/zh-cn/Message.properties",
                "en-us-windows/Message.properties", "foo/en-us-windows/Message.properties", "foo/bar/en-us-windows/Message.properties"};

        int x = 0;

        for(String bundleName: bundleNames) {
            String actualResourceName = control.toResourceName(bundleName,"properties");
            assertEquals(expected[x++], actualResourceName);
        }

    }

    @Test
    public void getBundleForen_ca() throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("com.joconner.i18n.res.Test", Locale.CANADA, new DirectoryResourceControl());
        String hello = bundle.getString("GREETING_MORNING");
        assertEquals("It's morning, hoser!", hello);
        String goodNight = bundle.getString("GREETING_NIGHT");
        assertEquals("Good evening!", goodNight);

    }

    @Test
    public void getBundleForen() throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("com.joconner.i18n.res.Test", Locale.ENGLISH, new DirectoryResourceControl());
        String goodNight = bundle.getString("GREETING_NIGHT");
        assertEquals("Good evening!", goodNight);
        String hello = bundle.getString("GREETING_MORNING");
        assertEquals("Good morning!", hello);
    }

    @Test
    public void newBundleProvidesNonJsonResourceBundle() throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("com.joconner.i18n.res.Test", Locale.ENGLISH, new DirectoryResourceControl());
        assertFalse(bundle instanceof JsonResourceBundle);
    }

    @Test
    public void newBundleProvidesJsonResourceBundle() throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("com.joconner.i18n.res.Test", Locale.JAPANESE, new DirectoryResourceControl());
        assertTrue(bundle instanceof JsonResourceBundle);
    }

}