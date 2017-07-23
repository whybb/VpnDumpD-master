package com.vpndumpdemo.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.VpnService;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.Toast;


import com.vpndumpdemo.presentor.IGraspBagImpl;
import com.vpndumpdemo.presentor.IGraspBagPresenter;
import com.vpndumpdemo.receiver.VpnStateReceviver;
import com.vpndumpdemo.service.VpnDumpService;


import com.vpndumpdemo.R;

import com.vpndumpdemo.utils.MyLog;

//Dump ipPcap Activity
public class GraspBag extends AppCompatActivity implements View.OnClickListener, IGraspBagView ,VpnStateReceviver.Interaction {

    private final static String TAG = "GraspBag";
    private ImageButton btn_graspleft;
    private PopupWindow popupWindow;
    private Button mCaptureBtn;
    private final int REQ_START_VPN = 100;


    private VpnStateChanageReceiver VpnStateReceiver;
    private static final String VPNstart = "com.vpndump.state.start";
    private static final String VPNstop = "com.vpndump.state.stop";
    private IGraspBagPresenter mGraspPresenter;

    private boolean isGrasp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*IntentFilter intentFilter = new IntentFilter();
        VpnStateReceviver receviver = new VpnStateReceviver();
        registerReceiver(receviver, intentFilter);
        receviver.setBRInteractionListener(this);*/

        //getSupportActionBar().hide();
        VpnStateReceiver = new VpnStateChanageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(VPNstart);
        filter.addAction(VPNstop);
        registerReceiver(VpnStateReceiver, filter);
        Log.d(TAG, " VpnStateReceiver已经注册" );
        setContentView(R.layout.grasp_bag);
        init();
        mGraspPresenter = new IGraspBagImpl(this);
    }

    class VpnStateChanageReceiver extends BroadcastReceiver //注册动态广播来进行开关的控制
    {

        public void onReceive(Context context, Intent intent)
        {
           String cmdAction= intent.getAction();
            MyLog.logd("VpnStateReceviver", "receiver Action:"+cmdAction);
            if("com.vpndump.state.start".equals(cmdAction))
            {
                if (!isGrasp)
                {
                    try {
                        startVpnService();//开始VPN服务
                        if (startCapture())
                        {
                            isGrasp = true;
                            mCaptureBtn.setText("抓包中");
                        }

                    }  catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }

            if("com.vpndump.state.stop".equals(cmdAction))
            {
                if(isGrasp) {
                    try {
                        isGrasp = false;
                        mCaptureBtn.setText("没有进行抓包");
                        stopVpnService();//停止VPN服务
                        stopCapture();//停止抓包
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

               /* try {
                if (isGrasp) {
                    isGrasp = false;
                    mCaptureBtn.setText("抓包中");
                    stopVpnService();//停止VPN服务
                    stopCapture();//停止抓包

                } else {
                    startVpnService();//开始VPN服务
                    if (startCapture()) {
                        isGrasp = true;
                        mCaptureBtn.setText("已经停止抓包");
                    }
                }

                // Bundle bundle = getIntent().getExtras();
                //int Code=bundle.getInt("selectedItem", -1);

                // Log.d(TAG, "VpnStateChanageReceiver接受到的消息是" + Code);


            }  catch(Exception e)
            {
                e.printStackTrace();
            }*/

        }


    }

    private void init() {

       // btn_graspleft = (ImageButton) this.findViewById(R.id.btn_grasptopleft);
       // btn_graspleft.setOnClickListener(this);
        mCaptureBtn = (Button) findViewById(R.id.btn_capture);//开始抓包的按键-Button
        mCaptureBtn.setOnClickListener(this);


    }

    /**
     * 开启vpnservice
     */
    protected void startVpnService() {

        VpnDumpService.isRunning=true;
        Intent intent = VpnService.prepare(this);
        if (intent != null) {
            startActivityForResult(intent, REQ_START_VPN);

        } else {

            onActivityResult(REQ_START_VPN, RESULT_OK, null);
        }

    }

    /**
     * 停止vpnService
     */
    protected void stopVpnService() {

        VpnDumpService.isCalledByUser = true;
        VpnDumpService.isRunning = false;

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQ_START_VPN:
                MyLog.logd(this, "start Vpn Service");

                Intent intent = new Intent(this, VpnDumpService.class);
                startService(intent);

                break;
            default:
                break;

        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*add
            case R.id.btn_grasptopleft:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                    return;
                } else {
                    initmPopupWindowViewleft();
                    popupWindow.showAsDropDown(v, 0, 5);
                }
                break;
            case R.id.graspbtn_wall:
                Intent intent = new Intent();
                intent.setClass(this, MainTabbed.class);
                startActivity(intent);
                popupWindow.dismiss();
                this.finish();
                break;
            case R.id.graspbtn_dariy:
                Intent intent1 = new Intent();
                intent1.setClass(this, DairyTabbed.class);
                startActivity(intent1);
                popupWindow.dismiss();
                this.finish();
                break;
                add */

            case R.id.btn_capture://开始抓包的流程.....

                if (isGrasp) {
                    isGrasp = false;
                    mCaptureBtn.setText("已经停止抓包");
                    stopVpnService();//停止VPN服务
                    stopCapture();//停止抓包

                } else {
                    startVpnService();//开始VPN服务
                    if (startCapture()) {
                        isGrasp = true;
                        mCaptureBtn.setText("抓包中");
                    }
                }
                break;

            default:
                break;
        }

    }


    /**
     * 停止抓包
     */
    private void stopCapture() {

        //读写应该放在子线程中操作...
        mGraspPresenter.stopGraspingBag();

    }

    /**
     * 开始抓包咯
     */
    private boolean startCapture() {


        if (VpnDumpService.isRunning) {
            Toast.makeText(this, "正在进行抓包", Toast.LENGTH_SHORT).show();

            //传参应该为appId
//            PCapFilter.startCapPacket(0);
            mGraspPresenter.startGraspingBag(-1);
            return true;
        } else {
            Toast.makeText(this, "请开启vpn服务", Toast.LENGTH_SHORT).show();
            return false;
        }

    }


  /*add
  private void initmPopupWindowViewleft() {
        View customView = getLayoutInflater().inflate(R.layout.graspleft_top,
                null, false);
        popupWindow = new PopupWindow(customView, 500, 600);
        // 设置动画效果 [R.style.AnimationFade 是自己事先定义好的]
        popupWindow.setAnimationStyle(R.style.ways);
        popupWindow.setOutsideTouchable(true);
        // 自定义view添加触摸事件

        customView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }

                return false;
            }
        });

        /** 在这里可以实现自定义视图的功能 */
  /*add
        Button btn_wall = (Button) customView.findViewById(R.id.graspbtn_wall);
        Button btn_dariy = (Button) customView.findViewById(R.id.graspbtn_dariy);
        btn_wall.setOnClickListener(this);
        btn_dariy.setOnClickListener(this);

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Intent intent = new Intent();
            startActivity(intent.setClass(this, MainTabbed.class));
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    add */

    @Override
    public void onGraspFininished(String savePath) {

        if (TextUtils.isEmpty(savePath)) {
            Toast.makeText(this, "抓包失败orz", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "抓包成功,保存路径为:" + savePath, Toast.LENGTH_LONG).show();


    }


    @Override
    public void onLoadApp(BaseAdapter baseAdapter) {

    }

    @Override
    public void onOptionFailed(int typeId, String msg) {

    }

    @Override
    public void onDataShowRefresh() {

    }

    @Override
    public void DealVpnSate(int state)
    {
        Log.d(TAG, "VpnStateChanageReceiver接受到的消息是" + state);
        if(state==1)
        {
            if (!isGrasp)
            {
                try {
                        startVpnService();//开始VPN服务
                        if (startCapture())
                        {
                            isGrasp = true;
                            mCaptureBtn.setText("抓包中");
                        }

                }  catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        }

        else
        {
            try
            {
                isGrasp = false;
                mCaptureBtn.setText("没有进行抓包");
                stopVpnService();//停止VPN服务
                stopCapture();//停止抓包
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }


    }
}

