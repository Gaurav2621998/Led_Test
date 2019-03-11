
package com.gourav.led_test;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.stsdemo.test.BleLedCmd;
import com.stsdemo.test.BleLedDeviceNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;
import static java.sql.Types.NULL;

/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
public class DeviceScanActivity extends ListActivity {

	public static int onlyOnce=0;
	
	
	public static final boolean BLE_CONTROLLER_MODE = true;
	public static final boolean BLE_SERVER_MODE = false;
	private static final int REQUEST_ENABLE_BT = 1;
	private static final long SCAN_PERIOD = 5000;
	protected static final String Con_Mode = "ControllingMode";
	public static String ControllingMode = "";

	String sRootBdAddr;

	
	private LeDeviceListAdapter mLeDeviceListAdapter;
	public BluetoothAdapter bluetoothAdapter;
	public Scanner scanner;

	public boolean mScanning;
	public static String RootAddress = "";
	private String RootDeviceName = "";
	private static boolean mBleApplicationMode = BLE_CONTROLLER_MODE;
	public static String ref_Address="";
	public static String node_Address="";

	public Messenger mServiceMsgr;

	public List<String>AddressList=new ArrayList<>();

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getActionBar().setTitle("Scan Activity");
		
		String sApplicationId = getApplication().getPackageName();
		ControllingMode = this.getIntent().getStringExtra(Con_Mode);
		
		if (sApplicationId != null) {
			// if (sApplicationId.toLowerCase().compareTo(
			// BLE_CONTROLLER_APP_ID.toLowerCase()) == 0) {
			mBleApplicationMode = BLE_CONTROLLER_MODE;
			
		}

		
			setTitle("Scan Activity");




		// Use this check to determine whether BLE is supported on the device.
		// Then you can selectively disable BLE-related features.
		if (!getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)) {
//			Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_LONG)
//					.show();
			finish();
		}

		// Initializes a Bluetooth adapter. For API level 18 and above, get a
		// reference to
		// BluetoothAdapter through BluetoothManager.
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		bluetoothAdapter = bluetoothManager.getAdapter();

		// Checks if Bluetooth is supported on the device.
		if (bluetoothAdapter == null) {
//			Toast.makeText(this, R.string.error_bluetooth_not_supported,
//					Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

	}

	public static boolean getAppMode() {
		return mBleApplicationMode;
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.gatt_scan, menu);
//		if (scanner == null || !scanner.isScanning()) {
//			menu.findItem(R.id.menu_stop).setVisible(false);
//			menu.findItem(R.id.menu_scan).setVisible(true);
//			menu.findItem(R.id.menu_log).setVisible(true);
//		//	menu.findItem(R.id.menu_refresh).setActionView(null);
//		} else {
//			menu.findItem(R.id.menu_stop).setVisible(true);
//			menu.findItem(R.id.menu_scan).setVisible(false);
//			menu.findItem(R.id.menu_log).setVisible(true);
////			menu.findItem(R.id.menu_refresh).setActionView(
////					R.layout.actionbar_indeterminate_progress);
//		}
//		getActionBar().setBackgroundDrawable(
//				new ColorDrawable(Color.rgb(3, 169, 244)));
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case R.id.menu_scan:
//			mLeDeviceListAdapter.clear();
//			if (scanner == null) {
//				scanner = new Scanner(bluetoothAdapter, mLeScanCallback);
//				mLeDeviceListAdapter.ClearNodeMode();
//				scanner.startScanning();
//				invalidateOptionsMenu();
//			}
//			break;
//		case R.id.menu_stop:
//			if (scanner != null) {
//				scanner.stopScanning();
//				scanner = null;
//
//				invalidateOptionsMenu();
//			}
//			break;
//		}
//		if (item.getItemId() == R.id.menu_log) {
//			Intent details = new Intent(DeviceScanActivity.this, ConnectionDetails.class);
//			startActivity(details);
//			}
//
//		return true;
//	}

	@Override
	protected void onResume() {
		super.onResume();

		// Ensures Bluetooth is enabled on the device. If Bluetooth is not
				// currently enabled,
				// fire an intent to display a dialog asking the user to grant
				// permission to enable it.
				if (!bluetoothAdapter.isEnabled()) {
					if (!bluetoothAdapter.isEnabled()) {
						Intent enableBtIntent = new Intent(
								BluetoothAdapter.ACTION_REQUEST_ENABLE);
						startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
					}
				}

				// Initializes list view adapter.
				mLeDeviceListAdapter = new LeDeviceListAdapter();
				setListAdapter(mLeDeviceListAdapter);
				if (scanner == null) {
					scanner = new Scanner(bluetoothAdapter, mLeScanCallback);
					scanner.startScanning();

				}

				invalidateOptionsMenu();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

				if (requestCode == REQUEST_ENABLE_BT
						&& resultCode == Activity.RESULT_CANCELED) {
					finish();
					return;
				}
				super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (scanner != null) {
			scanner.stopScanning();
			scanner = null;
			mLeDeviceListAdapter.clear();
			mLeDeviceListAdapter.ClearNodeMode();
		}
	}

	/**
	 * An event in case activity is canceled
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public static String getCmode(){
		return ControllingMode;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);

		Intent intent = new Intent(this, DeviceDetails.class);
		intent.putExtra("Address",
				device.getAddress());
		intent.putStringArrayListExtra("devicelist", (ArrayList<String>) AddressList);

		startActivity(intent);

		if (device == null) {

            // if User select Manual Control....
//		if (ControllingMode.equals("Hand")) {
//			ref_Address = device.getAddress();
////			final Intent intent1 = new Intent(this, NodeTreeViewActivity.class);
////			//if (RootDeviceName.equals("")) {
////				intent1.putExtra(NodeTreeViewActivity.EXTRAS_DEVICE_NAME,
////						device.getName());
////				intent1.putExtra(NodeTreeViewActivity.EXTRAS_DEVICE_ADDRESS,
////						device.getAddress());
////			//} else {
//				//intent1.putExtra(NodeTreeViewActivity.EXTRAS_DEVICE_NAME,
//				//		RootDeviceName);
//				//intent1.putExtra(NodeTreeViewActivity.EXTRAS_DEVICE_ADDRESS,
//				//		RootAddress);
//			//}
//			//if (RootAddress.equals("")) {
//				if (scanner == null) {
//					scanner = new Scanner(bluetoothAdapter, mLeScanCallback);
//					scanner.stopScanning();
//
//				}
//				//	sendBleLedCommand(device.getAddress());
//			//} else {
//			//	sendBleLedCommand(RootAddress);
//		//	}
//
//			// to a detailed setting screen.
//		//	startActivity(intent1);
//
//		}


            //sendBleLedCommand(sRootBdAddr, BleLedCmd.Svr2Node2);
        }


	}




    private void init() {
//		if (mLeDeviceListAdapter == null) {
//			leDeviceListAdapter = new BleDevicesAdapter(getBaseContext());
//			setListAdapter(leDeviceListAdapter);
//		}

		if (scanner == null) {
			scanner = new Scanner(bluetoothAdapter, mLeScanCallback);
			scanner.startScanning();

		}

		invalidateOptionsMenu();
	}

	int rssiSize = 15;

	int listrssi[] = new int[rssiSize];
	int listrssiavg[] = new int[rssiSize];
	String listAddress[] = new String[rssiSize];
	String listDeviceName[] = new String[rssiSize];
	int MacCount[] = new int[rssiSize];

	int count = 0;
	int index = 0;
	String DEviceAddress;
	String DEvicename;
	int Devicecount = 0;
	boolean Added = false;
	int position = 0;

	public BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

		public void onLeScan(final BluetoothDevice device, final int rssi,
                             final byte[] scanRecord) {
			runOnUiThread(new Runnable() {

				public void run() {

					String Address = device.getAddress();
					String sAddr[] = Address.split(":");

					//Toast.makeText(DeviceScanActivity.this, sAddr[0], Toast.LENGTH_SHORT).show();


					for (int j = 0; j < sAddr.length; j++) {

						if (sAddr[0].equals("00"))

						{
                           // Toast.makeText(DeviceScanActivity.this, sAddr[0] + rssi, Toast.LENGTH_SHORT).show();
							count++;
								if(!(AddressList.contains(device.getAddress().toString()))) {
									AddressList.add(device.getAddress().toString());
								}
								mLeDeviceListAdapter.addDevice(device, rssi);
								mLeDeviceListAdapter
										.AddNodeMode(
												device.getAddress(),
												rssi,
												scanRecord[27]);
								//Toast.makeText(DeviceScanActivity.this, "scanrecord : "+String.valueOf(scanRecord[27]), Toast.LENGTH_SHORT).show();
								mLeDeviceListAdapter.notifyDataSetChanged();
								//boolean bCanSelectRoot = false;



						
							Added = false;
							for (int i = 0; i < Devicecount; i++) {
								if (listAddress[i].equals(device.getAddress())) {
									Added = true;
									position = i;
									break;
								}
							}
							if (Added == false) {
								listrssi[Devicecount] = rssi;
								listAddress[Devicecount] = device.getAddress();
								listDeviceName[Devicecount] = device.getName();
								MacCount[Devicecount]++;
								Devicecount++;
							} else {
								if (position < 10) {
									MacCount[position]++;
									listrssi[position] += rssi;
								}
							}

							if (count >= 10) {

								count = 0;
								int lowest_index = 0;
								if (Devicecount > 1) {
									for (int i = 0; i < Devicecount; i++) {
										// System.out.println("Devicecount  =" +
										// Devicecount);
										// System.out.println("MacCount  =" +
										// MacCount[i]);
										listrssiavg[i] = (listrssi[i] / MacCount[i]);
										// System.out.println(listDeviceName[i]);
										// System.out.println(listrssiavg[i]);

									}
									for (int i = 0; i < Devicecount - 1; i++) {
										if (listrssiavg[i] > listrssiavg[i + 1]) {
											lowest_index = i;
										} else {
											lowest_index = i + 1;
										}
									}
								} else {
									lowest_index = 0;
								}

								// if user select Walk Mode....

							//	if (ControllingMode.equals("Walk"))
								{

									//Toast.makeText(DeviceScanActivity.this, "Walk", Toast.LENGTH_SHORT).show();
//									final Intent intent = new Intent(
//											DeviceScanActivity.this,
//											NodeTreeViewActivity.class);
//
//									//if (RootDeviceName.equals("")) {
//										intent.putExtra(
//												NodeTreeViewActivity.EXTRAS_DEVICE_NAME,
//												listDeviceName[lowest_index]);
//										intent.putExtra(
//												NodeTreeViewActivity.EXTRAS_DEVICE_ADDRESS,
//												listAddress[lowest_index]);
//								//	}


									//if (RootAddress.equals("")) {
									//	sendBleLedCommand(device.getAddress());
										ref_Address = device.getAddress();
									//} else {
									//	sendBleLedCommand(RootAddress);
									//}

									// A tablet changes to NodeTreeView activity
									// and a smart phone changes
									// to a detailed setting screen.
									DEvicename = null;
									DEviceAddress = null;
							//		startActivity(intent);
								}

							}

						}
						break;
					}
				}
			});
		}
	};

	
	
	/**
	 * The operation definition class of ListView (device name,
	 * BluetoothAddress)
	 */
	private class LeDeviceListAdapter extends BaseAdapter {
		private ArrayList<BluetoothDevice> mLeDevices;
		private LayoutInflater mInflator;

		private HashMap<String, LeDeviceMode> mNodeList = new HashMap<String, LeDeviceMode>();

		/**
		 * constractor of ListView
		 */
		public LeDeviceListAdapter() {
			super();
			mLeDevices = new ArrayList<BluetoothDevice>();
			mInflator = DeviceScanActivity.this.getLayoutInflater();
		}

		/**
		 * The device is registered to ListView.
		 * 
		 * @Param device The BluetoothLE device registered
		 */
		public void addDevice(BluetoothDevice device, int rssi) {
			if (!mLeDevices.contains(device)) {
				mLeDevices.add(device);
			}
		}

		public void AddNodeMode(String srcBdAddr, int srcRssi, byte srcMode) {
			LeDeviceMode mNodeDat = new LeDeviceMode(srcRssi, srcMode);
			mNodeList.put(srcBdAddr, mNodeDat);
		}

		public void ClearNodeMode() {
			mNodeList.clear();
		}

		/**
		 * The information on a BluetoothLE device is got from ListView.
		 * 
		 * @Param position The position of the item selected by ListView
		 * @return BluetoothDevice Information on a BluetoothLE device
		 */
		public BluetoothDevice getDevice(int position) {
			return mLeDevices.get(position);
		}

		/**
		 * ListView cleared
		 */
		public void clear() {
			mLeDevices.clear();
		}

		/**
		 * The number of items in ListView is returned.
		 * 
		 * @return The number of items in ListView
		 */
		public int getCount() {
			return mLeDevices.size();
		}

		/**
		 * The number of items in ListView is returned. return The number of
		 * items in ListView
		 */
		public Object getItem(int i) {
			return mLeDevices.get(i);
		}

		/**
		 * ID in ListView is returned.
		 * 
		 * @param i
		 *            Item ID
		 * @return Item ID
		 */
		public long getItemId(int i) {
			return i;
		}

		/**
		 * The information on View in ListView is got.
		 * 
		 * @param i
		 *            The position of the selected item
		 * @param view
		 *            Selected View
		 * @param viewGroup
		 *            View Groupï¼ˆDevice nameã€�BluetoothAddressï¼‰
		 * @return Selected View
		 */
		public View getView(int i, View view, ViewGroup viewGroup) {
			ViewHolder viewHolder;
			// General ListView optimization code.

			if (view == null) {
				view = mInflator.inflate(R.layout.activity_scan_dev, null);
				viewHolder = new ViewHolder();
				viewHolder.deviceAddress = (TextView) view
						.findViewById(R.id.device_address);
				viewHolder.deviceName = (TextView) view
						.findViewById(R.id.device_name);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}

			BluetoothDevice device = mLeDevices.get(i);

			String sModeTitle = "";
			//Toast.makeText(DeviceScanActivity.this, device.getAddress().toString(), Toast.LENGTH_SHORT).show();
			LeDeviceMode leNodeDev = mNodeList.get(device.getAddress());
			if (leNodeDev != null) {
				if ((leNodeDev.getmMode() & 64) != 0) {
					sModeTitle = sModeTitle + "     RootNode" + String.valueOf(leNodeDev.getmMode());
					sRootBdAddr=device.getAddress();
					//sendBleLedCommand(sRootBdAddr, BleLedCmd.Svr2Node2);
					RootDeviceName = device.getName();
					RootAddress = device.getAddress();


					//BluetoothGatt bluetoothGatt=device.connectGatt(this,false,gat)
				}
//				else if ((leNodeDev.getmMode() & NULL) != 0) {
//					sModeTitle = sModeTitle + "     Node" + String.valueOf(leNodeDev.getmMode());
//					node_Address = device.getAddress();
//				}
				else
				{
					sModeTitle = sModeTitle + "     Node" + String.valueOf(leNodeDev.getmMode());
					node_Address = device.getAddress();

				}

			}

			final String deviceName = device.getName();
			if (deviceName != null && deviceName.length() > 0)
				viewHolder.deviceName.setText(deviceName + sModeTitle);
			else
				viewHolder.deviceName.setText("Unknown device");
			viewHolder.deviceAddress.setText(device.getAddress());

			return view;
		}
	}
	
	
	/**
	 * The item definition in ListView A Device name and BluetoothAddress are
	 * defined by TextView and contained in the item of ListView by both.
	 */
	static class ViewHolder {
		TextView deviceName;
		TextView deviceAddress;
	}
	
	
	
	private class LeDeviceMode {
		private int mRssi;
		private byte mMode;

		public LeDeviceMode(int srcRssi, byte srcMode) {
			mRssi = srcRssi;
			mMode = srcMode;
		}

		public void setRssi(int srcRssi) {
			mRssi = srcRssi;
		}

		public void setMode(byte srcMode) {
			mRssi = srcMode;
		}

		public int getRssi() {
			return mRssi;
		}

		public byte getmMode() {
			return mMode;
		}

	}
	
	
	
	
	
	
	
	public static class Scanner extends Thread {
		public final BluetoothAdapter bluetoothAdapter;
		public final BluetoothAdapter.LeScanCallback mLeScanCallback;

		public volatile boolean isScanning = false;

		Scanner(BluetoothAdapter adapter,
                BluetoothAdapter.LeScanCallback callback) {
			bluetoothAdapter = adapter;
			mLeScanCallback = callback;

		}

		public boolean isScanning() {
			return isScanning;
		}

		public void startScanning() {
			synchronized (this) {
				isScanning = true;
				start();
			}
		}

		public void stopScanning() {
			synchronized (this) {
				isScanning = false;
				bluetoothAdapter.stopLeScan(mLeScanCallback);
			}
		}

		@Override
		public void run() {
			try {
				while (true) {
					synchronized (this) {
						if (!isScanning)
							break;

						bluetoothAdapter.startLeScan(mLeScanCallback);
					}

					sleep(SCAN_PERIOD);

					synchronized (this) {
						bluetoothAdapter.stopLeScan(mLeScanCallback);
					}
				}
			} catch (InterruptedException ignore) {
			} finally {
				bluetoothAdapter.stopLeScan(mLeScanCallback);
			}
		}
	}






}