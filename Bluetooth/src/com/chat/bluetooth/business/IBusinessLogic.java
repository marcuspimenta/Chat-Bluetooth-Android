package com.chat.bluetooth.business;

import java.util.List;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

/**
 * 
 * @author Marcus Pimenta
 * @email mvinicius.pimenta@gmail.com
 * @date 20:01:20 05/05/2013
 */
public interface IBusinessLogic {
	
	public interface OnSearchBluetoothListener{
		public abstract void onSearchBluetooth(List<BluetoothDevice> devicesFound);
	}

	public interface OnConnectionBluetoothListener{
		public abstract void onConnectionBluetooth(BluetoothSocket bluetoothSocket);
	}
	
	public interface OnBluetoothDeviceSelectedListener{
		public abstract void onBluetoothDeviceSelected(BluetoothDevice bluetoothDevice);
	}
	
}