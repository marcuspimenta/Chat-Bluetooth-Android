package com.chat.bluetooth.communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;

import com.chat.bluetooth.R;
import com.chat.bluetooth.activity.ChatActivity;
import com.chat.bluetooth.util.LogUtil;

/**
 * 
 * @author Marcus Pimenta
 * @email mvinicius.pimenta@gmail.com
 * 01/11/2012 13:06:15 
 */
public class BluetoothComunication extends Thread {
	 
	private boolean run;
	
	private Context context;
	private Handler handler;
	
	private BluetoothSocket bluetoothSocket;
	private DataInputStream dataInputStream;
	private DataOutputStream dataOutputStream;
	
	public BluetoothComunication(Context context, Handler handler){
		this.context = context;
		this.handler = handler;
		
		run = true;
	}
	
	public void setBluetoothSocket(BluetoothSocket bluetoothSocket){
		this.bluetoothSocket = bluetoothSocket;
	}
	
	@Override
	public void run() {
		 super.run();
		
		 try {
			 String nameBluetooth = bluetoothSocket.getRemoteDevice().getName();
			 dataInputStream = new DataInputStream(bluetoothSocket.getInputStream());
			 dataOutputStream = new DataOutputStream(bluetoothSocket.getOutputStream());
			
			 sendHandler(ChatActivity.MSG_TOAST, context.getString(R.string.connected_sucessfully));
			 
			 while (run) {
				 if(dataInputStream.available() > 0){
					 byte[] msg = new byte[dataInputStream.available()];
					 dataInputStream.read(msg, 0, dataInputStream.available());
					 
					 sendHandler(ChatActivity.MSG_BLUETOOTH, nameBluetooth + ": " + new String(msg));
				 }
			 }
		 }catch (IOException e) {
			 LogUtil.e(e.getMessage());
			 
			 stopComunication();
			 sendHandler(ChatActivity.MSG_TOAST, context.getString(R.string.lost_connection));
		 }
	}
	
	public boolean sendMessageByBluetooth(String msg){
		try {
			if(dataOutputStream != null){
				dataOutputStream.write(msg.getBytes());
				dataOutputStream.flush();
				return true;
			}else{
				sendHandler(ChatActivity.MSG_TOAST, context.getString(R.string.no_connection));
				return false;
			}
		} catch (IOException e) {
			LogUtil.e(e.getMessage());
			
			sendHandler(ChatActivity.MSG_TOAST, context.getString(R.string.failed_to_send_message));
			return false;
		}
	}
	
	public void sendHandler(int what, Object object){
		handler.obtainMessage(what, object).sendToTarget();
	}
           
	 public void stopComunication(){ 
		try {
			run = false;
			
			if(bluetoothSocket != null){
				bluetoothSocket.close();
			}
			
			if(dataInputStream != null && dataOutputStream != null){
				dataInputStream.close();
				dataOutputStream.close();
				
				dataInputStream = null;
				dataOutputStream = null;
			}
		} catch (IOException e) {
			LogUtil.e(e.getMessage());
		}
	 }
	 
 }