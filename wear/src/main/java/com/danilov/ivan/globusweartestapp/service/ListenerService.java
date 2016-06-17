package com.danilov.ivan.globusweartestapp.service;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.LocalBroadcastManager;

import com.danilov.ivan.commonlibrary.TheApplication;
import com.danilov.ivan.commonlibrary.utils.Utils;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.ByteArrayOutputStream;

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

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);
        Intent messageIntent = new Intent();
        messageIntent.setAction(Intent.ACTION_SEND);
        DataMapItem mapItem = DataMapItem.fromDataItem(dataEvents.get(0).getDataItem());
        Asset asset = mapItem.getDataMap().getAsset(ListenerService.EXTRA_PHOTO);
        //Convert to byte array
        Bitmap bitmap = BitmapFactory.decodeStream(Wearable.DataApi.getFdForAsset(TheApplication.getInstance().getGoogleClient(),
                asset).await().getInputStream());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        messageIntent.putExtra(ListenerService.EXTRA_PHOTO, byteArray);
        LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
    }
}
