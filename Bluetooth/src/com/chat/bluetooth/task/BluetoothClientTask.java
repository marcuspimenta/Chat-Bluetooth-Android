package com.chat.bluetooth.task;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;

import com.chat.bluetooth.R;
import com.chat.bluetooth.business.IBusinessLogic.OnConnectionBluetoothListener;
import com.chat.bluetooth.communication.BluetoothClient;
import com.chat.bluetooth.util.ToastUtil;

/**
 * 
 * @author Marcus Pimenta
 * @email mvinicius.pimenta@gmail.com
 * 01/11/2012 13:17:20 
 */
public class BluetoothClientTask extends AsyncTask<BluetoothDevice, Void, BluetoothSocket>{

	private Context context;
	private ProgressDialog progressDialog;
	
	private ToastUtil toastUtil;
	private BluetoothClient bluetoothClient;
	private OnConnectionBluetoothListener onBluetoothListener;
	
	public BluetoothClientTask(Context context, OnConnectionBluetoothListener onBluetoothListener){
		this.context = context;
		this.onBluetoothListener = onBluetoothListener;
		
		toastUtil = new ToastUtil(context);
		bluetoothClient = new BluetoothClient();
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = ProgressDialog.show(context, 
											 context.getText(R.string.waiting), 
											 context.getText(R.string.msg_connecting_bluetooth));
	}
	
	@Override
	protected BluetoothSocket doInBackground(BluetoothDevice... devices) {
		return bluetoothClient.conectedBluetooth(devices[0]);
	}
	
	@Override
	protected void onPostExecute(BluetoothSocket bluetoothSocket) {
		super.onPostExecute(bluetoothSocket);
		
		closeDialog();
		
		if(bluetoothSocket != null){
			onBluetoothListener.onConnectionBluetooth(bluetoothSocket);
		}else{
			toastUtil.showToast("Falha na conexão");
		}
	}
	
	private void closeDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

}	