# JSON Packaged Resource Bundle

Resource bundles are a group of related files that contain localized 
resources, typically text. The most common resource type is a text-only 
**properties** file containing key-value pairs. You can learn more about 
Resource bundles generally from their default [Javadoc documentation](http://docs.oracle.com/javase/6/docs/api/java/util/ResourceBundle.html) and other tutorials. 

The Java default ResourceBundle types and organization have neither changed 
nor provided any significant new options in decades. However, one significant 
feature allows us to modify bundle organization layout and even file 
format. This project, _json-packaged-bundle_ provides subclasses of 
_ResourceBundle_ and _ResourceBundle.Control_ that allow you to create 
bundles with the following features:

1. Use JSON objects to store key-value pairs.
2. Organize bundles in a directory-oriented structure in which translated 
files for a target locale all exist within a specific subdirectory/package. 

## Motivation for JSON Files
JSON is a localization file format shared with JavaScript localization 
libraries like Dojo I18n. JSON files should be encoded in UTF-8. Unlike 
the default **properties** files, JSON files don't need to be 
backslash-escaped to encode characters outside of the Latin-1 or ISO-8859-1 
range.

## Motivation for Package-Oriented Bundles

  
