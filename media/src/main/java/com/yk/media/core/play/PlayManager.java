package com.yk.media.core.play;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;

import com.yk.media.data.base.Size;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayManager implements IPlay,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {
    private static final String TAG = "PlayManager";

    /**
     * 默认是否循环播放
     */
    private static final boolean LOOP_PLAY = true;

    /**
     * MediaPlayer
     */
    private MediaPlayer mediaPlayer;

    /**
     * 播放回调
     */
    private final List<OnPlayListener> onPlayListenerList = new ArrayList<>();

    @Override
    public void continuePlay() {
        if (mediaPlayer == null || mediaPlayer.isPlaying()) {
            return;
        }
        mediaPlayer.start();
    }

    @Override
    public void play(String path) {
        play(path, LOOP_PLAY);
    }

    @Override
    public void play(String path, boolean isLoop) {
        play(path, null, isLoop);
    }

    @Override
    public void play(String path, SurfaceTexture surfaceTexture) {
        play(path, surfaceTexture, LOOP_PLAY);
    }

    @Override
    public void play(String path, SurfaceTexture surfaceTexture, boolean isLoop) {
        if (TextUtils.isEmpty(path)) {
            onErrorPlay(new IllegalArgumentException("path is empty"));
            return;
        }

        stop();

        Log.d(TAG, "play: " + path + " surfaceTexture:" + surfaceTexture);

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setLooping(isLoop);
            mediaPlayer.setDataSource(path);
            if (surfaceTexture != null) {
                mediaPlayer.setSurface(new Surface(surfaceTexture));
            }
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            onErrorPlay(new IllegalStateException("play fail"));
        }
    }

    @Override
    public void seekTo(int time) {
        if (mediaPlayer == null) {
            return;
        }
        long duration = getDuration();
        if (time >= duration) {
            return;
        }
        mediaPlayer.seekTo(time);
    }

    @Override
    public void pause() {
        if (mediaPlayer == null) {
            return;
        }
        mediaPlayer.pause();
        onPausePlay(mediaPlayer.getCurrentPosition());
    }

    @Override
    public void stop() {
        if (mediaPlayer == null) {
            return;
        }
        mediaPlayer.stop();
        onStopPlay();
    }

    @Override
    public long getDuration() {
        if (mediaPlayer == null) {
            return -1;
        }
        return mediaPlayer.getDuration();
    }

    @Override
    public long getCurrentPosition() {
        if (mediaPlayer == null) {
            return -1;
        }
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public boolean isPlaying() {
        if (mediaPlayer == null) {
            return false;
        }
        return mediaPlayer.isPlaying();
    }

    @Override
    public Size getVideoSize() {
        if (mediaPlayer == null) {
            return null;
        }
        return new Size(mediaPlayer.getVideoWidth(), mediaPlayer.getVideoHeight());
    }

    @Override
    public void addOnPlayListener(OnPlayListener onPlayListener) {
        onPlayListenerList.add(onPlayListener);
    }

    @Override
    public void removeOnPlayListener(OnPlayListener onPlayListener) {
        onPlayListenerList.remove(onPlayListener);
    }

    @Override
    public void removeAllOnPlayListener() {
        onPlayListenerList.clear();
    }

    private void onStartPlay(Size videoSize, long duration) {
        if (onPlayListenerList.isEmpty()) {
            return;
        }
        for (OnPlayListener listener : onPlayListenerList) {
            listener.onStartPlay(videoSize, duration);
        }
    }

    private void onPausePlay(long currentPos) {
        if (onPlayListenerList.isEmpty()) {
            return;
        }
        for (OnPlayListener listener : onPlayListenerList) {
            listener.onPausePlay(currentPos);
        }
    }

    private void onStopPlay() {
        if (onPlayListenerList.isEmpty()) {
            return;
        }
        for (OnPlayListener listener : onPlayListenerList) {
            listener.onStopPlay();
        }
    }

    private void onPlayCompletion() {
        if (onPlayListenerList.isEmpty()) {
            return;
        }
        for (OnPlayListener listener : onPlayListenerList) {
            listener.onPlayCompletion();
        }
    }

    private void onErrorPlay(Exception error) {
        Log.e(TAG, "onErrorPlay: ", error);
        if (onPlayListenerList.isEmpty()) {
            return;
        }
        for (OnPlayListener listener : onPlayListenerList) {
            listener.onErrorPlay(error);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d(TAG, "onPrepared: ");
        mp.start();
        Size videoSize = new Size(mediaPlayer.getVideoWidth(), mediaPlayer.getVideoHeight());
        onStartPlay(videoSize, getDuration());
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        onErrorPlay(new IllegalStateException("what:" + what + " extra:" + extra));
        return true;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        onPlayCompletion();
    }
}
