package com.serwylo.mame.controller.client.net.bluetooth;

public class BluetoothEvent 
{

	public static int START_DISCOVERY = 1;
	public static int END_DISCOVERY = 2;
	public static int DEVICE_FOUND = 3;
	
	private int type;
	
	// Used for DEVICE_FOUND events...
	private String deviceName;
	private String deviceAddress;
	
	public int getType()
	{
		return this.type;
	}
	
	public String getDeviceName()
	{
		return this.deviceName;
	}
	
	public String getDeviceAddress()
	{
		return this.deviceAddress;
	}

	public static BluetoothEvent createStartDiscoveryEvent()
	{
		BluetoothEvent event = new BluetoothEvent();
		event.type = START_DISCOVERY;
		return event;
	}
	
	public static BluetoothEvent createEndDiscoveryEvent()
	{
		BluetoothEvent event = new BluetoothEvent();
		event.type = END_DISCOVERY;
		return event;
	}

	public static BluetoothEvent createDeviceFoundEvent( String name, String address )
	{
		BluetoothEvent event = new BluetoothEvent();
		event.type = DEVICE_FOUND;
		event.deviceName = name;
		event.deviceAddress = address;
		return event;
	}
	
}
