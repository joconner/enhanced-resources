# Enhanced Resource Bundles

A ResourceBundle is a set of localizable resources, typically text, organized
 into a localizable file. To localized user interfaces, you typically create one or more resource bundles 
in a default language, submit those to localizers and translators, and then 
include all bundles in your source tree. You can learn more about Resource bundles 
from their [Javadoc documentation](http://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.html).
 
The default ResourceBundle types, formats, and lookup strategies haven't 
changed in decades. However, one significant feature allows us to enhance all
 these defaults. By extending either the [ResourceBundle](http://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.html) 
 or [ResourceBundle.Control](http://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.Control.html) classes, you can improve the default behaviors 
 significantly. This project showcases several enhancements that customers 
 have requested -- or even complained about -- numerous times over the years:

1. [UTF-8 encoded properties](docs/UTF8Resources.md) instead of Latin-1 or ASCII-ENCODED ones
2. [JSON objects](docs/JSONResources.md) to store key-value pairs
3. Directory-based bundle organizations (coming soon)

