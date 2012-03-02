# TCP server example
import socket

server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind( ( "", 8282 ) )
server_socket.listen( 5 )

print "TCPServer Waiting for client on port 8282"

while 1:
	client_socket, address = server_socket.accept()
	print "I got a connection from ", address
	while 1:
		data = client_socket.recv( 512 )
		print "RECIEVED:" , data
		if ( data == "Close" ):
			client_socket.close()
			break;
