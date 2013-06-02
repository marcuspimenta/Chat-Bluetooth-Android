package com.chat.bluetooth.alertdialog;

import android.app.AlertDialog;
import android.content.Context;

import com.chat.bluetooth.R;

/**
 * 
 * @author Marcus Pimenta
 * @email mvinicius.pimenta@gmail.com
 * @date 14:45:57 10/03/2013
 */
public abstract class AlertDialogGeneric {

	protected Context context;
	protected AlertDialog.Builder alert;
	
	public AlertDialogGeneric(Context context) {
		this.context = context;
		
		alert = new AlertDialog.Builder(context);
		alert.setIcon(R.drawable.ic_launcher);
	}
	
	public void showAlertDialog(){
		alert.create().show();
	}
	
	public void cancelAlertDialog(){
		alert.create().cancel();
	}

}