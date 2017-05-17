# Organize Resources by Packages

![Shipping containers](images/shipping-containers.jpg)

 
The Java `ResourceControl` class gives you an opportunity to modify the default lookup strategy for resource bundles. By creating your own controller, you can organize resources differently from their default. This article shows you how to organize translated resources into subpackages by locale using a custom `ResourceControl`. Package-based resources are significantly more easy to view, understand, and manage.
 
 
## The Default Strategy is Extension-Based
 
The default lookup strategy for resources uses a locale extension on the base filename. For example, a default resource file named `common.properties` will become `common_es.properties` for Spanish and `common_de.properties` for German. Using this strategy, all your resources will occupy a single folder or package location in your project:
 
 ```text
common.properties
common_de.properties
common_es_mx.properties
common_fr_ca.properties
foo.properties
foo_es_mx.properties
foo_fr_ca.properties
bar.properties
 ```
 
As you continue to add resource files and localizations to this single location, the directory becomes convoluted and difficult to navigate. Neither the target locales nor the exact files included in a single localization are obvious at a glance. This isn't ideal, and we can do better.

## Package-Based Resources Provide Organization

By putting your localized resources into folders or sub-packages, you improve resource organization. You can, at a glance, understand what localizations exist. You don't have to rename files. Instead, you can use the same filenames in target localizations as those used in the root resources. 

```text
|
|- common.properties
|- foo.properties
|- bar.properties
|
|-- fr-ca
|  |- common.properties
|  |- foo.properties
|  |- bar.properties
|
|-- es-mx
|  |- common.properties
|  |- foo.properties
|  |- bar.properties
```

Although the default `ResourceControl` does not search for localizations by locale-oriented packages, creating a subclass for this purpose is easy. Specifically, we need to override the `toBundleName` method. The default method converts a base bundle name and locale into a name with the locale extension. For example, given the base name of  `common` and the locale `fr-CA`, the default control creates the name `common-fr-CA`. However, our new package-oriented control would create the package-based `fr-ca.common` filename instead.
 
The code to perform this operation is here, a rather short subclass that performs this desirable packaging:
 
 
```java
public class PackageableResourceControl extends ResourceBundle.Control {

    boolean isPackageBased;

    public PackageableResourceControl() {
        this(true);
    }

    public PackageableResourceControl(boolean isPackageBased) {
        this.isPackageBased = isPackageBased;
    }

    /**
     * If this is a package-based control, converts a baseName resource file of
     * the form package-name.ResourceName to
     * package-name.locale-string.ResourceName. This controller separates
     * localized resources into their own subpackage and does not extend the
     * ResourceName.
     * <p>
     * If this is not a package-based control, converts a baseName resource
     * file to standard bundle names as provided by the default Java
     * ResourceBundle.Control class.
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
```

## Use the PackageableResourceControl in Your Own Code

Using the new control is as simple as this:

1. Place localized resources into target locale sub-packages.
2. Create a new instance of `PackageableResourceControl`.
3. Provide this new instance to the `ResourceBundle.getBundle` method.

### Create Localized Resources in Sub-Packages

Imagine you have 3 resource bundles in your base resource package:

* common.properties
* foo.properties
* bar.properties

If you localize these files into Canadian French and Mexican Spanish, you'd put those same file names, with localized strings of course, into the following subpackages as shown earlier:

```text
|
|- common.properties
|- foo.properties
|- bar.properties
|
|-- fr-ca
|  |- common.properties
|  |- foo.properties
|  |- bar.properties
|
|-- es-mx
|  |- common.properties
|  |- foo.properties
|  |- bar.properties
```


### Create a PackageableResourceControl

Create a new control like this:

```java
PackageableResourceControl packageControl = new PackageableResourceControl();
```

The `PackageableResourceControl` is now available to you when you create the actual resource 
bundle instance.

### Create a ResourceBundle With the New Control

Use the `PacakgeableResourceControl` when you create a new resource bundle

```java
ResourceBundle bundle = ResourceBundle.getBundle("common", packageControl);
```

Now you simply access resources in the `common` resource bundle as you normally would. However, 
because of the new package-oriented control, the resource bundle will look in subpackages for 
translated resources. For example, it will look in the `fr-ca` sub-package for French Canadian 
resources.

## Enjoy Your New Organization

Now that you have a better package-oriented resource organization, you can easily tell if what 
translations are available in your app. The answer is as easy as identifying the sub-packages 
under your base resource package.

See this and other example files in my Github repo. Good luck, and happy coding!