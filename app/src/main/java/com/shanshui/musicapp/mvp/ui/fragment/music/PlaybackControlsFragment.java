package com.shanshui.musicapp.mvp.ui.fragment.music;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shanshui.musicapp.R;
import com.shanshui.musicapp.mvp.AppConstant;
import com.shanshui.musicapp.mvp.music.LogHelper;
import com.shanshui.musicapp.mvp.ui.activity.music.FullScreenPlayerActivity;
import com.shanshui.musicapp.mvp.ui.widget.PlayPauseView;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author mashanshui
 * @date 2019-01-14
 * @desc 音乐播放控制器
 */
public class PlaybackControlsFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {
    private static final String TAG = LogHelper.makeLogTag(PlaybackControlsFragment.class);
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.iv_cover)
    CircleImageView ivCover;
    @BindView(R.id.title)
    TextView musicTitle;
    @BindView(R.id.artist)
    TextView musicArtist;
    @BindView(R.id.playPauseView)
    PlayPauseView playPauseView;
    @BindView(R.id.playQueueIv)
    ImageButton playQueueIv;
    @BindView(R.id.top_container)
    RelativeLayout topContainer;
    Unbinder unbinder;
    @BindView(R.id.nextPlayIv)
    MaterialIconView nextPlayIv;
    private MediaControllerCompat controller;
    private PlaybackStateCompat mLastPlaybackState;
    private final ScheduledExecutorService mExecutorService =
            Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> mScheduleFuture;
    private static final long PROGRESS_UPDATE_INTERNAL = 1000;
    private static final long PROGRESS_UPDATE_INITIAL_INTERVAL = 100;
    private final Handler mHandler = new Handler();

    public PlaybackControlsFragment() {
        // Required empty public constructor
    }

    private final Runnable mUpdateProgressTask = new Runnable() {
        @Override
        public void run() {
            updateProgress();
        }
    };

    private void scheduleSeekbarUpdate() {
        stopSeekbarUpdate();
        if (!mExecutorService.isShutdown()) {
            mScheduleFuture = mExecutorService.scheduleAtFixedRate(
                    new Runnable() {
                        @Override
                        public void run() {
                            mHandler.post(mUpdateProgressTask);
                        }
                    }, PROGRESS_UPDATE_INITIAL_INTERVAL,
                    PROGRESS_UPDATE_INTERNAL, TimeUnit.MILLISECONDS);
        }
    }

    private void stopSeekbarUpdate() {
        if (mScheduleFuture != null) {
            mScheduleFuture.cancel(false);
        }
    }


    private final MediaControllerCompat.Callback mCallback = new MediaControllerCompat.Callback() {
        @Override
        public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
            LogHelper.d(TAG, "Received playback state change to state ", state.getState());
            PlaybackControlsFragment.this.onPlaybackStateChanged(state);
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            if (metadata == null) {
                return;
            }
            LogHelper.d(TAG, "Received metadata state change to mediaId=",
                    metadata.getDescription().getMediaId(),
                    " song=", metadata.getDescription().getTitle());
            PlaybackControlsFragment.this.onMetadataChanged(metadata);
            updateDuration(metadata);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playback_controls, container, false);
        unbinder = ButterKnife.bind(this, view);
        controller = MediaControllerCompat.getMediaController(getActivity());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeScaleUpAnimation(ivCover,
                        ivCover.getWidth() / 2, ivCover.getHeight() / 2, 0, 0);
                Intent intent = new Intent(getActivity(), FullScreenPlayerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                MediaControllerCompat controller = MediaControllerCompat.getMediaController(getActivity());
                MediaMetadataCompat metadata = controller.getMetadata();
                if (metadata != null) {
                    intent.putExtra(AppConstant.EXTRA_CURRENT_MEDIA_DESCRIPTION,
                            metadata.getDescription());
                    startActivity(intent, compat.toBundle());
                }
            }
        });
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        LogHelper.d(TAG, "fragment.onStart");
        MediaControllerCompat controller = MediaControllerCompat.getMediaController(getActivity());
        if (controller != null) {
            onConnected();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        LogHelper.d(TAG, "fragment.onStop");
        MediaControllerCompat controller = MediaControllerCompat.getMediaController(getActivity());
        if (controller != null) {
            controller.unregisterCallback(mCallback);
        }
    }


    @OnClick({R.id.playPauseView, R.id.nextPlayIv, R.id.playQueueIv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.playPauseView:
                if (playPauseView.isPlaying()) {
                    //播放状态
                    pauseMedia();
                } else {
                    //暂停状态
                    playMedia();
                }
                break;
            case R.id.nextPlayIv:
                nextMedia();
                break;
            case R.id.playQueueIv:
                break;
            default:
                break;
        }
    }

    public void onConnected() {
        MediaControllerCompat controller = MediaControllerCompat.getMediaController(getActivity());
        LogHelper.d(TAG, "onConnected, mediaController==null? ", controller == null);
        if (controller != null) {
            onMetadataChanged(controller.getMetadata());
            onPlaybackStateChanged(controller.getPlaybackState());
            controller.registerCallback(mCallback);
        }
    }

    private void onPlaybackStateChanged(PlaybackStateCompat state) {
        LogHelper.d(TAG, "onPlaybackStateChanged ", state);
        if (getActivity() == null) {
            LogHelper.w(TAG, "onPlaybackStateChanged called when getActivity null," +
                    "this should not happen if the callback was properly unregistered. Ignoring.");
            return;
        }
        if (state == null) {
            return;
        }
        mLastPlaybackState = state;
        boolean playing = false;
        switch (state.getState()) {
            case PlaybackStateCompat.STATE_PLAYING:
                scheduleSeekbarUpdate();
                playing = true;
                break;
            case PlaybackStateCompat.STATE_PAUSED:
            case PlaybackStateCompat.STATE_STOPPED:
                stopSeekbarUpdate();
                break;
            case PlaybackStateCompat.STATE_ERROR:
                LogHelper.e(TAG, "error playbackstate: ", state.getErrorMessage());
                Toast.makeText(getActivity(), state.getErrorMessage(), Toast.LENGTH_LONG).show();
                stopSeekbarUpdate();
                break;
            default:
                break;
        }

        if (playing) {
            //播放状态
            playPauseView.play();
            playPauseView.setPlaying(true);
        } else {
            //暂停状态
            playPauseView.pause();
            playPauseView.setPlaying(false);
        }
    }

    private void onMetadataChanged(MediaMetadataCompat metadata) {
        LogHelper.d(TAG, "onMetadataChanged ", metadata);
        if (getActivity() == null) {
            LogHelper.w(TAG, "onMetadataChanged called when getActivity null," +
                    "this should not happen if the callback was properly unregistered. Ignoring.");
            return;
        }
        if (metadata == null) {
            return;
        }
        String title = metadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE);
        String artist = metadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST);
        String iconUrl = metadata.getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI);
        musicTitle.setText(title);
        musicArtist.setText(artist);
        Glide.with(getActivity()).load(iconUrl)
                .apply(new RequestOptions().placeholder(R.drawable.default_cover))
                .into(ivCover);
    }

    private void playMedia() {
        MediaControllerCompat controller = MediaControllerCompat.getMediaController(getActivity());
        if (controller != null) {
            controller.getTransportControls().play();
        }
    }

    private void pauseMedia() {
        MediaControllerCompat controller = MediaControllerCompat.getMediaController(getActivity());
        if (controller != null) {
            controller.getTransportControls().pause();
        }
    }

    private void nextMedia() {
        MediaControllerCompat controller = MediaControllerCompat.getMediaController(getActivity());
        if (controller != null) {
            controller.getTransportControls().skipToNext();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        stopSeekbarUpdate();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        controller.getTransportControls().seekTo(seekBar.getProgress());
        scheduleSeekbarUpdate();
    }

    private void updateDuration(MediaMetadataCompat metadata) {
        if (metadata == null) {
            return;
        }
        LogHelper.d(TAG, "updateDuration called ");
        int duration = (int) metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
        progressBar.setMax(duration);
//        mEnd.setText(DateUtils.formatElapsedTime(duration/1000));
    }


    private void updateProgress() {
        if (mLastPlaybackState == null) {
            return;
        }
        long currentPosition = mLastPlaybackState.getPosition();
        if (mLastPlaybackState.getState() == PlaybackStateCompat.STATE_PLAYING) {
            // Calculate the elapsed time between the last position update and now and unless
            // paused, we can assume (delta * speed) + current position is approximately the
            // latest position. This ensure that we do not repeatedly call the getPlaybackState()
            // on MediaControllerCompat.
            long timeDelta = SystemClock.elapsedRealtime() -
                    mLastPlaybackState.getLastPositionUpdateTime();
            currentPosition += (int) timeDelta * mLastPlaybackState.getPlaybackSpeed();
        }
        progressBar.setProgress((int) currentPosition);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        stopSeekbarUpdate();
        mExecutorService.shutdown();
    }

}
