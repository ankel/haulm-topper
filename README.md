# haulm-topper
Processing raw properties so you can get what you need easier.

## What is a haulm topper?
 [Haulm topper](https://en.wikipedia.org/wiki/Potato_harvester#Haulm_topper) is a machine used in harvesting root crops. It cuts the above ground parts of the plant so that a harvester can get at the roots below, which are the actual fruit of these crops.

## The problem
Apps typically need some configuration, from a `.properties` file for example. And if you've ever worked on any medium-or-bigger sized project, you're probably familiar with this pattern

```java
Properties properties;
//... read in the properties

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

Odd is that you have a class filled with methods similar to this to convert a string configuration to objects that your app uses.

And haulm-topper is here to help you with that. The goal of Haulm-topper is to simplify the configuration process like follow

```java
// Configuration interface
interface Config
{
  @PropertyKey("key")
  SomeType getImportantProperty();
}
//...
Config config = ConfigurationGenerator.createFor(Config.class, properties, SomeType.class);

SomeType prop = config.getImportantProperty();
```

In slow motion:
* You only need to declare an interface whose method annotations tell what their relevant key is.
* An object for the interface is generated for you
  * Optionally, you can provide a class that contains static methods that turn a string into the type that you want.
* That's it, Haulm-topper takes care of getting the property for you.
  * If the properties doesn't contain the annotated key, an exception is thrown.
  * But if the returned type is `Optional<SomeType>`, it'll be `Optional.empty`. How handy is that!
  
## Why the name?
 [Haulm topper](https://en.wikipedia.org/wiki/Potato_harvester#Haulm_topper) is a machine used in harvesting root crops. It cuts the above ground parts of the plant so that a harvester can get at the roots below, which are the actual fruit of these crops.  
