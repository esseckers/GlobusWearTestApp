package com.danilov.ivan.globusweartestapp.activity;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.danilov.ivan.commonlibrary.TheApplication;
import com.danilov.ivan.commonlibrary.annotation.Layout;
import com.danilov.ivan.commonlibrary.model.Sound;
import com.danilov.ivan.commonlibrary.utils.Utils;
import com.danilov.ivan.globusweartestapp.R;
import com.danilov.ivan.globusweartestapp.adapter.SoundAdapter;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ivan Danilov on 17.06.2016.
 * Email: i.danilov@globus-ltd.com
 */
@Layout(R.layout.activity_main)
public class MainActivity extends AbstractActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private MainViewHolder mainViewHolder;
    private SoundAdapter adapter;

    @Override
    protected void updateDisplay(List<Sound> sounds) {
        mainViewHolder.listEmpty.setVisibility(View.GONE);
        adapter.addAll(sounds);
    }

    @Override
    protected void updateImage(Bitmap bitmap) {
        mainViewHolder.image.setVisibility(View.VISIBLE);
        mainViewHolder.image.setImageBitmap(bitmap);
    }

    @Override
    protected void initView(View stub) {
        mainViewHolder = new MainViewHolder(stub);
        adapter = new SoundAdapter(this, R.layout.item_sound);
        adapter.setRoundWatch(TheApplication.getInstance().isRound());
        mainViewHolder.listSounds.setAdapter(adapter);
        mainViewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
            }
        });
        adapter.setOnItemClick(new SoundAdapter.OnItemClick() {
            @Override
            public void togglePlay(Sound sound) {
                sendMessage(Utils.getJsonString(sound).getBytes(), Utils.PLAY_PATH);
            }
        });
    }

    public class MainViewHolder {

        @Bind(R.id.listEmpty)
        TextView listEmpty;

        @Bind(R.id.listSounds)
        ListView listSounds;

        @Bind(R.id.image)
        ImageView image;

        public MainViewHolder(View root) {
            ButterKnife.bind(this, root);
        }
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
