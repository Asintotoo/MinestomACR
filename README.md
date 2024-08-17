# MinestomACR
Minestom Automatic Command Registration

[![platform](https://img.shields.io/badge/platform-Minestom-ff69b4?style=for-the-badge)](https://github.com/Minestom/Minestom)

MinestomACR is small utility library for Minestom.

It saves time by automatically registering commands.
You just need **one** more line of code and that's it!

The maven repository is available on [jitpack](https://jitpack.io/#Asintotoo/MinestomACR/).

## Usage

After having initialized your MinecraftServer, y call `MinestomACR.init()`. This will allow you to use the Auto Registration features.

```java
MinecraftServer.init()

MinestomACR.init();

// other code here
```

`MinestomACR.init()` can take a `boolean inverse` as parameter. If not given, the default value will be "false".
With `MinestomACR.init(false)` or simple `MinestomACR.init()`, in order for a class to be registered, it must be annotated with `@AutoRegister`.
With `MinestomACR.init(true)`, all the command classes will be registered without needing to annotate them. To NOT register a specific class, simply annotate it with `@DontRegister`.

## Example Usage

Here are two example of how to use this library:

**inverse mode: false** (default): The following command will be registered because it is annotated with `@AutoRegister`
```java
@AutoRegister
public class TestCommand extends Command {
      public TestCommand() {
          super("test");  
      }
}
```

**inverse mode: true**: The following command will NOT be registered because it is annotated with `@DontRegister`
```java
@DontRegister
public class TestCommand extends Command {
      public TestCommand() {
          super("test");  
      }
}
```

**Important Note**: Make sure your command class constructor is a *No Args Constructor* otherwise MinestomACR won't be able to register the class.
