package com.danilov.ivan.globusweartestapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.danilov.ivan.commonlibrary.TheApplication;
import com.danilov.ivan.commonlibrary.annotation.Layout;
import com.danilov.ivan.commonlibrary.model.Sound;
import com.danilov.ivan.commonlibrary.utils.Utils;
import com.danilov.ivan.globusweartestapp.service.ListenerService;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
/**
 * Created by Ivan Danilov on 17.06.2016.
 * Email: i.danilov@globus-ltd.com
 */
@Layout(R.layout.activity_main)
public class MainActivity extends AbstractActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    @Bind(R.id.playState)
    ImageView playState;

    @Bind(R.id.author)
    TextView author;

    @Bind(R.id.trackName)
    TextView trackName;

    @Override
    protected void updateDisplay(Sound sound) {
        if (sound.isPlay()) {
            playState.setBackground(Utils.getDrawable(this, R.drawable.ic_pause_black_24dp));
        } else {
            playState.setBackground(Utils.getDrawable(this, R.drawable.ic_play_arrow_black_24dp));
        }
        author.setText(sound.getAuthor());
        trackName.setText(sound.getTrackName());
    }

    @OnClick(R.id.refreshData)
    void refresh() {
        sendMessage(Utils.getJsonString(mockData()).getBytes(), Utils.PLAY_PATH);
    }

    private List<Sound> mockData() {
        List<Sound> soundList = new ArrayList<Sound>();
        soundList.add(new Sound("avicii", "levels", false));
        soundList.add(new Sound("Eddie Vedder", "Society", false));
        soundList.add(new Sound("Kygo", "Nothing Left (ft. Will Heard)", false));
        soundList.add(new Sound("A. Fruit", "South wind (demo)", false));
        soundList.add(new Sound("Frankie Knuckles", "Best of Frankie Knuckles", false));
        soundList.add(new Sound("Lo Air", "A Little (Original Mix)", false));
        soundList.add(new Sound("Linkin Park", "What I've Done", false));
        return soundList;
    }

    @OnClick(R.id.uploadPhoto)
    void getPhoto() {
        takePictureFromGallery(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            sendImage(getAsset(data.getData()), Utils.PHOTO_PATH);
        }
    }

    public static void takePictureFromGallery(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, 0);
    }

    private Asset getAsset(Uri uri) {
        try {
            Bitmap b = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return createAssetFromBitmap(Bitmap.createScaledBitmap(b, 1000, 1000, false));
        } catch (IOException e) {
            return null;
        }
    }

    private Asset createAssetFromBitmap(Bitmap bitmap) {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        return Asset.createFromBytes(byteStream.toByteArray());
    }

    public static void sendImage(final Asset asset, final String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PutDataMapRequest mapRequest = PutDataMapRequest.create(path);
                DataMap dataMap = mapRequest.getDataMap();
                dataMap.putAsset(ListenerService.EXTRA_PHOTO, asset);
                Wearable.DataApi.putDataItem(
                        TheApplication.getInstance().getGoogleClient(), mapRequest.asPutDataRequest()).await();
            }
        }).start();
    }

    public static void sendMessage(final byte[] msg, final String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(TheApplication.getInstance().getGoogleClient()).await();
                for (Node node : nodes.getNodes()) {
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(TheApplication.getInstance().getGoogleClient(), node.getId(), path, msg).await();
                    if (result.getStatus().isSuccess()) {
                        Log.v(TAG, "Message sent to: " + node.getDisplayName());
                    } else {
                        // Log an error
                        Log.v(TAG, "ERROR: failed to send Message");
                    }
                }
            }
        }).start();
    }
}
