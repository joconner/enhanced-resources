# Enhanced Resource Bundles

A ResourceBundle is a set of localizable resources, typically text. To 
localized user interfaces, you typically create one or more resource bundles 
in a default language, submit those to localizers and translators, and then 
include all bundles in your source tree. You can learn more about Resource bundles 
from their [Javadoc 
documentation](http://docs.oracle
.com/javase/8/docs/api/java/util/ResourceBundle.html).
 
The default ResourceBundle types, formats, and lookup strategies haven't 
changed in decades. However, one significant feature allows us to enhance all
 these defaults. By extending either the _ResourceBundle_ or _ResourceBundle
 .Control_ classes, you can improve the default behaviors significantly. This
  project showcases several enhancements that customers have requested, or 
  complained about, numerous times over the years:

1. UTF-8 encoded properties instead of Latin-1 or ASCII-ENCODED ones
2. JSON objects to store key-value pairs
3. Directory-based bundle organizations 

## Default Property Files
Property files are plain-text files that contain localizable resources. They 
contain simple key-value pairs. They look like this:

```
HELLO=I'm just a string, nothing special here.
CAVEAT=But my encoding is LATIN-1!
```

Although property files are effective, they're horribly awkward in one 
significant way: they must be encoded as ISO-8859-1. That means you cannot 
represent most Unicode characters without special escaping techniques. 
  
If you want to create a Japanese language bundle, for example, you must 
escape the Japanese characters to the form `\uXXXX`, where `XXXX` is the 
Unicode hexadecimal codepoint value. The Java Development Kit (JDK) has  
a tool called _native2ascii_ to facilitate this for you. This additional 
escaping makes translated resource bundles practically impossible to visually
 read. For example, the word for "book" is "本"in Japanese. Using the default 
 property bundle loading mechanism, you'd have to ASCII-escape the property 
 file like this:
 
```
 BOOK=\u672C
```

This is simply unacceptable in my opinion. You really want to see the correct
 character in its natural visual form:
 
```
BOOK=本
```

Fortunately, we don't have to sacrifice usability and readability. We can 
create our own _ResourceBundle.Control_ class to override the default loading
 strategy of the _ResourceBundle.getBundle_ method.  

## UTF-8 Enabled ResourceBundle Control


