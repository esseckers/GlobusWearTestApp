package com.danilov.ivan.globusweartestapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.danilov.ivan.commonlibrary.model.Sound;
import com.danilov.ivan.commonlibrary.utils.Utils;
import com.danilov.ivan.globusweartestapp.R;

import java.util.Arrays;
import java.util.Collection;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ivan Danilov on 17.06.2016.
 * Email: i.danilov@globus-ltd.com
 */
public class SoundAdapter extends AbstractAdapter<Sound> {

    private int[] selectedData;
    private boolean isRoundWatch;
    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public SoundAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Sound item = getItem(position);

        if (selectedData[position] == 1) {
            holder.playState.setBackground(Utils.getDrawable(context, R.drawable.ic_pause_black_24dp));
        } else {
            holder.playState.setBackground(Utils.getDrawable(context, R.drawable.ic_play_arrow_black_24dp));
        }

        holder.author.setText(item.getAuthor());
        holder.trackName.setText(item.getTrackName());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setPlay(!item.isPlay());
                if (onItemClick != null) {
                    onItemClick.togglePlay(item);
                }
                togglePlay(position);
            }
        });
        checkRoundWatch(position, convertView);
        return convertView;
    }

    public void setRoundWatch(boolean roundWatch) {
        isRoundWatch = roundWatch;
    }

    private void checkRoundWatch(int position, View convertView) {
        if (isRoundWatch) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            if (position == getCount() - 1) {
                params.setMargins(0, 0, 0, 40);
            } else if (position == 0) {
                params.setMargins(0, 40, 0, 0);
            }
            convertView.setLayoutParams(params);
        }
    }

    @Override
    public void addAll(Collection<? extends Sound> collection) {
        super.addAll(collection);
        refreshPlay(collection);
    }

    private void togglePlay(int pos) {
        if (selectedData[pos] == 1) {
            Arrays.fill(selectedData, 0);
        } else {
            Arrays.fill(selectedData, 0);
            selectedData[pos] = 1;
        }
        notifyDataSetChanged();
    }

    private void refreshPlay(Collection<? extends Sound> collection) {
        selectedData = new int[collection.size()];
        Arrays.fill(selectedData, 0);
    }

    public class ViewHolder {
        @Bind(R.id.playState)
        ImageView playState;

        @Bind(R.id.author)
        TextView author;

        @Bind(R.id.trackName)
        TextView trackName;

        public ViewHolder(View root) {
            ButterKnife.bind(this, root);
        }
    }

    public interface OnItemClick {
        void togglePlay(Sound sound);
    }
}
