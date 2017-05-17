package com.joconner.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;


/**
 * JsonResourceBundleControl is a ResourceBundle controller that helps a ResourceBundle.getBundle
 * factory find resources in JSON files. Optionally, the control can be configured to also use
 * package-based organization for resource bundles.
 * <p>For example, if your baseName resource is com.example.res.Foo, then this controller will help a
 * ResourceBundle find translations in subpackages that are named for the target locale. For example,
 * resources for the fr-CA language will be in the following:
 *
 * com.example.res.fr-ca.Foo
 *
 * Resources for Mexican Spanish (es-MX) should be stored here:
 * com.example.res.es-mx.FOO
 *
 * Resource subpackage names should be lowercase BCP-47 language tag identifiers.
 *
 * @author joconner
 */
public class JsonResourceBundleControl extends PackageableResourceControl {
    private static final String JSON_SUFFIX = "json";
    private static final List<String> FORMAT_JSON = Arrays.asList(JSON_SUFFIX);
    private static final List<String> supportedFormats;

    static {
        supportedFormats = new ArrayList<>(ResourceBundle.Control.FORMAT_DEFAULT);
        supportedFormats.add(JSON_SUFFIX);
    }

    /**
     * Creates a new JsonResourceBundleControl that reads localized bundle files
     * from JSON files instead of property files.
     */
    public JsonResourceBundleControl() {
    }

    /**
     * Creates a new JsonResourceBundleControl that uses localized bundle files in
     * subdirectories of the root package if isPackageBased is true,
     * otherwise uses the Java platform's default organization of bundles.
     * @param isPackageBased, true if you want subdirectory/subpackage-based
     *                        resource bundle files, false otherwise
     */
    public JsonResourceBundleControl(boolean isPackageBased) {
        super(isPackageBased);
    }

    /**
     * Returns the list of bundle file formats supported by
     * JsonResourceBundle. JsonResourceBundle supports bundle formats in the
     * following order "java.class", "java.properties", and "json".
     *
     * @param baseName, the basename of the resource bundle.
     * @return a string list of supported formats
     */
    @Override
    public List<String> getFormats(String baseName) {
        return supportedFormats;
    }

    /**
     * See the Java platforms default description of this method.
     * http://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.Control.html
     * @param baseName
     * @param locale
     * @param format
     * @param loader
     * @param reload
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws IOException
     */
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
                is = reload(resourceName, classLoader);
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

    InputStream reload(String resourceName, ClassLoader classLoader) throws IOException {
        InputStream stream = null;
        URL url = classLoader.getResource(resourceName);
        if (url != null) {
            URLConnection connection = url.openConnection();
            if (connection != null) {
                // Disable caches to get fresh data for
                // reloading.
                connection.setUseCaches(false);
                stream = connection.getInputStream();
            }
        }
        return stream;
    }


}
