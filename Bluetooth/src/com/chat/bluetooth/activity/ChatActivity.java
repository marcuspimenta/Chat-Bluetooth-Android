package com.chat.bluetooth.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.chat.bluetooth.R;
import com.chat.bluetooth.business.ChatBusinessLogic;
import com.chat.bluetooth.util.ToastUtil;

/**
 * 
 * @author Marcus Pimenta
 * @email mvinicius.pimenta@gmail.com
 * 05/10/2012 14:41:34
 */
public class ChatActivity extends GenericActivity{
	
	public static int MSG_TOAST = 1;
	public static int MSG_BLUETOOTH = 2;
	public static int BT_TIMER_VISIBLE = 30; 
	
	private final int BT_ACTIVATE = 0;
	private final int BT_VISIBLE = 1;

	private Button buttonService;
	private Button buttonClient;
	private ImageButton buttonSend;
	private EditText editTextMessage;
	private ListView listVewHistoric;
	private ArrayAdapter<String> historic;
	
	private ToastUtil toastUtil;
	private ChatBusinessLogic chatBusinessLogic;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_activity);
		
		settingsAttributes();
		settingsView();
		
		inicializaBluetooth();
		registerFilters();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_chat_activity, menu);
        return true;
    }
        
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clean:
                historic.clear();
                historic.notifyDataSetChanged();
                break;
        }
        
        return super.onOptionsItemSelected(item);
    }
	
	@Override
	public void settingsAttributes() {
		toastUtil = new ToastUtil(this);
		chatBusinessLogic = new ChatBusinessLogic(this, handler);
	}

	@Override
	public void settingsView() {
		editTextMessage = (EditText)findViewById(R.id.editTextMessage);
		
		historic = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		listVewHistoric = (ListView)findViewById(R.id.listVewHistoric);
		listVewHistoric.setAdapter(historic);
		
		buttonSend = (ImageButton)findViewById(R.id.buttonSend);
		buttonSend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String message = editTextMessage.getText().toString(); 
				
				if(message.trim().length() > 0){
					if(chatBusinessLogic.sendMessage(message)){
						editTextMessage.setText(""); 
						
						historic.add("I: " + message); 
						historic.notifyDataSetChanged();			
					}
				}else{
					toastUtil.showToast(getString(R.string.enter_message));
				}
			}
		});
		
		buttonService = (Button)findViewById(R.id.buttonService);
		buttonService.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, BT_TIMER_VISIBLE); 
				startActivityForResult(discoverableIntent, BT_VISIBLE);
			}
		});
		
		buttonClient = (Button)findViewById(R.id.buttonClient);
		buttonClient.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				chatBusinessLogic.startFoundDevices();
			}
		});
	}
	
	public void inicializaBluetooth() {
		if (chatBusinessLogic.getBluetoothManager().verifySuportedBluetooth()) {
			if (!chatBusinessLogic.getBluetoothManager().isEnabledBluetooth()) { 
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); 
				startActivityForResult(enableBtIntent, BT_ACTIVATE);
			}
		} else {
			toastUtil.showToast(getString(R.string.no_support_bluetooth));
			finish();
		}
	}
	
	public void registerFilters(){
		chatBusinessLogic.registerFilter();
	}

	private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            synchronized (msg) {
                switch (msg.what) {
                	case 1:
                		toastUtil.showToast((String)(msg.obj));
                		break;
                	case 2:
                		historic.add((String)(msg.obj));
       				 	historic.notifyDataSetChanged();
       				 	
       				 	listVewHistoric.requestFocus();
       				 	break;
                }
            }
        };
    };
    
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case BT_ACTIVATE:
				if (RESULT_OK != resultCode) {
					toastUtil.showToast(getString(R.string.activate_bluetooth_to_continue));
					finish(); 
				}
				break;
				
			case BT_VISIBLE:
				if (resultCode == BT_TIMER_VISIBLE) {
					
					chatBusinessLogic.stopCommucanition();
					chatBusinessLogic.startServer();
				} else {
					toastUtil.showToast(getString(R.string.device_must_visible));
				}
				break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		chatBusinessLogic.unregisterFilter();
		chatBusinessLogic.stopCommucanition();
	}

}