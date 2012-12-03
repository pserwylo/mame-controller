# TODO List


## Ability to choose controller from list

Need the ability (on the Android client) for the client to choose which controller they want to use (from a list of all available controller files).

This is the first and easiest way to provide support for different emulators. 
Better support will be described below.


### Allow users to download controllers

It would be cool if the controller definitions from somewhere like http://mc.serwylo.com/controllers.

Then you could introduce new controllers without having to update the package, although it would probably be just as easy to update it at this stage.

This would also require the ability to reference images dynamically, but currently I'm only using resource images compiled into the .apk file.


### Have server send client a controller file upon connect

This may or may not be useful.
I can imagine several qrcodes displayed on screen, each listening on a different port.
Each also would specify a different emulator to execute when connected to.

In this example, it would be great if they could tell the client: "This is the controller I want you to use".

However, it is probably easier and nicer to just have the one server, which fires up something like wahcade, and that deals with the multiple emulators. Otherwise we're just doubling up the work they did for wahcade (i.e. providing support for multiple emulators).


## Different keycodes for multiple clients

Currently, when you have a controller definition file, it only allows you to specify one keycode per button.

Realistically, this should take an array of key codes, where the first item is the keycode for this button given to the first client to connect, and so on.

This will require the clients to be told by the server which client number they are, so that they can assign themselves the appropriate key codes from the controller definition file.


## Support runtime button assets

Instead of just depending on compiled in drawables, it would be far preferable to be able to load an image at runtime, calculate its bounds, and use that as the basis for a button. 

This would mean that anybody could create controller definitions, and it would make it much easier for me.


## Layout out buttons better

I don't want to reinvent the wheel, but some sort of extremely primitive layout manager would be great.
The absolute placement of buttons, even with divice independent pixels, is not doing it for me.

Perhaps just start with some values such as "center", and then move into supporting percentages of the screen size.
