package com.example.spotifyclone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.media.session.MediaButtonReceiver;

import static com.example.spotifyclone.MainActivity.mediaSession;

public class MediaReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        MediaButtonReceiver.handleIntent(mediaSession, intent);

    }
}
