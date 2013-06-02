package com.chat.bluetooth.manager;

import android.bluetooth.BluetoothAdapter;

/**
 * 
 * @author Marcus Pimenta
 * @email mvinicius.pimenta@gmail.com
 * @date 15:24:12 01/06/2013
 */
public class BluetoothManager {
	
	private BluetoothAdapter bluetoothAdapter;
	
	public BluetoothManager(){
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); 
	}
	
	public BluetoothAdapter getBluetoothAdapter(){
		return bluetoothAdapter;
	}

	public boolean verifySuportedBluetooth(){
		return (bluetoothAdapter != null) ? true : false;
	}
	
	public boolean isEnabledBluetooth(){
		return bluetoothAdapter.isEnabled();
	}
	
}