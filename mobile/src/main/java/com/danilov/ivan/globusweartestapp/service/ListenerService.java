package com.danilov.ivan.globusweartestapp.service;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.danilov.ivan.commonlibrary.utils.Utils;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
/**
 * Created by Ivan Danilov on 17.06.2016.
 * Email: i.danilov@globus-ltd.com
 */
public class ListenerService extends WearableListenerService {

    public final static String EXTRA_PLAY_LIST = "playlist";
    public final static String EXTRA_PHOTO = "photo";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Intent messageIntent = new Intent();
        messageIntent.setAction(Intent.ACTION_SEND);
        if (messageEvent.getPath().equals(Utils.PLAY_PATH)) {
            final String message = new String(messageEvent.getData());
            messageIntent.putExtra(EXTRA_PLAY_LIST, message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
        } else {
            super.onMessageReceived(messageEvent);
        }
    }
}
