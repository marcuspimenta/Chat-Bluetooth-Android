package com.chat.bluetooth.activity;

import android.app.Activity;

/**
 * 
 * @author Marcus Pimenta
 * @email mvinicius.pimenta@gmail.com
 * @date 19:47:09 05/05/2013
 */
public abstract class GenericActivity extends Activity{
	
	public static final String TAG = "Chat Bluetooth";
	
	public abstract void settingsAttributes();
	
	public abstract void settingsView();

}