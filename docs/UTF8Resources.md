# Using Unicode in Java Resource Bundles

![Shipping containers](images/shipping-containers.jpg)

Java resource bundles contain localizable text. One type of bundle is a 
PropertyResourceBundle. This bundle is a plain-text file that contains simple 
key-value pairs that like this:

```
HELLO=I'm just a string, nothing special here.
CAVEAT=But my encoding is LATIN-1!
```

Although Java property files are effective and are widely used, they're 
horribly awkward in one significant way: they must be encoded as ISO-8859-1. 
That means you cannot represent most Unicode characters without special 
escaping techniques if you're using JDK 8 or earlier versions.
 
## Fumbling with ASCII Encodings
  
In JDK 8 and earlier, you must _ASCII-escape_ non Latin-1 characters if you 
want to use a `PropertyResourceBundle`. The Java Development Kit (JDK) has a 
tool called _native2ascii_ to facilitate the conversion. This additional 
escaping makes translated resource bundles practically impossible to visually 
read. For example, the word for "book" is "本" (hon) in Japanese. Using the 
default property bundle loading mechanism, you'd have to ASCII-escape the 
property file. The ASCII-escape format is `\uXXXX`, where `XXXX` represents 
a UTF-16 code unit hex-value:
 
```text
 BOOK=\u672C
```

This is simply unacceptable in my opinion. We prefer to see the correct 
character in its natural visual form:
 
```text
BOOK=本
```

## Creating a UTF-8 Enabled ResourceBundle Control

Fortunately, you don't have to sacrifice usability and readability. We can 
create our own `ResourceBundle.Control` class to override the default loading 
strategy used by the `ResourceBundle.getBundle` method. By overloading the 
`newBundle` method, we can easily tell the bundle loader to use `UTF-8` for the
property file charset encoding. 

Create a `PropertyResourceBundle` using a UTF-8 `InputStreamReader` instead
of an `InputStream` that's limited to Latin-1:

```java
InputStream stream = classLoader.getResourceAsStream(resourceName);
Reader reader = new InputStreamReader(stream, "UTF-8");
ResourceBundle bundle = new PropertyResourceBundle(reader);
```

The full source code for the `UTF8ResourceBundleControl` is in my Github 
[Enhanced Resources](https://github.com/joconner/enhanced-resources) project. 
Please take a look for the complete details of how to implement the
`ResourceBundle.Control` subclass.

## Using the UTF8ResourceBundleControl

Using the new `UTF8ResourceBundleControl` is as simple as providing it to the 
`ResourceBundle.getBundle` method. In this test, I create a Japanese language
bundle and retrieve a UTF-8 encoded string:

```java
ResourceBundle.Control utf8Control = new Utf8ResourceBundleControl();
ResourceBundle bundle = ResourceBundle.getBundle("com.joconner.i18n.res.Utf8Resources", 
    Locale.JAPANESE, utf8Control);
String hello = bundle.getString("HELLO");
assertEquals("こんにちは！", hello);
```

## Looking Forward to JDK 9

JDK 9 has added support for UTF-8 Property Files. You can read more about this 
welcome addition in the [OpenJDK JEP notes](http://openjdk.java.net/jeps/226). 
You'll be able to enjoy UTF-8 resources without extra work. In the meantime, however,
if you're going to continue using JDK 8 for a while longer, please give my 
implementation a try. And tell me how it goes! 

Download the source code for `UTF8ResourceBundleControl` and more from my
[Enhanced Resources Github Repo](https://github.com/joconner/enhanced-resources).
