package com.example.videoplayer.Presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.view.View;

import com.example.videoplayer.Model.Video;
import com.example.videoplayer.R;

import java.util.List;

public class VideoArrayAdapter extends ArrayAdapter<Video> {
    private List<Video> videoList;

    static class FruitViewHolder {
        TextView videoDescription;
        TextView videoUrl;
    }

    public VideoArrayAdapter(Context context, int textViewResourceId, List<Video> videos) {
        super(context, textViewResourceId);

        videoList = videos;
    }

    @Override
    public void add(Video object) {
        videoList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.videoList.size();
    }

    @Override
    public Video getItem(int index) {
        return this.videoList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        FruitViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.video_item, parent, false);
            viewHolder = new FruitViewHolder();
            viewHolder.videoDescription = row.findViewById(R.id.vDesc);
            viewHolder.videoUrl = row.findViewById(R.id.vUrl);
            row.setTag(viewHolder);
        } else {
            viewHolder = (FruitViewHolder) row.getTag();
        }
        Video video = getItem(position);
        viewHolder.videoUrl.setText(video.getUrl());
        viewHolder.videoDescription.setText(video.getDesc());

        return row;
    }
}
