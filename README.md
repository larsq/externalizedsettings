externalizedsettings
====================

# What

Sometimes codes resides or being published to private
repositories and its data is often sensitive and should
not be stored in git, especially in public repositories.

There are some practices to protect sensitive data such as
 `git-crypt` but in most cases the credentials are distributed
 to the developer manually to be inserted. 
 
 This plugin reads the repository settings from the project, a parent project or the
 gradle user home directory and matches against the given name
 for the repository and configure url and credentials. The information stays out
 from the versioned configuration (by either put it in gradle user directory) or in a file that should be effectively ignored by the system
 
 # How
 
 Place the configuration into a file called `.settings.json`. A configuration
 could look like this:
 
```json
{
  "settings": [
    {
      "name": "name",
      "url": "http://example.com/1",
      "username": "username",
      "password": "password"
    },
    {
      "name": "other",
      "url": "http://example.com/2"
    }
  ]
} 
```
 
`name` and `url`are mandatory but `username` and `password` are optional.


The plugin will start look in the project directory and traverses up through
its parent until the first file is found. If not found in the project
or its ancestors, the plugin make an attempt to find the setting file in the
gradle home user directory.

# Buildscript

The plugin is meaningless without `maven-publish` plugin. A minimal
configuration may look like this:

```kotlin
plugins {
    id("java")
    id("maven-publish")
    id("dev.larsq.externalsettings.repository") version "1.0.0"
} 
```
 
  
# Limitations

* No validation of settings file
* Error handling and message are kept to a minimum
* Name `.settings` is hard-coded and cannot be changed
* No smart configuration (loads all the time but could be kept limited to publishing task)