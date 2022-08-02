# WearOS Sensors
The _wearossensors_ library is an Android WearOS library that allows to collect data from the IMU sensors
(i.e., accelerometer and gyroscope), the magnetometer, the heart rate, and the GPS of an Android WearOS
smartwatch (if the corresponding sensor is available in the device).

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

The _wearossensors_ library uses and extends the functionality of the Android 
[_backgroundsensors_](https://github.com/GeoTecINIT/BackgroundSensors) library, and therefore, it is safe to carry out
the data collection in the background (i.e., when the app is not in the foreground, or the smartwatch is idle).


## Installation
To install the library you have to add the [Jitpack](https://jitpack.io) repository to the file where your project
declares the URLs of the external repositories where the dependencies are looked for:

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

- An Android WearOS smartwatch running WearOS 1.0 (API level 23) or higher. In addition, the smartwatch must
  be paired with a smartphone with the counterpart application installed.
  
> **Warning**: Both applications (smartwatch and smartphone apps) must have the same [application id](https://developer.android.com/studio/build/configure-app-module#set-application-id).
> If that's not the case, the applications will not be able to interact.

> **Note**: Don't forget to check the requirements of [_nativescript-wearos-sensors_](https://github.com/GeoTecINIT/nativescript-wearos-sensors) too.

## Usage
The library offers two main features:

- [Sensor data collection](#sensor-data-collection): it can be started/stopped from the paired smartphone or from the smartwatch itself.
  Some permissions must be added to the **AndroidManifest of the smartwatch device** depending on which sensor you want to collect data from:
<details>
  <summary>Accelerometer, gyroscope and magnetometer</summary>

  If your app targets an API level 31 or higher and you will to collect data from the sensors
  at a sampling rate higher than 200Hz, the following permission must be added to the manifest:

  ```xml
  <uses-permission android:name="android.permission.HIGH_SAMPLING_RATE_SENSORS" />
  ```
</details>
<details>
  <summary>Heart rate</summary>

  ```xml
  <uses-permission android:name="android.permission.BODY_SENSORS" />
  ```
</details>
<details>
<summary>Location</summary>

  ```xml
  <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
  <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
  ```
</details>

- [Messaging](#messaging): it allows to send and receive simple messages between both devices.

### Sensor data collection
The library allows to collect data from the accelerometer, gyroscope, magnetometer, heart rate and GPS sensors
of the smartwatch device. For the data collection, the application in the smartwatch acts as a _companion_
application, where the _main_ application is the on in the smartphone. This means that the smartphone is 
who _instructs_ the smartwatch to start/stop the data collection, even when is the smartwatch who wants to start/stop the data collection.

Before going deeper with the data collection, we have to talk about permissions.

#### Permissions
In order to access to the data of the heart rate and the GPS sensors, the user
has to grant some permissions. The library handles this situation (i.e., when permissions are required), and
launches a notification to warn the user that some permissions need to be granted. However, we **do not**
provide a mechanism to ask the permissions, we delegate that task on the developer. Why? To customise the
way the permissions are required. In other words, the developer has to provide a class reference of an
activity for requesting permissions using the [`PermissionsManager`](#permissionsmanager), and the library
will start that activity when any permission is required and the user taps into the notification warning.

We have tried to make it easy for you to implement that activity:
- You can obtain the list of the permissions that are required and also to request them using the  [`PermissionsManager`](#permissionsmanager) API.
- After the user grants or denies a permission, you have to tell the smartphone that which permissions
  have been granted and which ones not using the [`PermissionsResultClient`](#permissionsresultclient). 
  Remember: the smartphone is the one that starts the data collection, including the request for permissions.
  
Here you can see an example of an activity for requesting permissions:
```java
public class YourRequestPermissionsActivity extends FragmentActivity {
    protected void onCreate(Bundle savedInstanceState) {
        // ...

        // Get permissions to request
        ArrayList<String> permissionsToRequest = PermissionsManager.permissionsToRequestFromIntent(getIntent());
        
        // You can show a message explaining why you are going to request permissions
        // and then...
        PermissionsManager.requestPermissions(this, permissions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Check which permissions are granted and which ones rejected
      
        // Tell the smartphone if ...
        PermissionsResultClient permissionsResultClient = new PermissionsResultClient(this);
        
        // all permissions are granted ...
        permissionsResultClient.sendPermissionsSuccessfulResponse(getIntent());
        
        // or if any permission was rejected (include in your failure message which permission were rejected)
        permissionsResultClient.sendPermissionsFailureResponse(getIntent(), failureMessage);
    }
}
```

Finally, here is a sample on how to setup your activity for requesting permissions:
```java
public class MainActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        // ...
      
        PermissionsManager.setPermissionsActivity(this, YourRequestPermissionsActivity.class);
    }
}
```

#### Start/stop data collection from smartphone
The library fully handles this for you. You have to do nothing!

#### Start/stop data collection from smartwatch
To start or stop the data collection, the smartphone needs to be updated regarding the change in the data
collection status. So, if we want to start/stop the data collection from the smartwatch, we have to notify that intention to the smartphone.
Then, the smartphone will update its internal status and once everything is set up, it will confirm the smartwatch
that the data collection can be started/stopped, so the smartwatch can act in consequence.

In order to start/stop the data collection from the smartwatch you can use the [`CommandClient`](#commandclient). Also, the sensors are defined in 
[`WearSensor`](#wearsensor) and you can get the sensors that are available using the [`SensorManager`](#sensormanager).
Here you can see a sample usage:

```java
public class MainActivity extends Activity {
    // ...
    private CommandClient commandClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ...
        commandClient = new CommandClient(this);
    }
    
    public void setupUI() {
        List<Sensor> availableSensors = sensorManager.availableSensors(WearSensor.values());
    }

    public void onStartSingleCommandTap(Sensor sensor) {
        CollectionConfiguration config = new CollectionConfiguration(
                selectedSensor,
                android.hardware.SensorManager.SENSOR_DELAY_GAME,
                selectedSensor == WearSensor.HEART_RATE || selectedSensor == WearSensor.LOCATION ? 1 : 50
        );
        commandClient.sendStartCommand(config);
    }

    public void onStopSingleCommandTap(Sensor sensor) {
        commandClient.sendStopCommand(selectedSensor);
    }
    
    // ...
}
```

> **Note**: Here we are using [`Sensor`](#sensor) and [`CollectionConfiguration`](#collectionconfiguration)
> from [_backgroundsensors_](https://github.com/GeoTecINIT/BackgroundSensors).
> Check its documentation for more information.

### Messaging
When having a system composed by several devices (two, in our case), it is important to have a way to
communicate. We provide the [`PlainMessageClient`](#plainmessageclient), which allows to send and receive string-based messages.
There are two types of received messages: the ones which require a response and the ones which don't.
For now, sending messages with required response is only available from the smartphone side.

Here you can see an example on how to use the messaging feature:

```java
public class MainActivity extends Activity {
    // ...
    private PlainMessageClient plainMessageClient;
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ...
        plainMessageClient = new PlainMessageClient(this);
        
        // Register a listener for the messages
        plainMessageClient.registerListener(message -> {
            Log.d("MainActivity", "received " + message);
    
            // We received a message with response required so...
            if (message.responseRequired()){
                Log.d("MainActivity", "response required! sending response...");
                // We send a response
                PlainMessage response = new PlainMessage("PONG!", message.getPlainMessage());
                plainMessageClient.send(response);
            }
        });
    }

      public void onSendPlainMessageTap(View view) {
            PlainMessage message = new PlainMessage("Hi! This is a test message");
            plainMessageClient.send(message);
      }
  }
```

> **Note**: you can find a full sample of all these features in the [MainActivity](app/src/main/java/es/uji/geotec/wearossensorsdemo/MainActivity.java)
and [RequestPermissionsActivity](app/src/main/java/es/uji/geotec/wearossensorsdemo/RequestPermissionsActivity.java) activities
of the demo application.

## API

### [`WearSensor`](wearossensors/src/main/java/es/uji/geotec/wearossensors/sensor/WearSensor.java)
| **Value**       | **Description**                      |
|-----------------|--------------------------------------|
| `ACCELEROMETER` | Represents the accelerometer sensor. |
| `GYROSCOPE`     | Represents the gyroscope sensor.     |
| `MAGNETOMETER`  | Represents the magnetometer sensor.  |
| `HEART_RATE`    | Represents the heart rate monitor.   |
| `LOCATION`      | Represents the GPS.                  |

### `SensorManager`
Refer to the [_backgroundsensors_](https://github.com/GeoTecINIT/BackgroundSensors#sensormanager) documentation.

### [`PermissionsManager`](wearossensors/src/main/java/es/uji/geotec/wearossensors/permissions/PermissionsManager.java)
| **Static Method**                                                       | **Return type**     | **Description**                                                                                                                                                        |
|-------------------------------------------------------------------------|---------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `permissionsToRequestFromIntent(Intent intent)`                         | `ArrayList<String>` | Permissions to request in the custom request permissions activity.                                                                                                     |
| `permissionsToBeRequested(Context context, ArrayList<String> required)` | `ArrayList<String>` | Returns the permissions that need to be requested (internal use only).                                                                                                 |
| `requestPermissions(Activity activity, ArrayList<String> permissions)`  | `void`              | Request the specified permissions previously obtained from `permissionsToRequestFromIntent`. You should call this method in your custom request permissions activity.  |
| `setPermissionsActivity(Context context, Class<?> permissionsActivity)` | `void`              | Sets up the class that will be used for requesting permissions. You should call this method in your MainActivity class once your app has started.                      |
| `getPermissionsActivity(Context context)`                               | `Class<?>`          | Gets the class that will be used for requesting permissions.                                                                                                           |


### [`PermissionsResultClient`](wearossensors/src/main/java/es/uji/geotec/wearossensors/permissions/PermissionsResultClient.java)
| **Method**                                                            | **Return type** | **Description**                                                                                                                                                                 |
|-----------------------------------------------------------------------|-----------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `sendPermissionsSuccessfulResponse(Intent intent)`                    | `void`          | Indicates to the smartphone that all requested permissions have been granted                                                                                                    |
| `sendPermissionsFailureResponse(Intent intent, String failureReason)` | `void`          | Indicates to the smartphone that some requested permissions have not been granted. It includes a message to indicate which permission/s was/were not granted (`failureReason`). |

### [`CommandClient`](wearossensors/src/main/java/es/uji/geotec/wearossensors/command/CommandClient.java)
| **Method**                                                | **Return type** | **Description**                                                                                               |
|-----------------------------------------------------------|-----------------|---------------------------------------------------------------------------------------------------------------|
| `sendStartCommand(CollectionConfiguration configuration)` | `void`          | Sends a command to the smartphone to start the collection in the smartwatch with the specified configuration. |
| `sendStopCommand(Sensor sensor)`                          | `void`          | Sends a command to the smartphone to stop the collection of the specified sensor in the smartwatch.           |

#### CollectionConfiguration
Refer to the [_backgroundsensors_](https://github.com/GeoTecINIT/BackgroundSensors#collectionconfiguration) documentation.

#### Sensor
Refer to the [_backgroundsensors_](https://github.com/GeoTecINIT/BackgroundSensors) documentation.

### [`PlainMessageClient`](wearossensors/src/main/java/es/uji/geotec/wearossensors/plainmessage/PlainMessageClient.java)
| **Method**                                        | **Return type** | **Description**                                     |
|---------------------------------------------------|-----------------|-----------------------------------------------------|
| `registerListener(PlainMessageListener listener)` | `void`          | Registers the listener for the messaging feature.   |
| `unregisterListener()`                            | `void`          | Unregisters the listener for the messaging feature. |
| `send(PlainMessage plainMessage)`                 | `void`          | Sends a message to the smartphone.                  |

#### [`PlainMessage`](wearossensors/src/main/java/es/uji/geotec/wearossensors/plainmessage/PlainMessage.java)
| **Field**        |  **Type**     | **Description**                                                                             |
|------------------|---------------|---------------------------------------------------------------------------------------------|
| `message`        | `String`      | Content of the message.                                                                     |
| `inResponseTo`   | `PlainMessage` | If the message is a response to other one, the reference to that message. `null` otherwise. |

## License

Apache License 2.0

See [LICENSE](LICENSE).


