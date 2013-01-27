package com.bluetooth.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.bluetooth.R;
import com.bluetooth.manager.BluetoothComunication;
import com.bluetooth.notice.Notice;
import com.bluetooth.task.BluetoothClientTask;
import com.bluetooth.task.BluetoothServiceTask;
import com.google.inject.Inject;

/**
 * 
 * @author Marcus Pimenta
 * @email mvinicius.pimenta@gmail.com
 * 05/10/2012 14:41:34
 */
@SuppressLint("HandlerLeak")
@ContentView(R.layout.main)
public class MainActivity extends RoboActivity{
	
	private final int BT_ACTIVATE = 0;
	private final int BT_VISIBLE = 1;

	public static int BT_TIMER_VISIBLE = 30; 

	@InjectView(R.id.edtMsg)
	private EditText edMsg;
	
	@InjectView(R.id.btnSend)
	private Button btnSend;
	
	@InjectView(R.id.btnService)
	private Button btnService;
	
	@InjectView(R.id.btnClient)
	private Button btnClient;
	
	@InjectView(R.id.lstHistoric)
	private ListView lstHistoric;
	
	@Inject
	private Notice notice;

	private BluetoothSocket socket;
	private BluetoothAdapter adaptador;
	private BluetoothComunication bluetoothComunication;
	private BluetoothClientTask bluetoothClientTask;
	private BluetoothServiceTask bluetoothServiceTask;
	
	private ArrayAdapter<String> historic;
	private List<BluetoothDevice> devicesFound; 

	private ProgressDialog progressDialog;	
	private EventsBluetoothReceiver eventsBTReceiver; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		devicesFound = new ArrayList<BluetoothDevice>(); 

		configView();
		inicializaBluetooth();
		registerFilters();
	}
	
	public void configView(){
		historic = new ArrayAdapter<String>(this,	android.R.layout.simple_list_item_1);
		lstHistoric.setAdapter(historic);
		
		btnSend.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				if(bluetoothComunication != null){
					String msg = edMsg.getText().toString(); 
					
					if(msg.trim().length() > 0){
						edMsg.setText(""); 
						
						bluetoothComunication.sendMessageByBluetooth(msg);
						
						historic.add("Eu: " + msg); 
						historic.notifyDataSetChanged();							
					}else{
						notice.showToast("Escreva alguma mensagem");
					}
				}else{
					notice.showToast("Sem conexão com outro dispositivo");
				}
			}
		});
		
		btnService.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, BT_TIMER_VISIBLE); 
				startActivityForResult(discoverableIntent, BT_VISIBLE);
			}
		});
		
		btnClient.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				progressDialog = ProgressDialog.show(MainActivity.this, "Aguarde", "Procurando dispositivos...");
				
				closeCommunication();
				
				devicesFound.clear(); 
				adaptador.startDiscovery(); 
			}
		});
	}

	public void inicializaBluetooth() {
		adaptador = BluetoothAdapter.getDefaultAdapter(); 
		
		if (adaptador != null) {
			if (!adaptador.isEnabled()) { 
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); 
				startActivityForResult(enableBtIntent, BT_ACTIVATE);
			}
		} else {
			notice.showToast("Aparelho nÃ£o suporta Bluetooth");
			finish();
		}
	}
	
	public void registerFilters(){
		eventsBTReceiver = new EventsBluetoothReceiver(); 

		IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_FOUND); 
		IntentFilter filter2 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED); 

		registerReceiver(eventsBTReceiver, filter1);
		registerReceiver(eventsBTReceiver, filter2);
	}

	private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            synchronized (msg) {
                switch (msg.what) {
                	case 1:
                		socket = (BluetoothSocket)(msg.obj);
                		
                		if(socket != null){
                			bluetoothComunication = new BluetoothComunication(handler, 3, 2);
                			bluetoothComunication.openComunication(socket);
                		}else{
                			notice.showToast("Falha na conexão");
                		}
                		break;
            			
                	case 2:
                		String message = (String)(msg.obj);
                		
                		notice.showToast(message);
                		break;
                	
                	case 3:
                		String messageBT = (String)(msg.obj);
                		
                		historic.add(messageBT);
       				 	historic.notifyDataSetChanged();
       				 	break;
                }
            }
        };
    };
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(eventsBTReceiver);
		
		closeCommunication();
	}
    
    public void closeCommunication(){
    	try {
    		if(socket != null){
    			socket.close();
    			socket = null;
    		}
    		
    		if(bluetoothServiceTask != null){
				bluetoothServiceTask.closeServerSocket();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case BT_ACTIVATE:
				if (RESULT_OK != resultCode) {
					notice.showToast("Você deve ativar o Bluetooth pra continuar");
					finish(); 
				}
				break;
				
			case BT_VISIBLE:
				if (resultCode == BT_TIMER_VISIBLE) {
					closeCommunication();
					
					bluetoothServiceTask = new BluetoothServiceTask(MainActivity.this, adaptador, handler, 1); 
					bluetoothServiceTask.execute();
				} else {
					notice.showToast("Para iniciar o servidor, seu aparelho deve estar visivel");
				}
				break;
		}
	}

	private class EventsBluetoothReceiver extends BroadcastReceiver { 
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) { 
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				devicesFound.add(device); 
			} else{
				if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(intent.getAction())){
					progressDialog.dismiss();
					exibirDispositivosEncontrados();
				}
			}
		}
	}
	
	private void exibirDispositivosEncontrados() { 

		String[] aparelhos = new String[devicesFound.size()]; 

		for (int i = 0; i < devicesFound.size(); i++){
			aparelhos[i] = devicesFound.get(i).getName();
		}
			
		AlertDialog.Builder dialog = new AlertDialog.Builder(this).setTitle("Aparelhos encontrados").setItems(aparelhos, new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				
				bluetoothClientTask = new BluetoothClientTask(MainActivity.this, handler, 1);
				bluetoothClientTask.execute(devicesFound.get(which));

				dialog.dismiss(); 
			}
		});
		dialog.show();
	}
	
}