package com.danilov.ivan.globusweartestapp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.WatchViewStub;
import android.view.View;

import com.danilov.ivan.commonlibrary.annotation.Layout;
import com.danilov.ivan.commonlibrary.model.Sound;
import com.danilov.ivan.commonlibrary.utils.Utils;
import com.danilov.ivan.globusweartestapp.R;
import com.danilov.ivan.globusweartestapp.service.ListenerService;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ivan Danilov on 17.06.2016.
 * Email: i.danilov@globus-ltd.com
 */
public abstract class AbstractActivity extends WearableActivity {

    @Bind(R.id.watch_view_stub)
    WatchViewStub stub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getViewLayout());
        ButterKnife.bind(this);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                initView(stub);
            }
        });
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);
    }

    protected int getViewLayout() {
        Layout layout = this.getClass().getAnnotation(Layout.class);
        return layout != null ? layout.value() : 0;
    }

    protected abstract void updateDisplay(List<Sound> sounds);

    protected abstract void updateImage(Bitmap bitmap);

    protected abstract void initView(View stub);

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra(ListenerService.EXTRA_PLAY_LIST);
            if (message != null) {
                updateDisplay(Arrays.asList(Utils.getModel(message, Sound[].class)));
            } else {
                byte[] byteArray = intent.getByteArrayExtra(ListenerService.EXTRA_PHOTO);
                updateImage(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
            }
        }
    }
}
