package com.chat.bluetooth.util;

import android.content.Context;
import android.widget.Toast;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * 
 * @author Marcus Pimenta
 * @email mvinicius.pimenta@gmail.com 
 * 04/10/2012 15:19:14
 */
@Singleton
public class ToastUtil {

	@Inject
	private Context context;

	private Toast toast;

	public void showToast(String mensagem) {
		if (toast != null) {
			toast.setText(mensagem);
		} else {
			toast = Toast.makeText(context, mensagem, Toast.LENGTH_LONG);
		}
		toast.show();
	}

	public void closeToast() {
		if (toast != null)
			toast.cancel();
	}

}