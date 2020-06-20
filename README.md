# DysonAPIJava
Simple Library to control Dyson Fans with Java
Supported Devices:
- Dyson Pure Cool™ (475)
- Dyson Pure Hot+Coolᵀᴹ Link (455)
- Any Device with similar functions. Fork the Library and add the device in DysonDeviceType.java

## Import to project
1) Download Jarfile with all dependencies in the "Releases" tab
2) Maven:
Repository:
```xml
    <repositories>
        <repository>
            <id>myMavenRepoRead</id>
            <url>https://mymavenrepo.com/repo/QPWAmwOfBYJMVg7noRm7/</url>
        </repository>
    </repositories>
```
Dependency:
```xml
    <dependencies>
        <dependency>
            <groupId>org.tschoerner.christian</groupId>
            <artifactId>dysonapi</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
```

## How to use
*Important* Please notice that at least **Java 8** is required to use this Library<br><br>

1) Get the serial number of your device. It is in the format XXX-XX-XXXXXXXX.
You can find it at the sticker of your fan or in the app:<br />
![alt text](https://cdn.tschoerner.cloud/hcRXygKoa5tzCuu3ylVY0wAt2vlNN9ni4CX82YLTnMqfyezQ "App")

2) Get the WIFI code from the sticker or the manual of your fan. It could look like this:<br>
![alt text](https://cdn.tschoerner.cloud/a5VHT2QBPia6eUf1uIQwPnBO2n1KCuMnjWyT9MBf7Etz1G3071 "Code")
3) Hash the password using [this](https://paste.tschoerner.cloud/r/PEs9p7dtyo) python script (Python 2 & 3). Thanks to [mecks52](https://github.com/mecks52/openhab2-dyson475/blob/master/getPwdHash.py)
4) Get the local IP-Address of your fan
5) Happy coding. This little example shows everything you need to know about the Library and how to use it: <br>
```java
    private static final String DYSON_IP = "192.168.86.XXX";
    private static final String DYSON_SERIALNUMBER = "NN2-EU-XXXXXXXX";
    private static final String DYSON_PASSWORD = "1SFkfgdXRXXXXXXXXXXXXXXXXXXXXXXX";

    public static void main(String[] args) throws MqttException {
        DysonDevice dysonDevice = new DysonDevice(DysonDeviceType.PURECOOLLINK, DYSON_IP,
                DYSON_PASSWORD,
                DYSON_SERIALNUMBER);

        dysonDevice.setField(DysonStateEnum.MODE, ModeField.ON);
        dysonDevice.setField(DysonStateEnum.STATUS, StatusField.ON); // not needed
        dysonDevice.setField(DysonStateEnum.FANSPEED, FanSpeedField.SPEED2);
        dysonDevice.setField(DysonStateEnum.NIGHTMODE, NightModeField.OFF);
        dysonDevice.setField(DysonStateEnum.OSCILLATION, OscillationField.ON);

        dysonDevice.getState(dysonState -> {
            int fanSpeed = dysonState.getInt(DysonStateEnum.FANSPEED);
            boolean nightMode = dysonState.getBoolean(DysonStateEnum.NIGHTMODE);

            System.out.println("FanSpeed: " + fanSpeed);
            System.out.println("NightMode: " + nightMode);
        });

        dysonDevice.getSensorData(dysonSensorData -> {
            Object temperature = dysonSensorData.get(DysonSensorEnum.TEMPERATURE);

            System.out.println("Temperature: " + temperature.toString());
        });

    }

```

## Bugs - Contact
If you found a bug, have questions or want to message me please open an issue. Thank you!

