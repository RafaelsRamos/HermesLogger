# Hermes

Hermes is an open source Android library that aims at increasing the overall team's productivity. It does so by empowering not-so-technical team members, such as QAs and Testers to more easily and independently perform software analysis. 
By empowering not-so-technical team members, the time developers have to dedicate clarifying behaviours or looking into issues that, turn out, can only be solved by another team, would decrease.

<h3 align="center">Usual issue analysis flowchart</h1>
<p align="center">
  <img src="readme_resources/issue-tracking.drawio.svg">
</p>

Section A and C are inevitable, making it more likely to navigate from Section A to C, not having to pass by Section B, will result in an overall team productivity improvement.

## Gradle Setup

```
repositories {
  google()
  mavenCentral()
}

dependencies {
  implementation 'io.github.rafaelsramos:hermeslogger:0.1.0'
}
```

## Setup in project

With 3 simple lines, the HermesLogger is attached to the project.

``` kotlin
Hermes.initialize(isDebugEnvironment)
// SystemInfoBuildable implementation
Hermes.updateSystemInfo(systemInfoBuildable)
OverviewLayout.create(activityReference)
```
`Hermes.initialize(isDebugEnvironment)` informs Hermes if current environment is or not a debug environment. If it's not, all interactions with the library will be aborted.

`Hermes.updateSystemInfo(systemInfoBuildable)` provides Hermes with an implementation that will be used to build device/app information, such as device model, dimentions, or even App environment or variant.

`OverviewLayout.create(activityReference)` generates an OverviewLayout, that will inflate itself on top the activity's views. This layout is where:
- All the submitted logs can be seen;
- Logs can be removed;
- Individual logs can be shared;
- Entire log stack can be shared;

## Use cases

Debugging and testing an Android application can be hard. However, with some well placed hints, one's life can be substantially easier.

Here are some **Common** use-cases:
- Transitions between fragments:
    ```kotlin
    Hermes.i().message("Transitioning to: ${fragment.javaClass.simpleName}").submit()
    ```
- API call information:
    ```kotlin
    Hermes.i()
        .message("AllProducts API call")
        .extraInfo(result.data, format = DataType.JSON) // Formats the content as Json
        .submit()
    ```
- Exceptions
    ```kotlin
    try {
        throw IllegalArgumentException("Invalid arguments")
    } catch (ex: IllegalArgumentException) {
        Hermes.e().throwable(ex).submit()
    }
    ```

Specially for QAs and Testers, it is part of the job to share with devs bugs and strange behaviours. With that specific action in mind, it is possible to share a single or the whole stack of logs though Messenger, Whatsapp, mail, ect.

To use such a feature, it is necessary to request `WRITE_EXTERNAL_STORAGE` permission from the user:

For Android 11- devices, permissions can be 
1. Declare the permission on **AndroidManifest.xml**
   ```xml 
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    ```
2. Ask permission to the user at runtime (using [TedPermission](https://github.com/ParkSangGwon/TedPermission) in the example below)
    ```kotlin
    TedPermission
        .with(this)
        .setDeniedMessage("Necessary to use share feature\n\n")
        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .check()
    ```

# More examples

// Show data formatting, ect.

# Beta

// Brief explaination on why shares are usefull / show shares
