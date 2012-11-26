Setting up
==========

I am using ArchLinux for the cabinet, because it is very configurable, and starts off extremely light weight. I want to ensure as little cruft as possible, to maximise boot times. However, you could just as easily use any other OS. These instructuions will asume you are using linux, and therefore that certain software will be available.

Desktop Environment
-------------------

I've used the IceWM. This seems like a pretty damn light weight WM (even more so than Openbox or LXDE). 

https://wiki.archlinux.org/index.php/Beginners_Guide#Install_X (required xf86-video-fbdev for Raspberry PI)
https://wiki.archlinux.org/index.php/IceWM

Auto Login (and start DE)
------------------------------------------

I use inittab to change the runlevel to 5 (to automatically start X when booting), and then change the login manager to that specified at:

https://wiki.archlinux.org/index.php/Start_X_at_Boot#inittab


Configure DE
------------

Start by copying the default config files fore IceWM:

	https://wiki.archlinux.org/index.php/IceWM#Configuration



List of commands
----------------
* pacman -Syu
* pacman -S sudo
* useradd -m -g users -G audio,disk,network,power,storage,video,bin -s /bin/bash mame
* passwd mame
* pacman -S xorg-server xorg-xinit xorg-server-utils
* pacman -S icewm
* vim /etc/inittab # See Auto Login above...
* su mame
* echo "exec icewm-session" >> ~/.xinitrc
* mkdir ~/.icewm
* cp -R /usr/share/icewm/* ~/.icewm
