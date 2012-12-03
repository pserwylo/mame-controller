# Setting up

I am using ArchLinux for the cabinet, because it is very configurable, and starts off extremely light weight. I want to ensure as little cruft as possible, to maximise boot times. However, you could just as easily use any other OS. These instructuions will asume you are using linux, and therefore that certain software will be available.

## Desktop Environment (DE)

I've used the IceWM. This seems like a pretty damn light weight WM (even more so than Openbox or LXDE). 

https://wiki.archlinux.org/index.php/Beginners_Guide#Install_X (required xf86-video-fbdev for Raspberry PI)
https://wiki.archlinux.org/index.php/IceWM

## Auto Login (and start DE)

https://wiki.archlinux.org/index.php/Automatic_login_to_virtual_console
https://wiki.archlinux.org/index.php/Start_X_at_Login


## Configure DE

Start by copying the default config files fore IceWM:

	https://wiki.archlinux.org/index.php/IceWM#Configuration


## Java errors running server with GUI

I found that when I first ran the server with the minimal installation described above, it would crash. For me it was an issue with OpenJDK and Monospaced fonts.

The default Arch install with a default OpenJDK package from pacman does not support Monospaced fonts in AWT Java applications. To fix this, the package ttf-dejavu has the fonts which are specified by the default OpenJDK fontconfig.properties. Installing this package solves the problem.


# List of commands

## Install relevant software

* pacman -Sy sudo xorg-server xorg-xinit xorg-server-utils icewm

## Setup mame user

* useradd -m -g users -G audio,disk,network,power,storage,video,bin -s /bin/bash mame
* passwd mame
* su mame
* echo "exec icewm-session" >> ~/.xinitrc
* mkdir ~/.icewm
* cp -R /usr/share/icewm/* ~/.icewm
* echo "/usr/bin/wahcade" > ~/.icewm/startup
