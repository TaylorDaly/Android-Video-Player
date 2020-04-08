package com.example.videoplayer.View;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.videoplayer.Model.Video;
import com.example.videoplayer.R;
import com.example.videoplayer.Presenter.MainPresenter;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


public class FullscreenActivity extends AppCompatActivity implements MainPresenter.View {
    private ListView mVideoList;
    private MainPresenter presenter;

    // Video player variables
    private PlayerView playerView;
    private View loadingSpinner;
    private boolean playWhenReady = true;
    private SimpleExoPlayer player;
    private int videoIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        // Set up presenter
        presenter = new MainPresenter(this, this);

        // Set up list of videos view
        mVideoList = findViewById(R.id.list_view_videos);
        setAdapter(presenter.videoAdapter);
    }

    public void setAdapter(ArrayAdapter<Video> adapter) {
        mVideoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                Video item = (Video) adapter.getItemAtPosition(position);
                videoIndex = position;
                playVideo(item);
            }
        });

        mVideoList.setAdapter(adapter);
    }

    public void playVideo(Video video) {
        setContentView(R.layout.video_view);

        // Set up video player
        playerView = findViewById(R.id.video_view);
        loadingSpinner = findViewById(R.id.loading);

        TrackSelection.Factory adaptiveTrackSelection = new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter());
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(adaptiveTrackSelection),
                new DefaultLoadControl());

        playerView.setPlayer(player);

        // Required overrides for Event Listener
        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case ExoPlayer.STATE_READY:
                        loadingSpinner.setVisibility(View.GONE);
                        break;
                    case ExoPlayer.STATE_BUFFERING:
                        loadingSpinner.setVisibility(View.VISIBLE);
                        break;
                    case ExoPlayer.STATE_ENDED:
                        if (videoIndex == 0) {
                            presenter.nextVideo(videoIndex);
                            videoIndex++;
                        } else {
                            setContentView(R.layout.activity_fullscreen);
                            mVideoList = findViewById(R.id.list_view_videos);
                            setAdapter(presenter.videoAdapter);
                        }
                        break;

                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
            }

            @Override
            public void onPositionDiscontinuity(int reason) {
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            }

            @Override
            public void onSeekProcessed() {
            }
        });

        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "Video Player"), defaultBandwidthMeter);

        String hls_url = video.getUrl();
        Uri uri = Uri.parse(hls_url);
        Handler mainHandler = new Handler();
        MediaSource mediaSource = new HlsMediaSource(uri,
                dataSourceFactory, mainHandler, null);
        player.prepare(mediaSource);
        player.setPlayWhenReady(playWhenReady);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setContentView(R.layout.activity_fullscreen);
        mVideoList = findViewById(R.id.list_view_videos);
        setAdapter(presenter.videoAdapter);
    }
}
