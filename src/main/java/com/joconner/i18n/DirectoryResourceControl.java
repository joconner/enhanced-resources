package com.joconner.i18n;

import java.util.Locale;
import java.util.ResourceBundle;


/**
 * DirectoryResourceControl is a ResourceBundle controller that helps a ResourceBundle
 * factory find resources in subdirectories of the base resource file. For example, if
 * your baseName resource is com.example.res.Foo, then this controller will help a
 * ResourceBundle find translations in subpackages that are named for the target locale. For example,
 * resources for the fr-CA language will be in the following:
 *     com.example.res.fr-ca.Foo
 *
 * Resources for Mexican Spanish (es-MX) should be stored here:
 *     com.example.res.es-mx.FOO
 *
 * Resource subpackage names should be lowercase BCP-47 language tag identifiers.
 *
 * @author joconner
 */
public class DirectoryResourceControl extends ResourceBundle.Control {


    public DirectoryResourceControl() {

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
        String resName = nBasePackage > 0 ? baseName.substring(nBasePackage+1) : baseName;
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

}
