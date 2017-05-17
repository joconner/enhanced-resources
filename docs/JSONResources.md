# Using JSON as a Java ResourceBundle Format

A `ResourceBundle` is a Java file that holds localizable resources, typically
strings in a `PropertyResourceBundle` or a `ListResourceBundle` implementation. 
A PropertyResourceBundle is implemented as a text file containing key-value 
pairs; it's a properties file. A ListResourceBundle is a Java class that works 
similarly, but it allows values to be more complex. Products use the PropertyResourceBundle
the most. Its simplicity is an overwhelming factor in making the choice between
the two options.

Unfortunately, neither property files nor Java classes are particularly useful 
in storing metadata about text. Sure, you can add comments above or below a 
specific key-value pair, but comments are not useful to a consuming application 
or translation tool. Useful comments and metadata about a particular key-value 
pair are the most often needed items that translators request. Unfortunately, 
they don't get that information from the most common resource bundle formats.

## JSON Provides Options for Meta-Data

    