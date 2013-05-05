package com.chat.bluetooth.communication;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

/**
 * 
 * @author Marcus Pimenta
 * @email mvinicius.pimenta@gmail.com
 * 01/11/2012 13:05:05 
 */
public class BluetoothClient{
	 
	 private final UUID MEU_UUID_PC = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	 public BluetoothSocket conectedBluetooth(BluetoothDevice device) {
		 BluetoothSocket socket = null;
		 
		 try {
			 socket = device.createRfcommSocketToServiceRecord(MEU_UUID_PC);
			 socket.connect();
			 
		 } catch (IOException e) { 
			 e.printStackTrace();
		 }
		 
		 return socket;
	 }
	 
 }