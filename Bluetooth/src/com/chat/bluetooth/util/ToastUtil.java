package com.chat.bluetooth.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 
 * @author Marcus Pimenta
 * @email mvinicius.pimenta@gmail.com 
 * 04/10/2012 15:19:14
 */
public class ToastUtil {

	private Toast toast;
	private Context context;
	
	public ToastUtil(Context context){
		this.context = context;
	}

	public void showToast(String mensagem) {
		if (toast != null) {
			toast.setText(mensagem);
		} else {
			toast = Toast.makeText(context, mensagem, Toast.LENGTH_LONG);
		}
		
		toast.show();
	}

	public void closeToast() {
		if (toast != null){
			toast.cancel();
		}
	}

}