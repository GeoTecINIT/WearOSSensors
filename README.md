# WearOS Sensors
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.geotecinit/wear-os-sensors/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.geotecinit/wear-os-sensors)

The _WearOS Sensors_ library is an Android WearOS library that allows to collect data from the IMU sensors
(i.e., accelerometer and gyroscope), the magnetometer, the heart rate, and the GPS of an Android WearOS
smartwatch (if the corresponding sensor is available in the device).

This library can be used to build WearOS applications that are the **counterpart** of smartphone applications
built with the [_nativescript-wearos-sensors_](https://github.com/GeoTecINIT/nativescript-wearos-sensors)
plugin (for the [NativeScript](https://nativescript.org) framework).
The smartphone application counterpart can request to start/stop the data collection on the smartwatch, and
then receive the collected data from the smartwatch.

> [!IMPORTANT] 
> An application built with this library is completely useless if there is not a counterpart
> application built with the _nativescript-wearos-sensors_ plugin installed in the paired smartphone.
> In other words, the smartwatch can not work by itself alone. It requires for a smartphone to work.

The data collection can be started both from the smartwatch and from the paired smartphone. In addition,
the library offers a way to communicate with the smartphone by sending messages.

The _WearOS Sensors_ library uses and extends the functionality of the Android 
[_Background Sensors_](https://github.com/GeoTecINIT/BackgroundSensors) library, and therefore, it is safe to carry out
the data collection in the background (i.e., when the app is not in the foreground, or the smartwatch is idle).


## Installation
To install the library you have to add the dependency in your `build.gradle`:

```groovy
dependencies {
    implementation 'io.github.geotecinit:wear-os-sensors:1.2.0'
}
```

## Requirements
The library has the following requirements:

- An Android WearOS smartwatch running WearOS 1.0 (API level 23) or higher. In addition, the smartwatch must
  be paired with a smartphone with the counterpart application installed.
  
> [!IMPORTANT] 
> Both applications (smartwatch and smartphone apps) must have the same [application id](https://developer.android.com/studio/build/configure-app-module#set-application-id).
> If that's not the case, the applications will not be able to interact.

> [!TIP]
> Don't forget to check the requirements of [_nativescript-wearos-sensors_](https://github.com/GeoTecINIT/nativescript-wearos-sensors) too.

### Tested WearOS versions and devices
The library has been tested in the following WearOS versions:

- **Wear OS 2.33** (Android 9 - API 28):
  - TicWatch Pro 3 GPS
- **Wear OS 3.5** (Android 11 - API 30):
  - TicWatch Pro 3 GPS
  - TicWatch Pro 5
- **WearOS 4** (Android 13 - API 33):
  - Samsung Watch4

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

  You **must** add the following permission to the manifest:

  ```xml
  <uses-permission android:name="android.permission.BODY_SENSORS" />
  ```

  In addition, if your app runs on a WearOS 4+ smartwatch (Android 13 - API 33), add the following permission:

  ```xml
  <uses-permission android:name="android.permission.BODY_SENSORS_BACKGROUND" />
  ```

</details>
<details>
  <summary>Location</summary>

  You **must** add the following permission to the manifest:

  ```xml
  <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
  <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
  ```

  In addition, if your app runs on a WearOS 3+ smartwatch (Android 13 - API 33), add the following permission:

  ```xml
  <uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
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
activity for requesting permissions using the [`PermissionsManager.setPermissionsActivity()`](#permissionsmanager). There are two
ways of requesting permissions:

- If the data collection is started using the paired smartphone, the permissions will be automatically requested. You have to do nothing!
- If the data collection is started from the smartphone, you should check that the necessary permissions are granted using the [`PermissionsManager.launchPermissionsRequestIfNeeded()`](#permissionsmanager)

and the library
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

If your app runs in WearOS 4+, the `POST_NOTIFICATIONS` permission will be required. To do so, we also
provide the [`PermissionsManager.launchRequiredPermissionsRequest()`](#permissionsmanager).

Finally, here is a sample on how to setup your activity for requesting permissions:
```java
public class MainActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        // ...
      
        PermissionsManager.setPermissionsActivity(this, YourRequestPermissionsActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionsManager.launchRequiredPermissionsRequest(this);
        }
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
        // If the sensor requires permissions and have not been granted...
        boolean requested = PermissionsManager.launchPermissionsRequestIfNeeded(this, sensor.getRequiredPermissions());
        if (requested) return;
        
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

> [!TIP]
> Here we are using [`Sensor`](#sensor) and [`CollectionConfiguration`](#collectionconfiguration)
> from [_Background Sensors_](https://github.com/GeoTecINIT/BackgroundSensors).
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

> [!TIP]
> You can find a full sample of all these features in the [MainActivity](app/src/main/java/es/uji/geotec/wearossensorsdemo/MainActivity.java)
> and [RequestPermissionsActivity](app/src/main/java/es/uji/geotec/wearossensorsdemo/RequestPermissionsActivity.java) activities 
> of the demo application.

## API

### [`WearSensor`](wearossensors/src/main/java/es/uji/geotec/wearossensors/sensor/WearSensor.java)
| **Value**       | **Description**                      |
|-----------------|--------------------------------------|
| `ACCELEROMETER` | Represents the accelerometer sensor. |
| `GYROSCOPE`     | Represents the gyroscope sensor.     |
| `MAGNETOMETER`  | Represents the magnetometer sensor.  |
| `HEART_RATE`    | Represents the heart rate monitor.   |
| `LOCATION`      | Represents the GPS.                  |

Each sensor provide the `getRequiredPermissions()` method to obtain the permissions that need to be
requested for the specified sensor. Use it along `PermissionsManager.launchPermissionsRequestIfNeeded()`.

### `SensorManager`
Refer to the [_Background Sensors_](https://github.com/GeoTecINIT/BackgroundSensors#sensormanager) documentation.

### [`PermissionsManager`](wearossensors/src/main/java/es/uji/geotec/wearossensors/permissions/PermissionsManager.java)
| **Static Method**                                                                    | **Return type**     | **Description**                                                                                                                                                                                         |
|--------------------------------------------------------------------------------------|---------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `permissionsToRequestFromIntent(Intent intent)`                                      | `ArrayList<String>` | Permissions to request in the custom request permissions activity.                                                                                                                                      |
| `specialPermissionsToRequestFromIntent(Intent intent)`                               | `ArrayList<String>` | Special permissions to request in the custom request permissions activity.                                                                                                                              |
| `launchRequiredPermissionsRequest(Activity activity)`                                | `void`              | Launch the permissions activity to request the required permission to post notifications (only WearOS 4+).                                                                                              |
| `launchPermissionsRequestIfNeeded(Activity activity, ArrayList<String> permissions)` | `boolean`           | Launch the permissions activity to request the specified permissions. Call this method to request the permissions for a specified sensor. Returns `true` if the permissions activity has been launched. |
| `permissionsToBeRequested(Context context, ArrayList<String> required)`              | `ArrayList<String>` | Returns the permissions that need to be requested (internal use only).                                                                                                                                  |
| `requestPermissions(Activity activity, ArrayList<String> permissions)`               | `void`              | Request the specified permissions previously obtained from `permissionsToRequestFromIntent`. You should call this method in your custom request permissions activity.                                   |
| `setPermissionsActivity(Context context, Class<?> permissionsActivity)`              | `void`              | Sets up the class that will be used for requesting permissions. You should call this method in your MainActivity class once your app has started.                                                       |
| `getPermissionsActivity(Context context)`                                            | `Class<?>`          | Gets the class that will be used for requesting permissions.                                                                                                                                            |


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
Refer to the [_Background Sensors_](https://github.com/GeoTecINIT/BackgroundSensors#collectionconfiguration) documentation.

#### Sensor
Refer to the [_Background Sensors_](https://github.com/GeoTecINIT/BackgroundSensors) documentation.

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


## Author

<a href="https://github.com/matey97" title="Miguel Matey Sanz">
  <img src="https://avatars3.githubusercontent.com/u/25453537?s=120" alt="Miguel Matey Sanz" width="120"/>
</a>


## Acknowledgements

The development of this library has been possible thanks to the Spanish Ministry of Universities (grant FPU19/05352).



