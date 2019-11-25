package com.example.spotifyclone;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

public class SingletonMedia {
    private static volatile SingletonMedia instance = null;
    MediaPlayer mediaPlayer;
    private Context context;

    private SingletonMedia(Context context) {
        this.context = context;
    }

    public static SingletonMedia getInstance(Context context) {
        if (instance == null) {
            synchronized (SingletonMedia.class) {
                if (instance == null) {
                    instance = new SingletonMedia(context);
                }
            }
        }
        return instance;
    }

    public MediaPlayer getMediaPlayer() {
        if (instance.mediaPlayer == null) {
            instance.mediaPlayer = new MediaPlayer();
        }
        return instance.mediaPlayer;
    }

    public void playSound(Context context, MediaPlayer.OnPreparedListener onPreparedListener, MediaPlayer.OnCompletionListener onCompletionListener, Uri music) {



        if (instance.mediaPlayer != null) {
            if (instance.mediaPlayer.isPlaying()) {
                instance.mediaPlayer.stop();

            }
            instance.mediaPlayer.release();
            instance.mediaPlayer = null;

        }
        instance.mediaPlayer = new MediaPlayer();
        instance.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            instance.mediaPlayer.setDataSource(context, music);
            instance.mediaPlayer.prepare();
            instance.mediaPlayer.setOnPreparedListener(onPreparedListener);
            instance.mediaPlayer.setOnCompletionListener(onCompletionListener);
            instance.mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public synchronized void pauseSound() {
        if (instance.mediaPlayer != null) {
            instance.mediaPlayer.pause();
        }
    }

    public synchronized void resumeSound() {
        if (instance.mediaPlayer != null) {
            instance.mediaPlayer.start();
        }
    }

    public int getCurrentPosition() {
        if (instance.mediaPlayer != null) {
            return instance.mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public int getDuration() {
        if (instance.mediaPlayer != null) {
            return instance.mediaPlayer.getDuration();
        }
        return 0;
    }

    public boolean isPlaying() {
        if (instance.mediaPlayer != null) {
            return instance.mediaPlayer.isPlaying();
        }
        return true;
    }
}
