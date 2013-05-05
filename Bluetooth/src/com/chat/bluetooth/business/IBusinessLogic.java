package com.chat.bluetooth.business;

import android.bluetooth.BluetoothSocket;

/**
 * 
 * @author Marcus Pimenta
 * @email mvinicius.pimenta@gmail.com
 * @date 20:01:20 05/05/2013
 */
public interface IBusinessLogic {

	public interface OnBluetoothListener{
		public abstract void onConnectionBluetooth(BluetoothSocket bluetoothSocket);
	}
	
}