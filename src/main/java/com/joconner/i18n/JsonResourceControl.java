package com.joconner.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;


/**
 * JsonResourceControl is a ResourceBundle controller that helps a ResourceBundle.getBundle
 * factory find resources in sub-packages of the base resource file. For example, if
 * your baseName resource is com.example.res.Foo, then this controller will help a
 * ResourceBundle find translations in subpackages that are named for the target locale. For example,
 * resources for the fr-CA language will be in the following:
 *
 * com.example.res.fr-ca.Foo
 *
 * Resources for Mexican Spanish (es-MX) should be stored here:
 * com.example.res.es-mx.FOO
 *
 *  Resource subpackage names should be lowercase BCP-47 language tag identifiers.
 *
 * @author joconner
 */
public class JsonResourceControl extends ResourceBundle.Control {
    private static final String JSON_SUFFIX = "json";
    private static final List<String> FORMAT_JSON = Arrays.asList(JSON_SUFFIX);
    private static final List<String> supportedFormats;

    static {
        supportedFormats = new ArrayList<>(ResourceBundle.Control.FORMAT_DEFAULT);
        supportedFormats.add(JSON_SUFFIX);
    }

    private boolean packageBased;


    public JsonResourceControl() {
        packageBased = true;
    }

    public JsonResourceControl(boolean isPackageBased) {
        this.packageBased = isPackageBased;
    }

    @Override
    public List<String> getFormats(String baseName) {
        return supportedFormats;
    }

    /**
     * Convert a baseName resource file of the form package-name.ResourceName to
     * package-name.locale-string.ResourceName. This controller separates localized resources into
     * their own subpackage and does extend the ResourceName.
     */
    @Override
    public String toBundleName(String baseName, Locale locale) {
        String bundleName = null;
        if(packageBased) {

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
            bundleName = strBuilder.toString();
        } else {
            bundleName = super.toBundleName(baseName, locale);
        }
        return bundleName;
    }

    @Override
    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
            throws IllegalAccessException, InstantiationException, IOException {

        ResourceBundle bundle = null;
        if (format.equals(JSON_SUFFIX)) {
            String bundleName = toBundleName(baseName, locale);
            final String resourceName = toResourceName(bundleName, JSON_SUFFIX);
            if (resourceName == null) {
                return bundle;
            }
            final ClassLoader classLoader = loader;
            final boolean reloadFlag = reload;
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

            InputStreamReader reader = new InputStreamReader(is, "UTF-8");
            if (reader != null) {
                try {
                    bundle = new JsonResourceBundle(reader);
                } finally {
                    reader.close();
                }
            }
        } else {
            bundle = super.newBundle(baseName, locale, format, loader, reload);
        }
        return bundle;
    }
}
