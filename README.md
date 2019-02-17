Building
--------

CheckDeath builds against the [Spigot-API](https://hub.spigotmc.org/javadocs/spigot/) using [Maven](https://maven.apache.org/), rather than the product that [BuildTools](https://www.spigotmc.org/wiki/buildtools) generates.
This means that you do not need to trouble yourself with getting the BuildTools and such just to compile this project.

Use Maven to build the plugin file, run the following command:
```
mvn clean install
```

Each resulting jar file will generate inside /target. Then simply toss it into your server's /plugins folder and enjoy!