package com.chat.bluetooth.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.chat.bluetooth.communication.BluetoothClient;

/**
 * 
 * @author Marcus Pimenta
 * @email mvinicius.pimenta@gmail.com
 * 01/11/2012 13:17:20 
 */
public class BluetoothClientTask extends AsyncTask<BluetoothDevice, Void, BluetoothSocket>{

	private Handler handler;
	private Message message;
	
	private Activity activity;
	private ProgressDialog progressDialog;
	
	private BluetoothClient bluetoothClient;
	
	public BluetoothClientTask(Activity activity, Handler handler, int what){
		this.activity = activity;
		this.handler = handler;
		
		message = new Message();
		message.what = what;
		
		bluetoothClient = new BluetoothClient();
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = ProgressDialog.show(activity, "Aguarde", "Conectando com o dispositivo selecionado...");
	}
	
	@Override
	protected BluetoothSocket doInBackground(BluetoothDevice... devices) {
		return bluetoothClient.conectedBluetooth(devices[0]);
	}
	
	@Override
	protected void onPostExecute(BluetoothSocket result) {
		super.onPostExecute(result);
		
		closeDialog();
		
		message.obj = result;
		handler.dispatchMessage(message);
	}
	
	private void closeDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

}	