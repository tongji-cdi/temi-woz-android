# Temi WoZ Tool
A Wizard-of-Oz testing application that enables remote-controlling the actions of the Temi robot. Used in our CHI'21 paper "Patterns for Representing Knowledge Graphs to Communicate Situational Knowledge of Service Robots".

## Running the code
The code should be directly usable by importing it into Android Studio and compiling it into an APK. Then you can install the APK onto Temi by following the [official guide](https://github.com/robotemi/sdk/wiki/Installing-and-Uninstalling-temi-Applications).

In short, you should connect to Temi on your local network and push install the APK by running:
```
adb connect <TEMI_IP_ADDRESS>:5555
adb install [option] PATH_OF_APK
```

## Controlling the robot
...More to come!
