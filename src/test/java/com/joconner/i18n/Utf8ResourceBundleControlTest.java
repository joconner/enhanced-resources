package com.joconner.i18n;


import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by joconner on 1/11/17.
 */
public class Utf8ResourceBundleControlTest {

    @Test
    public void testDefaultResources() {
        ResourceBundle.Control utf8Control = new Utf8ResourceBundleControl();
        ResourceBundle bundle =
                ResourceBundle.getBundle("com.joconner.i18n.res.Utf8Resources", Locale.ROOT, utf8Control);
        String hello = bundle.getString("HELLO");
        assertEquals("Héllø, wórld!", hello);
    }

    @Test
    public void testJapaneseResources() {
        ResourceBundle.Control utf8Control = new Utf8ResourceBundleControl();
        ResourceBundle bundle =
                ResourceBundle.getBundle("com.joconner.i18n.res.Utf8Resources", Locale.JAPANESE, utf8Control);
        String hello = bundle.getString("HELLO");
        assertEquals("こんにちは！", hello);

    }

    @Test
    public void testPackagedResource() {
        ResourceBundle.Control utf8Control = new Utf8ResourceBundleControl(true);
        ResourceBundle bundle =
                ResourceBundle.getBundle("com.joconner.i18n.res.Utf8Resources", Locale.JAPANESE, utf8Control);
        String hello = bundle.getString("GREETING_NIGHT");
        assertEquals("こんばんは！", hello);
    }

}
