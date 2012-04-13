Mame Controller (Server)
========================

How to connect
--------------
The server will throw up a window in the bottom right of the screen (always on top) unless --nogui is specified as a startup option. This gui will specify the address and port the server is currently listening on, but more importantly, display a QR code which allows people to connect. The URL it goes to is http://mc.serwylo.com/?s=ipaddress:port. It could just as easily navigate to a custom scheme like "mame-controller://run" which would be preferrable for many reasons. For example, this will only give the option to open with the mame controller client on your mobile, instead of also having the option of opening in a browser. The issue is, when the controller is not installed, then we also want to be able to navigate them to the correctitem in the Google Play store. 

http://mc.serwylo.com will give you the option of going to the controller in addition to web browsers. If the controller is installed, you should choose the controller app and make it default to always opening with this. If, however, the app is not yet installed, opening http://mc.serwylo.com in the browser will redirect you to the appropriate place in the market.

The alternative to using the http:// scheme would be to have two QR codes, one for installing the controller, and the other for connecting. This may be desirable, and perhaps can be switched over to this if this current solution becomes unbearable.
