[![Build Status](https://travis-ci.org/ankel/haulm-topper.svg?branch=master)](https://travis-ci.org/ankel/haulm-topper)

# haulm-topper
Processing raw properties so you can get what you need easier.

## What is a haulm topper?
 [Haulm topper](https://en.wikipedia.org/wiki/Potato_harvester#Haulm_topper) is a machine used in harvesting root crops. It cuts the above ground parts of the plant so that a harvester can get at the roots below, which are the actual fruit of these crops.

## Seriously, what is it?
Apps typically need some configuration, from a `.properties` file for example. And if you've ever worked on any medium-or-bigger sized project, you're probably familiar with this pattern

```java
final Properties properties = new Properties();
properties.load(this.getClass().getResourceAsStream("default.properties"));

SomeType getImportantProperty()
{
  String value = properties.get("key");
  if (value == null)
  { 
    throw new RuntimeException("Property [Key] not found");
  }
  SomeType type = SomeType.parseString(value);
  return type;
}
```

Odd is that you have a class filled with methods similar to this to convert a string configuration to objects that your app uses. These boiler plate code are fairly error prone and for that many lines, there are only one piece of information that cannot be deduced automatically: the property key.
And haulm-topper is here to help you with that. The goal of Haulm-topper is to simplify the configuration process like follow

```java
// Configuration interface
interface Config
{
  @PropertyKey("key")
  SomeType getImportantProperty();
}
//...
Config config = ConfigurationGenerator.createFor(Config.class, properties, ConvertUtility.class);

SomeType prop = config.getImportantProperty();
```

In slow motion:
* You only need to declare an interface whose method annotations tell what their relevant key is.
* An object for the interface is generated for you
  * Optionally, you can provide a class that contains static methods that turn a string into the type that you want.
* That's it, Haulm-topper takes care of getting the property for you.
  * If the properties doesn't contain the annotated key, an exception is thrown.
  * But if the returned type is `java.util.Optional<SomeType>`, it'll be `Optional.empty` instead of an exception. How handy is that?
    * And if the convert method returns null, the final value is still `Optional.empty`

Finally, a set of default conversion methods are provided (`toInt`, `toLong`, `toDouble`) but if your `ConvertUtility` class contains any static method with similar signature, it will override the default ones.
 
For instance, here's a high-performance web-scale implementation of `ConvertUtility`.

```java
public class ConvertUtility
{
  public static int someRandomName(final String s)
  {
    return 42;
  }
}
```
