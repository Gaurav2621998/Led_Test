package com.gourav.led_test;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.stsdemo.test.BleLedCmd;
import com.stsdemo.test.BleLedDeviceNode;

import static android.content.ContentValues.TAG;

public class DeviceDetails extends AppCompatActivity {

    Button on2,off2,status,on1,off1;
    TextView textView;

    String temp2;

    public int check= 0;

    String temp="";
    String x="";
    public Messenger mServiceMsgr,mJScriptMessenger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);

        String sRootBdAddr=getIntent().getStringExtra("Address");

      //  mJScriptMessenger = new Messenger(new ActivityHandler());

        sendBleLedCommand(sRootBdAddr, BleLedCmd.Svr2Node2);

        String args[]=sRootBdAddr.split(":");
        for(int i=0;i<args.length;i++)
        {

            temp=temp+args[i];

        }

        temp2="00:00:30:98:01:2f";
        String args1[]=temp2.split(":");

        x="";
        for(int i=0;i<args1.length;i++)
        {

            x=x+args1[i];

        }

        on1=(Button)findViewById(R.id.on);
        off1=(Button)findViewById(R.id.off);

        on2=(Button)findViewById(R.id.on2);
        off2=(Button)findViewById(R.id.off2);


        status=(Button)findViewById(R.id.showstatus);
        textView=(TextView)findViewById(R.id.status);

        on1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLedStatus(temp,"FF0043","1","0","0","2");
//                statuschange(x,"1",50);
//                statuschange("000030B7012F","1",100);

//                Handler handler=new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        setLedStatus(x,"FF0043","1","0","0","2");
//                        Handler handler1=new Handler();
//                        handler1.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                setLedStatus("000030B9012F","FFFFFF","1","0","0","15");
//
//                            }
//
//
//                        },20);
//                    }
//
//
//                },20);
//              // sendBleLedCommand(temp2, BleLedCmd.Svr2Node2);

//                setLedStatus("000030B9012F","FFFFFF","1","0","0","15");



            }


        });




        off1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLedStatus(temp,"FFFFFF","0","0","0","15");
//                statuschange(x,"0",50);
//                statuschange("000030B7012F","0",100);
                //sendBleLedCommand(temp2, BleLedCmd.Svr2Node2);
//                Handler handler=new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        setLedStatus(x,"FFFFFF","0","0","0","15");
//                        Handler handler1=new Handler();
//
//                        handler1.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                setLedStatus("000030B9012F","FFFFFF","0","0","0","15");
//
//                            }
//                        },20);
//
//                    }
//                },20);
//
            }});





        on2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check=1;

////                unbindService(mServiceConnection);
////                unregisterReceiver(mAppBroadcastReceiver);
//                registerReceiver(mAppBroadcastReceiver,
//                        makeAppUpdateIntentFilter());





//                Intent intent = new Intent("toshiba.led_ble_controler.ACTION_NODE_TREE");
//                sendBroadcast(intent);

               // sendBleLedCommand(temp2, BleLedCmd.Svr2Node2);
                setLedStatus(x,"FF0043","1","0","0","2");



            }


        });

        off2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //sendBleLedCommand(temp2, BleLedCmd.Svr2Node2);
                setLedStatus(x,"FFFFFF","0","0","0","15");

            }
        });


        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    getDevSts(temp);
            }
        });

    }

    public void statuschange(final String addr, final String onOff,int time)
    {
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setLedStatus(addr,"FFFFFF",onOff,"0","0","15");

            }
        },time);

    }

    public void getDevSts(String sBdAddr) {
        try {
            BleLedCmd mBleLedCmd = new BleLedCmd();


            Message msg = Message.obtain(null, BleGattService.MSG_GET_DEV_STS, 0,0);
            msg.replyTo = mJScriptMessenger;

            Bundle bundle = new Bundle();

            bundle.putString("TargetBdAddr",sBdAddr);
            msg.setData(bundle);

            mServiceMsgr.send(msg);
        } catch(RemoteException e) {
            Log.d(TAG, e.toString());
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
    }
    

    @Override
    protected void onPause() {

        super.onPause();
        // Broadcast in an application is validated.
        this.unregisterReceiver(mAppBroadcastReceiver);

        super.onPause();
    }

    @Override
    protected void onResume() {

        this.registerReceiver(mAppBroadcastReceiver,
                makeAppUpdateIntentFilter());

        super.onResume();
    }



    private final BroadcastReceiver mAppBroadcastReceiver = new BroadcastReceiver() {
        /*
         * The event at the time of broadcaster reception
         *
         * @param context The context of Java application
         *
         * @Param intent Activity of the screen displayed
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BleGattService.ACTION_NODE_TREE.equals(action)) {

                String data=intent.getStringExtra(BleGattService.ACTION_NODE_TREE);
                //if(textView.getText().equals("Status")) {
                    textView.setText(data);

//                    if(check==1)
//                    {
//                        setLedStatus(temp2,"FF0043","1","0","0","2");
//
//                    }
//				mNodeTreeMap = intent
//						.getStringExtra(BleGattService.ACTION_NODE_TREE);
                if (BleGattService.ACTION_NODE_DISCONNECT.equals("0")) {
                    Toast.makeText(DeviceDetails.this, "Disconnect",
                            Toast.LENGTH_SHORT).show();
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					Message msg = Message.obtain(null,
//							BleGattService.MSG_GATTSERVER_STOP);
//					msg.replyTo = mServiceMsgr;
//					try {
//						mServiceMsgr.send(msg);
//					} catch (RemoteException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//
//					Intent i = new Intent(NodeTreeViewActivity.this,
//							DeviceScanActivity.class);
//
//					startActivity(i);
                    finish();

                } else {
                    // mWebView.loadUrl("javascript:redrawTreeNode();");
                    //openWebView();
                }
            }
        }
    };

    private static IntentFilter makeAppUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BleGattService.ACTION_NODE_TREE);
        return intentFilter;
    }



    public void sendBleLedCommand(String sBdAddr, byte bCmd) {

        // mBleGattService.mBluetoothDeviceAddress = sBdAddr;
        byte bSndData[] = {};

        BleLedCmd mBleLedCmd = new BleLedCmd();

        if (bCmd == BleLedCmd.Svr2Node2) {
            // In the case of a tablet, a node designation command is sent.
            bSndData = mBleLedCmd.MakeCommand(BleLedCmd.Svr2Node2, null,
                    (byte) 0, (short) 0, "FFFFFF");

        }

        else {

            bSndData = mBleLedCmd.MakeCommand(BleLedCmd.Svr2Node3, null,
                    (byte) 0, (short) 0, "FFFFFF");
        }

        Intent gattServiceIntent = new Intent(this, BleGattService.class);

        gattServiceIntent.putExtra(BleGattService.EXTRAS_BD_ADDR, sBdAddr);
        gattServiceIntent.putExtra(BleGattService.EXTRAS_SVR_CMD, bSndData);

        boolean bRet = bindService(gattServiceIntent, mServiceConnection,
                BIND_AUTO_CREATE);

        // bRet = mBleGattService.gattWriteCommand(bSndData);
    }


    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        /**
         * In order to do messaging to GATT service, a messenger is generated at
         * the time of the GATT service bind. GATT service is required by
         * messenger to connect of initialization and BluetoothLE GATT after
         * messenger generation.
         *
         * @param componentName
         *            The name of GATT service
         * @param service
         *            A binder with GATT service
         */
        public void onServiceConnected(ComponentName componentName,
                                       IBinder service) {
            // mBleGattService = ((BleGattService.LocalBinder)
            // service).getService();
            mServiceMsgr = new Messenger(service);
//
            Toast.makeText(DeviceDetails.this, ""+componentName, Toast.LENGTH_SHORT).show();
           try {
                Message msg = Message.obtain(null,
                        BleGattService.MSG_START_INIT);
                msg.replyTo = mServiceMsgr;
                mServiceMsgr.send(msg);

                msg = Message.obtain(null, BleGattService.MSG_START_CONNECT);
                msg.replyTo = mServiceMsgr;
                mServiceMsgr.send(msg);
            } catch (RemoteException e) {
                Log.d(TAG, e.toString());
            }
        }

        /**
         * A messenger is closed at the time of disconnection of GATT service.
         *
         * @param componentName
         *            The name of GATT service
         */
        public void onServiceDisconnected(ComponentName componentName) {

            mServiceMsgr = null;

            Toast.makeText(DeviceDetails.this, "Service DisConnected", Toast.LENGTH_SHORT).show();
//
        }
    };

    class ActivityHandler extends Handler {
        /**
         * The receiving event of GATT service HTML is redrawn when
         * MSG_TREE_REDRAW is received. When others are received, a message is
         * sent to Javascript and an interface as response of Javascript.
         *
         * @param msg
         *            The message which received from GATT service
         */
        @Override
        public void handleMessage(Message msg) {
            boolean bRet = false;

            if (msg.what == BleGattService.MSG_TREE_REDRAW) {
//				mContollerSettingNode = null;
//				openWebView();
            } else if (msg.what == BleGattService.MSG_SERVER_CLOSE) {
                //closeWebView();
            } else {
                //bRet = mHtmlJavaIF.messageCallback(msg);
                if (bRet == false) {
                    super.handleMessage(msg);
                }
            }
        }
    }

    @Override
    protected void onStop() {
        try {
            if (mServiceMsgr != null) {
                Message msg = Message.obtain(null,
                        BleGattService.MSG_GATTSERVER_STOP);
                msg.replyTo = mServiceMsgr;
                mServiceMsgr.send(msg);
            }
        } catch (RemoteException e) {
            Log.d(TAG, e.toString());
        }
        this.unbindService(mServiceConnection);
        finish();
        super.onStop();
    }

    public void setLedStatus(String sBdAddr, String sLedColor, String sLedOn, String sAutoOn, String sNodeAll, String sDimmer) {
        String args[]=sBdAddr.split(":");
        sBdAddr="";
        for(int i=0;i<args.length;i++)
        {
            sBdAddr=sBdAddr+args[i];
        }

        BleLedCmd mBleLedCmd = new BleLedCmd();
        short iStatus = 0;
        byte bNodeAll = BleLedCmd.TargetSingle;
        short iDimmer = 0x0000;

        Message msg = Message.obtain(null, BleGattService.MSG_SET_DEV_STS, 0,0);
        //msg.replyTo = mActivity.mJScriptMessenger;

        iStatus = BleLedDeviceNode.convertLedColor(sLedColor);

        if(sLedOn.compareTo("1")==0){
            iStatus = (short)(iStatus | BleLedCmd.LED_ON);
        }

        if(sAutoOn.compareTo("1")==0){
            iStatus = (short)(iStatus | BleLedCmd.AUTO_ON);
        }

        if(sNodeAll.compareTo("1")==0) {
            bNodeAll = BleLedCmd.TargetAll;
        }

        // Dimmer
        try {
            iDimmer = (short)(Integer.parseInt(sDimmer));
        } catch(NumberFormatException e) {
            Log.d(TAG, "setLedStatus() Dimmer convert failed." + e.toString());
            iDimmer = 0x0000;
        }
        iStatus = (short)(iStatus | (BleLedCmd.DIMMER_MASK & (iDimmer << 4)));


        byte bCmd[] = mBleLedCmd.MakeCommand(BleLedCmd.Svr2Node1, sBdAddr, bNodeAll, iStatus,sLedColor);

        Bundle bundle = new Bundle();

        bundle.putByteArray("cmd", bCmd);

        bundle.putByteArray("TargetBdAddr", bCmd);
        msg.setData(bundle);


        try {
            mServiceMsgr.send(msg);

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return;
    }

}
