package com.joconner.i18n;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.Assert.*;

/**
 * @author joconner
 */
public class JsonResourceControlTest {

    private JsonResourceControl control = new JsonResourceControl();

    @Test
    public void getFormats() throws Exception {
        List<String> supportedFormats = control.getFormats("foo");
        List<String> expectedFormats = Arrays.asList("java.class", "java.properties", "json");
        assertEquals(expectedFormats, supportedFormats);
    }

    @Test
    public void toBundleName() throws Exception {
        Locale[] locales = {Locale.CHINA, Locale.forLanguageTag("en-US-Windows")};
        String[] baseNames = {"Message", "foo.Message", "foo.bar.Message"};
        String[] expected = {"zh-cn.Message", "foo.zh-cn.Message", "foo.bar.zh-cn.Message",
                "en-us-windows.Message", "foo.en-us-windows.Message", "foo.bar.en-us-windows.Message"};
        int x = 0;
        for (Locale locale : locales) {
            for (String baseName : baseNames) {
                String bundleName = control.toBundleName(baseName, locale);
                assertEquals(expected[x++], bundleName);
            }
        }
    }

    @Test
    public void toNonPackageBasedBundleName() throws Exception {
        JsonResourceControl control = new JsonResourceControl(false);
        Locale[] locales = {Locale.CHINA, Locale.forLanguageTag("en-US-Windows")};
        String[] baseNames = {"Message", "foo.Message", "foo.bar.Message"};
        String[] expected = {"Message_zh_CN", "foo.Message_zh_CN", "foo.bar.Message_zh_CN",
                "Message_en_US_Windows", "foo.Message_en_US_Windows", "foo.bar.Message_en_US_Windows"};
        int x = 0;
        for (Locale locale : locales) {
            for (String baseName : baseNames) {
                String bundleName = control.toBundleName(baseName, locale);
                assertEquals(expected[x++], bundleName);
            }
        }
    }

    @Test
    public void toResourceName() throws Exception {
        String[] bundleNames = {"Message", "zh-cn.Message", "foo.zh-cn.Message", "foo.bar.zh-cn.Message",
                "en-us-windows.Message", "foo.en-us-windows.Message", "foo.bar.en-us-windows.Message"};
        String[] expected = {"Message.properties", "zh-cn/Message.properties", "foo/zh-cn/Message.properties", "foo/bar/zh-cn/Message.properties",
                "en-us-windows/Message.properties", "foo/en-us-windows/Message.properties", "foo/bar/en-us-windows/Message.properties"};

        int x = 0;
        for (String bundleName : bundleNames) {
            String actualResourceName = control.toResourceName(bundleName, "properties");
            assertEquals(expected[x++], actualResourceName);
        }
    }

    @Test
    public void toNonPackagedResourceName() throws Exception {
        JsonResourceControl control = new JsonResourceControl(false);
        String[] bundleNames = {"Message", "Message_zh_CN", "foo.Message_zh_CN", "foo.bar.Message_zh_CN",
                "Message_en_US-Windows", "foo.Message_en_US_Windows", "foo.bar.Message_en_US_Windows"};
        String[] expected = {"Message.properties", "Message_zh_CN.properties", "foo/Message_zh_CN.properties", "foo/bar/Message_zh_CN.properties",
                "Message_en_US-Windows.properties", "foo/Message_en_US_Windows.properties", "foo/bar/Message_en_US_Windows.properties"};

        int x = 0;
        for (String bundleName : bundleNames) {
            String actualResourceName = control.toResourceName(bundleName, "properties");
            assertEquals(expected[x++], actualResourceName);
        }
    }

    @Test
    public void getBundleForen_ca() throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("com.joconner.i18n.res.Test", Locale.CANADA, control);
        String hello = bundle.getString("GREETING_MORNING");
        assertEquals("It's morning, hoser!", hello);
        String goodNight = bundle.getString("GREETING_NIGHT");
        assertEquals("Good evening!", goodNight);

    }

    @Test
    public void getBundleForen() throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("com.joconner.i18n.res.Test", Locale.ENGLISH, new JsonResourceControl());
        String goodNight = bundle.getString("GREETING_NIGHT");
        assertEquals("Good evening!", goodNight);
        String hello = bundle.getString("GREETING_MORNING");
        assertEquals("Good morning!", hello);
    }

    @Test
    public void newBundleProvidesNonJsonResourceBundle() throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("com.joconner.i18n.res.Test", Locale.ENGLISH, new JsonResourceControl());
        assertFalse(bundle instanceof JsonResourceBundle);
    }

    @Test
    public void newBundleProvidesJsonResourceBundle() throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("com.joconner.i18n.res.Test", Locale.JAPANESE, new JsonResourceControl());
        assertTrue(bundle instanceof JsonResourceBundle);
    }

}