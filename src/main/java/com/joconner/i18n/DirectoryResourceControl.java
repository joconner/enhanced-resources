package com.joconner.i18n;

import java.util.Locale;
import java.util.ResourceBundle;


/**
 * @author joconner
 */
public class DirectoryResourceControl extends ResourceBundle.Control {


    public DirectoryResourceControl() {

    }


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
