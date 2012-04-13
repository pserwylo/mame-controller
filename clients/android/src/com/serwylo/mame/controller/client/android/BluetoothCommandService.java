package com.serwylo.mame.controller.client.android;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import com.badlogic.gdx.Gdx;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BluetoothCommandService 
{

    // Unique UUID for this application
    // private static final UUID MY_UUID = UUID.fromString("04c6093b-0000-1000-8000-00805f9b34fb");
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    
    private final BluetoothAdapter mAdapter;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private int mState;
    
    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
    
    // Constants that indicate command to computer
    public static final int EXIT_CMD = -1;
    public static final int VOL_UP = 1;
    public static final int VOL_DOWN = 2;
    public static final int MOUSE_MOVE = 3;
    
    /**
     * Constructor. Prepares a new BluetoothChat session.
     */
    public BluetoothCommandService() 
    {
    	mAdapter = BluetoothAdapter.getDefaultAdapter();
    	mState = STATE_NONE;
    }
    
    /**
     * Set the current state of the chat connection
     * @param state  An integer defining the current connection state
     */
    private synchronized void setState( int state ) 
    {
        Gdx.app.log( "BLUETOOTH", "setState() " + mState + " -> " + state );
        mState = state;
    }

    /**
     * Return the current connection state. 
     */
    public synchronized int getState() 
    {
        return mState;
    }
    
    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume() 
     */
    public synchronized void start() 
    {
        Gdx.app.log( "Bluetooth", "start" );

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) 
        {
        	mConnectThread.cancel(); 
        	mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if ( mConnectedThread != null ) 
        {
        	mConnectedThread.cancel(); 
        	mConnectedThread = null;
        }

        setState( STATE_LISTEN );
    }
    
    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     * @param device  The BluetoothDevice to connect
     */
    public synchronized void connect( BluetoothDevice device ) 
    {
    	Gdx.app.log("BLUETOOTH", "connect to: " + device );

        // Cancel any thread attempting to make a connection
        if ( mState == STATE_CONNECTING ) 
        {
            if ( mConnectThread != null ) 
            {
            	mConnectThread.cancel(); 
            	mConnectThread = null;
            }
        }

        // Cancel any thread currently running a connection
        if ( mConnectedThread != null ) 
        {
        	mConnectedThread.cancel(); 
        	mConnectedThread = null;
        }

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread( device );
        mConnectThread.start();
        setState( STATE_CONNECTING );
    }
    
    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     * @param socket  The BluetoothSocket on which the connection was made
     * @param device  The BluetoothDevice that has been connected
     */
    public synchronized void connected( BluetoothSocket socket, BluetoothDevice device ) 
    {
        Gdx.app.log("BLUETOOTH", "connected" );

        // Cancel the thread that completed the connection
        if ( mConnectThread != null ) 
        {
        	mConnectThread.cancel(); 
        	mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if ( mConnectedThread != null ) 
        {
        	mConnectedThread.cancel(); 
        	mConnectedThread = null;
        }

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread( socket );
        mConnectedThread.start();
        
        setState( STATE_CONNECTED );
    }

    /**
     * Stop all threads
     */
    public synchronized void stop() 
    {
        Gdx.app.log( "BLUTOOTH", "stop" );
        if ( mConnectThread != null ) 
        {
        	mConnectThread.cancel(); 
        	mConnectThread = null;
        }
        
        if ( mConnectedThread != null ) 
        {
        	mConnectedThread.cancel(); 
        	mConnectedThread = null;
        }
        
        setState( STATE_NONE );
    }
    
    /**
     * Write to the ConnectedThread in an unsynchronized manner
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write( byte[] out ) {
        
    	// Create temporary object
        ConnectedThread r;
        
        // Synchronize a copy of the ConnectedThread
        synchronized ( this ) 
        {
            if ( mState != STATE_CONNECTED ) 
            {
            	return;
            }
            
            r = mConnectedThread;
        }
        
        // Perform the write unsynchronized
        r.write( out );
    }
    
    public void write( int out ) 
    {
    
    	// Create temporary object
        ConnectedThread r;
        
        // Synchronize a copy of the ConnectedThread
        synchronized ( this ) 
        {
        
        	if ( mState != STATE_CONNECTED ) 
        	{
        		return;
        	}
            r = mConnectedThread;
        
        }
        
        // Perform the write unsynchronized
        r.write( out );
    }
    
    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private void connectionFailed() 
    {
        setState( STATE_LISTEN );
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost() 
    {
    	setState( STATE_LISTEN );
    }
    
    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread 
    {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread( BluetoothDevice device ) 
        {
            mmDevice = device;
            BluetoothSocket tmp = null;

            // Get a BluetoothSocket for a connection with the given BluetoothDevice
            try 
            {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } 
            catch ( IOException e ) 
            {
                Gdx.app.log( "BLUETOOTH", e.getMessage() );
            }
            mmSocket = tmp;
        }

        public void run() 
        {
            Gdx.app.log( "BLUETOOTH", "BEGIN mConnectThread" );
            setName( "ConnectThread" );

            // Always cancel discovery because it will slow down a connection
            mAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try 
            {
                // This is a blocking call and will only return on a
                // successful connection or an exception
            	Gdx.app.log( "BLUETOOTH", "Connecting to bluetooth socket..." );
            	mmSocket.connect();
            	Gdx.app.log( "BLUETOOTH", "Connected." );
            } 
            catch ( IOException e ) 
            {
            	Gdx.app.log( "BLUETOOTH", "Failed to connect to bluetooth socket: " + e.getMessage() );
                connectionFailed();
                
                // Close the socket
                try 
                {
                    mmSocket.close();
                } 
                catch ( IOException e2 ) 
                {
                    Gdx.app.log( "BLUETOOTH", "unable to close() socket during connection failure: " + e2.getMessage() );
                }
                
                // Start the service over to restart listening mode
                BluetoothCommandService.this.start();
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized ( BluetoothCommandService.this ) 
            {
                mConnectThread = null;
            }

            // Start the connected thread
            connected( mmSocket, mmDevice );
        }

        public void cancel() 
        {
            try 
            {
                mmSocket.close();
            } 
            catch ( IOException e ) 
            {
                Gdx.app.log( "BLUETOOTH", "close() of connect socket failed: " + e.getMessage() );
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread 
    {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread( BluetoothSocket socket ) 
        {
            Gdx.app.log( "BLUETOOTH", "create ConnectedThread" );
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try 
            {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } 
            catch ( IOException e ) 
            {
                Gdx.app.log("BLUETOOTH", "temp sockets not created: " + e.getMessage() );
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() 
        {
            Gdx.app.log( "BLUETOOTH", "BEGIN mConnectedThread" );
            byte[] buffer = new byte[1024];
            
            // Keep listening to the InputStream while connected
            while ( true ) 
            {
                try 
                {
                	// Read from the InputStream
                    int bytes = mmInStream.read( buffer );
                } 
                catch ( IOException e ) 
                {
                    Gdx.app.log( "BLUETOOTH", "disconnected: " + e.getMessage() );
                    connectionLost();
                    break;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         * @param buffer  The bytes to write
         */
        public void write(byte[] buffer) 
        {
            try 
            {
                mmOutStream.write( buffer );
            } 
            catch ( IOException e ) 
            {
                Gdx.app.log( "BLUETOOTH", "Exception during write: " + e.getMessage() );
            }
        }
        
        public void write( int out ) 
        {
        	try 
        	{
                mmOutStream.write( out );
            } 
        	catch ( IOException e ) 
            {
                Gdx.app.log( "BLUETOOTH", "Exception during write: " + e.getMessage() );
            }
        }

        public void cancel() 
        {
            try 
            {
            	mmOutStream.write( EXIT_CMD );
                mmSocket.close();
            } 
            catch ( IOException e ) 
            {
                Gdx.app.log( "BLUETOOTH", "close() of connect socket failed: " + e.getMessage() );
            }
        }
    }
}