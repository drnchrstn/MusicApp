package com.example.spotifyclone;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.media.session.MediaButtonReceiver;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import static com.example.spotifyclone.MainActivity.sessionToken;


public class MusicService extends Service {

    MediaPlayer mediaPlayer;
    ArrayList<File> songs;
    int position = 0;
    private static final String CHANNEL_ID = "media_playback_channel";
    public static String songTitle = "";
    private final IBinder musicBind = new MusicBinder();
    public static long state = PlaybackStateCompat.STATE_PAUSED;
    int action;
    static final int AUTO = 1;
    static final int PLAY_TRACK = 2;
    public static int songPosn = 0;
    private int songsNumber;
    Notification notification;


    @Override
    public void onCreate() {
        songPosn = MainActivity.getPosn();
        songs = SingletonArraylist.getInstance(MusicService.this).getSongList();
        mediaPlayer = SingletonMedia.getInstance(MusicService.this).getMediaPlayer();
        songsNumber = songs.size();
        state = PlaybackStateCompat.STATE_PAUSED;




        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Intent onBindIntent = new Intent("STATE_BIND");

        LocalBroadcastManager.getInstance(this).sendBroadcast(onBindIntent);
        return musicBind;
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    public void stopNotifications() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE);
        mediaPlayer.release();
        stopForeground(true);
        stopSelf();
    }

    public void playNext() {
        songsNumber = SingletonArraylist.getInstance(MusicService.this).getSongList().size();
        if (MainActivity.isShuffle){

            state = PlaybackStateCompat.STATE_PLAYING;
            Random randomNumber = new Random();
            int[] array = new int[songsNumber];
            for (int i = 0; i<songsNumber; i++){
                array[i] = i;
            }
            for (int i =0; i<songsNumber; i++){
                int randomPos = randomNumber.nextInt(songsNumber);
                songPosn = randomPos;
                break;

            }

            playMusic(songPosn);
            MainActivity.setPosn(songPosn);

        }else {
            state = PlaybackStateCompat.STATE_PLAYING;
            action = PLAY_TRACK;
            songPosn++;
            if (songPosn >= songsNumber) songPosn = 0;
            playMusic(songPosn);
            MainActivity.setPosn(songPosn);
        }
    }

    public void playMusic(int position){
        songTitle = SingletonArraylist.getInstance(MusicService.this).getSongList().get(position).getName();
        Uri songResourceUri = Uri.parse(SingletonArraylist.getInstance(MusicService.this).getSongList().get(position).toString());
        SingletonMedia.getInstance(MusicService.this).playSound(MusicService.this, new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                state = PlaybackStateCompat.STATE_PLAYING;
                Intent onPreparedIntent = new Intent(   "MEDIA_PLAYER_PREPARED");
                LocalBroadcastManager.getInstance(MusicService.this).sendBroadcast(onPreparedIntent);
                action = AUTO;
                state = PlaybackStateCompat.STATE_PLAYING;
            }
        }, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                playNext();
            }
        }, songResourceUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            showNotification();
        }

        state = PlaybackStateCompat.STATE_PLAYING;
    }

    public void pausePlayer() {

        SingletonMedia.getInstance(MusicService.this).pauseSound();
        state = PlaybackStateCompat.STATE_PAUSED;



    }

    public void go(){

        SingletonMedia.getInstance(MusicService.this).resumeSound();
        state = PlaybackState.STATE_PLAYING;

    }

    public void playPrev() {
        songsNumber = SingletonArraylist.getInstance(MusicService.this).getSongList().size();
        if (MainActivity.isShuffle){

            state = PlaybackStateCompat.STATE_PLAYING;
            Random randomNumber = new Random();
            int[] array = new int[songsNumber];
            for (int i = 0; i<songsNumber; i++){
                array[i] = i;
            }
            for (int i =0; i<songsNumber; i++){
                int randomPos = randomNumber.nextInt(songsNumber);
                songPosn = randomPos;
                break;

            }

            playMusic(songPosn);
            MainActivity.setPosn(songPosn);

        }else {


            action = PLAY_TRACK;
            songPosn--;
            if (songPosn < 0) songPosn = songsNumber - 1;
            playMusic(songPosn);
            state = PlaybackStateCompat.STATE_PLAYING;
            MainActivity.setPosn(songPosn);
        }
    }

    public void setPlayTrack(int position) {

        action = PLAY_TRACK;
        songPosn = position;
        playMusic(position);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public static int getSongPosn(){
        return songPosn;
    }

    public static String getSongName(){
        return songTitle;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Media Playback";
            String description = "Media playback controls";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void showNotification(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setOngoing(true);
        builder.addAction(R.drawable.prev, Constants.ACTION_PREV,
                MediaButtonReceiver.buildMediaButtonPendingIntent
                        (this, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));

        if (state == PlaybackStateCompat.STATE_PLAYING) {
            builder.addAction(
                    R.drawable.pause, Constants.ACTION_PAUSE,
                    MediaButtonReceiver.buildMediaButtonPendingIntent(this,
                            PlaybackStateCompat.ACTION_PAUSE));
        } else {
            builder.addAction(
                    R.drawable.play, Constants.ACTION_PLAY,
                    MediaButtonReceiver.buildMediaButtonPendingIntent(this,
                            PlaybackStateCompat.ACTION_PLAY));
            builder.setAutoCancel(true);
            builder.setOngoing(false);



        }
        builder.addAction(R.drawable.next, Constants.ACTION_NEXT,
                MediaButtonReceiver.buildMediaButtonPendingIntent
                        (this, PlaybackStateCompat.ACTION_SKIP_TO_NEXT));

        builder.addAction(R.drawable.ic_close_black_24dp, Constants.STOPFOREGROUND_ACTION,
                MediaButtonReceiver.buildMediaButtonPendingIntent
                        (this, PlaybackStateCompat.ACTION_STOP));


        Intent intent = new Intent(getApplicationContext(), NowPlaying.class);
        intent.addCategory(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setClass(this, NowPlaying.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentTitle("Now Playing...")
                .setContentText(songTitle.replace(".mp3", ""))
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.drawable.applemusiclogo)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(sessionToken)
                        .setShowActionsInCompactView(0, 1,2,3));


        notification = builder.build();
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                notification);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}
