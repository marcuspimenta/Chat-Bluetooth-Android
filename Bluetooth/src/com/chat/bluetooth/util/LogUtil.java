package com.chat.bluetooth.util;

import android.util.Log;

import com.chat.bluetooth.activity.GenericActivity;

/**
 * 
 * @author Marcus Pimenta
 * @email mvinicius.pimenta@gmail.com
 * @date 18:05:06 11/03/2013
 */
public class LogUtil {
	
	public static void d(String message) {
		d(message, null);
	}

	public static void d(String message, Throwable e) {
		Log.d(GenericActivity.TAG, message, e);
	}

	public static void i(String message) {
		i(message, null);
	}

	public static void i(String message, Throwable e) {
		Log.i(GenericActivity.TAG, message, e);
	}

	public static void w(String message) {
		w(message, null);
	}

	public static void w(String message, Throwable e) {
		Log.w(GenericActivity.TAG, message, e);
	}

	public static void e(String message) {
		e(message, null);
	}

	public static void e(String message, Throwable e) {
		Log.e(GenericActivity.TAG, message, e);
	}

}