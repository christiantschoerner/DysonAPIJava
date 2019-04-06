# DysonAPIJava
Simple Library to control Dyson Fans with Java
Supported Devices:
- Dyson Pure Cool™ (475)
- More coming

## Import to project
1) Download Jarfile with all dependencies included [here](https://drive.google.com/uc?export=download&id=1qwacjVMhxpuMV_Xe_lWYlEAdlghxft8c)
2) Maven -> Coming soon!

## How to use
1) Get the serial number of your device. It is in the format XXX-XX-XXXXXXXX. You can find it at the sticker of your fan or in the app:
![alt text](https://i.ibb.co/ZGVMwfJ/github.jpg)
2) Get the WIFI code from the sticker or the manual of your fan
3) Hash the password using [this](https://pastebin.com/raw/Sv89m4jj) python script. Thanks to [mecks52](https://github.com/mecks52/openhab2-dyson475/blob/master/getPwdHash.py)
4) Get the local IP address of your fan
5) Start coding. Create an object of your fan and connect to it
´DysonDevice475 dyson = new DysonDevice475("SERIALNUMBER", "IPADDRESS", "HASHED PASSWORD");
dyson.connect();´


