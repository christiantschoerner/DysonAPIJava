# DysonAPIJava
Simple Library to control Dyson Fans with Java
Supported Devices:
- Dyson Pure Cool™ (475)
- More coming

## Import to project
1) Download Jarfile with all dependencies included [here](https://drive.google.com/uc?export=download&id=1qwacjVMhxpuMV_Xe_lWYlEAdlghxft8c)
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
1) Get the serial number of your device. It is in the format XXX-XX-XXXXXXXX.
You can find it at the sticker of your fan or in the app:<br />
![alt text](https://i.ibb.co/ZGVMwfJ/github.jpg "App")

2) Get the WIFI code from the sticker or the manual of your fan
3) Hash the password using [this](https://pastebin.com/raw/Sv89m4jj) python script. Thanks to [mecks52](https://github.com/mecks52/openhab2-dyson475/blob/master/getPwdHash.py)
4) Get the local IP address of your fan
5) Start coding. Create an object of your fan and connect to it
```java
DysonDevice475 dyson = new DysonDevice475("SERIALNUMBER", "IPADDRESS", "HASHED PASSWORD");
dyson.connect();
```
6) Change some values:
```java
dyson.turnOn();
dyson.setFanLevel(3);
dyson.setOscillation(true);
dyson.setNightmode(false);
```
7) Request Fan's state.
**Important** Please wait at least 1 second after connect() before you request a state. Otherwise values may be null because the fan did not send a callback yet. Data is automatically requested on connect and after that every 5 seconds. If you want to request data manually use `dyson.requestData();` You can do a request after a second for example with a Timer like this:\n
Synchronous:
```java
new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    System.out.println("Fanspeed: " + dyson.getState().getFanSpeedInt());
                    System.out.println("Temperature Kevlin: " + dyson.getSensor().getTemperatureKelvin());
                    System.out.println("Temperature Celsius: " + dyson.getSensor().getTemperatureCelsius());
                    System.out.println("Temperature Fahrenheit: " + dyson.getSensor().getTemperatureFahrenheit());
                    
                    // do something cool
                } catch (MqttException e) {
                    e.printStackTrace();
                }

            }
        }, 1000);
```
Asynchronous:
```java
        Executors.newScheduledThreadPool(5).schedule(new Runnable() {
            public void run() {
                try {
                    dyson.disconnect();

                    System.out.println("Fanspeed: " + dyson.getState().getFanSpeedInt());
                    System.out.println("Temperature Kevlin: " + dyson.getSensor().getTemperatureKelvin());
                    System.out.println("Temperature Celsius: " + dyson.getSensor().getTemperatureCelsius());
                    System.out.println("Temperature Fahrenheit: " + dyson.getSensor().getTemperatureFahrenheit());
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }, 1, TimeUnit.SECONDS);
```

8) Disconnect after your stuff is done. This is important because the MqTT Handler only keeps 10 connections alive. When you forgot to disconnect you can not connect any longer. If that is the case please restart the fan by unplugging and replugging it. You can disconnect like this:
```java
dyson.disconnect();
```

## Bugs - Contact
If you found a bug, have questions or want to message me write an email to dev(at)chriis.de. Thank you!

