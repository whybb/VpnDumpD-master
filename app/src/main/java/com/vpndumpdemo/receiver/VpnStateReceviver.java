package com.vpndumpdemo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;

import com.vpndumpdemo.utils.MyLog;


public class VpnStateReceviver extends BroadcastReceiver
{
    private Interaction Interaction;
    @Override
    public void onReceive(Context context, Intent intent)
    {

        int state;
        try{
        state=intent.getIntExtra("state",0);
        MyLog.logd("VpnStateReceviver", "receiver sate:"+state);
        Interaction.DealVpnSate(state);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



    }

    public interface Interaction
    {
         void DealVpnSate(int state);
    }

    public void setBRInteractionListener(Interaction Interaction) {
        this.Interaction = Interaction;
    }


}
