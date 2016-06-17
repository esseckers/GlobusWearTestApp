package com.danilov.ivan.globusweartestapp;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import com.danilov.ivan.commonlibrary.annotation.Layout;
import com.danilov.ivan.commonlibrary.model.Sound;
import com.danilov.ivan.globusweartestapp.service.ListenerService;
import com.danilov.ivan.commonlibrary.utils.Utils;

import butterknife.ButterKnife;

/**
 * Created by Ivan Danilov on 17.06.2016.
 * Email: i.danilov@globus-ltd.com
 */
public abstract class AbstractActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getViewLayout());
        ButterKnife.bind(this);
        BluetoothAdapter.getDefaultAdapter().enable();
        // Register the local broadcast receiver
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);
    }

    protected int getViewLayout() {
        Layout layout = this.getClass().getAnnotation(Layout.class);
        return layout != null ? layout.value() : 0;
    }

    protected abstract void updateDisplay(Sound sound);

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra(ListenerService.EXTRA_PLAY_LIST);
            updateDisplay(Utils.getModel(message, Sound.class));
        }
    }
}
