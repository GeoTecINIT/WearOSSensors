# WearOS Sensors
The _wearossensors_ library is an Android WearOS library that allows to collect data from the IMU sensors
(i.e., accelerometer and gyroscope), the magnetometer, the heart rate, and the GPS of an Android WearOS
smartwatch (if the corresponding sensor are available in the device).

This library can be used to build WearOS applications that are the **counterpart** of smartphone applications
built with the [_nativescript-wearos-sensors_](https://github.com/GeoTecINIT/nativescript-wearos-sensors)
plugin (for the [NativeScript](https://nativescript.org) framework).
The smartphone application counterpart can request to start/stop the data collection on the smartwatch, and
then receive the collected data from the smartwatch.

> **Warning**: An application built with this library is completely useless if there is not a counterpart
> application built with the _nativescript-wearos-sensors_ plugin installed in the paired smartphone.
> In other words, the smartwatch can not work by itself alone. It requires for a smartphone to work.

The data collection can be started both from the smartwatch and from the paired smartphone. In addition,
the library offers a way to communicate with the smartphone by sending messages.

The _wearossensors_ library uses and extends the functionality of the Android library
[_backgroundsensors_](https://github.com/GeoTecINIT/BackgroundSensors), and therefore, it is safe to carry out
the data collection in the background (i.e., when the app is not in the foreground, or the smartwatch is idle).


## Installation
To install the library you have to add the [Jitpack](https://jitpack.io) repository to file where your project
describes the repositories:

<details>
  <summary>build.gradle (project)</summary>

```groovy
allprojects {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```
</details>

<details>
  <summary>settings.gradle</summary>

```groovy
dependencyResolutionManagement {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```
</details>

Then, just add the dependency in your _build.gradle_ (module):

```groovy
dependencies {
    implementation 'com.github.GeotecINIT:WearOSSensors:X.X.X'
}
```

## Requirements
The library has the following requirements:

- An Android WearOS smartwatch running WearOS 1.0 (API level 23). In addition, the smartwatch must
  be paired with a smartphone with the counterpart application installed.

> **Warning**: Both applications (smartwatch and smartphone apps) must have the same application id.
> If that's not the case, the applications will not be able to interact.

- _(Optional)_ For apps targeting an API level 31 or higher and willing to collect data from the sensors
  at a sampling rate higher than 200Hz, the following permission must be added:

```xml
<uses-permission android:name="android.permission.HIGH_SAMPLING_RATE_SENSORS" />
```
- _(Optional)_ Apps willing to collect data from the heart rate sensor of the device must add the next permission:

```xml
<uses-permission android:name="android.permission.BODY_SENSORS" />
```

- _(Optional)_ Apps willing to collect location data from the GPS of the device must add the next permission:

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

> **Note**: Don't forget to check the requirements of [_nativescript-wearos-sensors_](https://github.com/GeoTecINIT/nativescript-wearos-sensors)

## Usage
TODO

## API
TODO

## License

Apache License 2.0

See [LICENSE](LICENSE).


