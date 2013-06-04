package com.chat.bluetooth.broadcast;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.chat.bluetooth.R;
import com.chat.bluetooth.business.IBusinessLogic.OnSearchBluetoothListener;
import com.chat.bluetooth.util.ToastUtil;

/**
 * 
 * @author Marcus Pimenta
 * @email mvinicius.pimenta@gmail.com
 * @date 19:51:50 01/06/2013
 */
public class EventsBluetoothReceiver extends BroadcastReceiver { 
	
	private Context context;
	private ProgressDialog progressDialog;
	
	private ToastUtil toastUtil;
	private List<BluetoothDevice> devicesFound; 
	private OnSearchBluetoothListener onSearchBluetoothListener;
	
	public EventsBluetoothReceiver(Context context, OnSearchBluetoothListener onSearchBluetoothListener){
		this.context = context;
		this.onSearchBluetoothListener = onSearchBluetoothListener;
		
		toastUtil = new ToastUtil(context);
		devicesFound = new ArrayList<BluetoothDevice>();
	}
	
	public void registerFilters(){
		context.registerReceiver(this, new IntentFilter(BluetoothDevice.ACTION_FOUND));
		context.registerReceiver(this, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
	}
	
	public void unregisterFilters(){
		context.unregisterReceiver(this);
	}
	
	public void showProgress(){
		devicesFound.clear();
		
		progressDialog = ProgressDialog.show(context, 
											 context.getText(R.string.waiting), 
											 context.getText(R.string.msg_searching_devices));
	}
	
	private void closeProgress() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}
		
	@Override
	public void onReceive(Context context, Intent intent) {
		if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) { 
			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			devicesFound.add(device); 
		} else{
			if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(intent.getAction())){
				closeProgress();
				
				if(devicesFound.size() > 0){
					onSearchBluetoothListener.onSearchBluetooth(devicesFound);
				}else{
					toastUtil.showToast(context.getString(R.string.no_device_found));
				}
			}
		}
	}
	
}