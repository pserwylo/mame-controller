Mame Controller
===============

In a similar vein to other remote control apps for Android/iOS, this will provide a remote control interface for controlling a PC.
The difference is that the UI will specifically be targeted towards playing MAME arcade games.

The clients will run on the phone, and talk to a server which is written in ~~python~~ java and running on the machine that MAME is running on.
The input from the client will be converted to input that the MAME can understand (probably just keyboard keys).
This will be done using the ~~uinput module of the Linux kernel, so it will not be cross platform.~~ java.awt.Robot API so that it will be cross platform.
