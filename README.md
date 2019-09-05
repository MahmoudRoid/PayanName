# Improving performance of Android smartphones cpus

## Prerequirements

### CyanogenMod

To have a full controll on smartphone, Installig custom rom like CyanogenMod is required.
Installation Instructions :

1) Download your proper version of CyanogenMod ( ROM )
2) Intsalling Recovery (i.e. TWRP)
3) Place ROM in your SD card or Internal Storage
4) Crack and open bootloader
5) Reboot into recovery mode
6) Full wipe and factory reset (recommended)
7) Back to main menu
8) Flash ROM
9) Reboot

After reboot go to Setting -> Developer Options -> toggle on root option.

## Programming Languages Used
First of all, this project was written in Java as Primary android programing language. Following Google's official support of Kotlin's 	programming language, the algorithm was overwritten with this new language.
Since the Algorithm works with linux kernel and casue of C++ programming language speed, the algorithm was overwritten again.
Speed of C++ helped us to achieve better performance and have a great results at benchmarks.
In order to use C++ as android developing language, NDK which is a tool for compiling the codes to run on adnroid OS, is required. Another tools that should be downloaded in SDK are CMAKE and LLDB. Next step is configuring gradle. add the following code into your gradle.

cmake {
cppFlags ""
            arguments "-DANDROID_STL=c++_static"
            }

You can use C++ in your application now.

## Challenges
### Cpu Temperature
There are different system files to achieve cpu temperature:

1) sys/class/thermal/thermal_zone0/temp
2) sys/devices/virtual/thermal/thermal_zone0/temp
3) sys/class/hwmon/hwmonX/temp1_input

You should notice that which thermal zone is relevant to temperature of cpu. so yoy should read all thermal_zones type. The paths is shown blow.

sys/class/thermal/thermal_zone0/type
sys/class/thermal/thermal_zone1/type
sys/class/thermal/thermal_zone2/type
.............
.............


### Cpu Utilization
            
            



