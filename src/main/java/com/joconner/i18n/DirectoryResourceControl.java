package com.joconner.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.*;

import static com.sun.tools.javac.jvm.ByteCodes.ret;


/**
 * DirectoryResourceControl is a ResourceBundle controller that helps a ResourceBundle
 * factory find resources in subdirectories of the base resource file. For example, if
 * your baseName resource is com.example.res.Foo, then this controller will help a
 * ResourceBundle find translations in subpackages that are named for the target locale. For example,
 * resources for the fr-CA language will be in the following:
 * com.example.res.fr-ca.Foo
 * <p>
 * Resources for Mexican Spanish (es-MX) should be stored here:
 * com.example.res.es-mx.FOO
 * <p>
 * Resource subpackage names should be lowercase BCP-47 language tag identifiers.
 *
 * @author joconner
 */
public class DirectoryResourceControl extends ResourceBundle.Control {


    public DirectoryResourceControl() {

    }

    @Override
    public List<String> getFormats(String baseName) {
        return Arrays.asList("java.class", "java.properties", "joconner.json");
    }

    /**
     * Convert a baseName resource file of the form package-name.ResourceName to
     * package-name.locale-string.ResourceName. This controller separates localized resources into
     * their own subpackage and does extend the ResourceName.
     */
    @Override
    public String toBundleName(String baseName, Locale locale) {
        int nBasePackage = baseName.lastIndexOf(".");
        String basePackageName = nBasePackage > 0 ? baseName.substring(0, nBasePackage) : "";
        String resName = nBasePackage > 0 ? baseName.substring(nBasePackage + 1) : baseName;
        String langSubPackage = locale.equals(Locale.ROOT) ? "" : locale.toLanguageTag().toLowerCase();
        StringBuilder strBuilder = new StringBuilder();
        if (nBasePackage > 0) {
            strBuilder.append(basePackageName).append(".");
        }
        if (langSubPackage.length() > 0) {
            strBuilder.append(langSubPackage).append(".");
        }
        strBuilder.append(resName);
        return strBuilder.toString();
    }

    @Override
    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
            throws IllegalAccessException, InstantiationException, IOException {
        ResourceBundle bundle = null;
        if (format.equals("joconner.json")) {
            String bundleName = toBundleName(baseName, locale);
            final String resourceName = toResourceName(bundleName, "json");
            if (resourceName == null) {
                return bundle;
            }
            final ClassLoader classLoader = loader;
            final boolean reloadFlag = reload;
            InputStream stream = null;
            try {
                stream = AccessController.doPrivileged(
                        new PrivilegedExceptionAction<InputStream>() {
                            public InputStream run() throws IOException {
                                InputStream is = null;
                                if (reloadFlag) {
                                    URL url = classLoader.getResource(resourceName);
                                    if (url != null) {
                                        URLConnection connection = url.openConnection();
                                        if (connection != null) {
                                            // Disable caches to get fresh data for
                                            // reloading.
                                            connection.setUseCaches(false);
                                            is = connection.getInputStream();
                                        }
                                    }
                                } else {
                                    is = classLoader.getResourceAsStream(resourceName);
                                }
                                return is;
                            }
                        });
            } catch (PrivilegedActionException e) {
                throw (IOException) e.getException();
            }
            if (stream != null) {
                try {
                    bundle = new JsonResourceBundle(stream);
                } finally {
                    stream.close();
                }
            }
        } else {
            bundle = super.newBundle(baseName, locale, format, loader, reload);
        }
        return bundle;
    }
}
