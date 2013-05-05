package com.chat.bluetooth.communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;

/**
 * 
 * @author Marcus Pimenta
 * @email mvinicius.pimenta@gmail.com
 * 01/11/2012 13:06:15 
 */
public class BluetoothComunication extends Thread {
	 
	private int whatMsgBT;
	private int whatMsgNotice;
	
	private Handler handler;
	
	private BluetoothSocket socket;
	private DataInputStream dataInputStream = null;
	private DataOutputStream dataOutputStream = null;
	
	public BluetoothComunication(Handler handler, int whatMsgBT, int whatMsgNotice){
		this.handler = handler;
		this.whatMsgBT = whatMsgBT;
		this.whatMsgNotice = whatMsgNotice;
	}
	
	public void openComunication(BluetoothSocket socket){
		this.socket = socket;
		start();
	}
	 
	@Override
	public void run() {
		 super.run();
		
		 String nameBluetooth;
		
		 try {
			 nameBluetooth = socket.getRemoteDevice().getName();
			 dataInputStream = new DataInputStream(socket.getInputStream());
			 dataOutputStream = new DataOutputStream(socket.getOutputStream());
			
			 sendHandler(whatMsgNotice, "Conexão realizada com sucesso");
			 
			 while (true) {
				 if(dataInputStream.available() > 0){
					 byte[] msg = new byte[dataInputStream.available()];
					 dataInputStream.read(msg, 0, dataInputStream.available());
					 
					 sendHandler(whatMsgBT, nameBluetooth + ": " + new String(msg));
				 }
			 }
		 } catch (IOException e) {
			 e.printStackTrace(); 
			 
			 dataInputStream = null;
			 dataOutputStream = null;
			 
			 sendHandler(whatMsgNotice, "Conexão perdida");
		 }
	}
	
	public void sendMessageByBluetooth(String msg){
		try {
			if(dataOutputStream != null){
				dataOutputStream.write(msg.getBytes());
			}else{
				sendHandler(whatMsgNotice, "Sem conexão");
			}
			
		} catch (IOException e) {
			e.printStackTrace(); 
			 
			sendHandler(whatMsgNotice, "Falha no envio da mensagem");
		}
	}
	
	public void sendHandler(int what, Object object){
		handler.obtainMessage(what, object).sendToTarget();
	}
           
	 public void stopComunication(){ 
		try {
			if(dataInputStream != null && dataOutputStream != null){
				dataInputStream.close();
				dataOutputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	 }
	 
 }