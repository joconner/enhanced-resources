# JSON Packaged Resource Bundle

Resource bundles are a group of related files that contain localized 
resources, typically text. The most common resource type is a text-only 
properties file containing key-value pairs. You can learn more about Resource
 bundles generally from their default [Javadoc documentation](http://docs.oracle.com/javase/6/docs/api/java/util/ResourceBundle.html) and other tutorials. 

The Java default bundle types and organization have neither changed nor 
provided any significant new options in decades. However, one significant 
feature allows us to modify bundle organization layout and even file 
format. This project, _json-packaged-bundle_ provides subclasses of 
_ResourceBundle_ and _ResourceBundle.Control_ that allow you to use JSON 
files in a directory-organized structure.

## Motivation for JSON Files
JSON is a localization file format shared with JavaScript localization 
libraries like Dojo I18n. 
  
