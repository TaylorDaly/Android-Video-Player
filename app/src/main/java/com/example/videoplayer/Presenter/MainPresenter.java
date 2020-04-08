package com.example.videoplayer.Presenter;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.example.videoplayer.Model.Video;
import com.example.videoplayer.R;

import java.util.ArrayList;
import java.util.List;

public class MainPresenter {

    private List<Video> videos = new ArrayList<>();
    private View mainView;
    public ArrayAdapter<Video> videoAdapter;

    public MainPresenter(View view, Context c) {
        this.mainView = view;

        // Hardcoding URL list, could be replaced with a file read or DB read in the future
        String[] VideosList = {"https://s3.amazonaws.com/interview-quiz-stuff/tos-trailer/master.m3u8",
                "https://s3.amazonaws.com/interview-quiz-stuff/tos/master.m3u8"};

        // Parse videos from array into objects
        int iter = 1;
        for (String url : VideosList) {
            videos.add(new Video(url, "Video " + iter));

            ++iter;
        }

        videoAdapter = new VideoArrayAdapter(c, R.layout.video_item, videos);
    }

    public void nextVideo(int videoIndex) {
        mainView.playVideo(videos.get(videoIndex + 1));
    }

    public interface View {
        void setAdapter(ArrayAdapter<Video> adapter);

        void playVideo(Video video);
    }
}
