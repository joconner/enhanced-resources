package com.joconner.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by joconner on 2/21/17.
 */
public class PackageBasedResourceControl extends ResourceBundle.Control {

    protected boolean isPackageBased;

    public PackageBasedResourceControl() {}

    /**
     * Creates a new ResourceBundle.Control that uses localized bundle files in
     * subdirectories of the root package if isPackageBased is true,
     * otherwise uses the Java platform's default organization of bundles.
     * @param isPackageBased, true if you want subdirectory/subpackage-based
     *                        resource bundle files, false otherwise
     */

    public PackageBasedResourceControl(boolean isPackageBased) {
        this.isPackageBased = isPackageBased;
    }

    /**
     * If this is a package-based control, converts a baseName resource file of
     * the form package-name.ResourceName to
     * package-name.locale-string.ResourceName. This controller separates
     * localized resources into their own subpackage and does extend the
     * ResourceName.
     *
     * If this is not a package-based control, converts a baseName resource
     * file to standard bundle names as provided by the default Java
     * ResourceBundle.Control class.
     *
     */
    @Override
    public String toBundleName(String baseName, Locale locale) {
        String bundleName = null;
        if (isPackageBased) {

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

}
