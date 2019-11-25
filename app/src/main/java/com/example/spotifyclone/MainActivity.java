package com.example.spotifyclone;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.app.LoaderManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import static com.example.spotifyclone.MusicService.state;

public class MainActivity extends AppCompatActivity {
    MusicHelper musicHelper;
    public static MusicService musicSrv;
    private boolean musicBound=false;
    public static int posn = 0;
    public static MediaSessionCompat mediaSession;
    public static MediaSessionCompat.Token sessionToken;
    private PlaybackStateCompat.Builder mStateBuilder;
    private boolean paused = false, playbackPaused = false;
    private Intent playIntent;
    public static boolean isPlayed = false;
    public static boolean isShuffle = false;




    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        verifyStoragePermissions(MainActivity.this);
        musicHelper = new MusicHelper(MainActivity.this);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_favorites, R.id.navigation_playlist)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        bindMusicService();
        initializeMediaSession();
        state = PlaybackStateCompat.STATE_PAUSED;


    }

    public void initializeMediaSession() {
        // Create a MediaSessionCompat
        mediaSession = new MediaSessionCompat(this, "Main Activity");
        sessionToken = mediaSession.getSessionToken();
        // Enable callbacks from MediaButtons and TransportControls
        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        // Do not let MediaButtons restart the player when the app is not visible
        mediaSession.setMediaButtonReceiver(null);
        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_SKIP_TO_NEXT |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE |
                                PlaybackStateCompat.ACTION_STOP);

        mediaSession.setPlaybackState(mStateBuilder.build());
        // MySessionCallback has methods that handle callbacks from a media controller
        mediaSession.setCallback(new MySessionCallback());
        // Start the Media Session since the activity is active
        mediaSession.setActive(true);
    }

    public class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            paused = false;
            playbackPaused = false;
            go();
            state = PlaybackStateCompat.STATE_PLAYING;

        }

        @Override
        public void onPause() {
            paused = true;
            playbackPaused = true;
            pausePlayer();
            state = PlaybackStateCompat.STATE_PAUSED;

        }

        @Override
        public void onSkipToPrevious() {
            state = PlaybackStateCompat.STATE_PLAYING;
            musicSrv.playPrev();

        }

        @Override
        public void onSkipToNext() {
            state = PlaybackState.STATE_PLAYING;
            musicSrv.playNext();
        }

        @Override
        public void onStop() {
            mediaSession.release();
            musicSrv.stopNotifications();
            musicSrv.stopSelf();
            System.exit(0);
        }
    }

    public void go() {
        paused = false;
        playbackPaused = false;
        musicSrv.go();
        state = PlaybackStateCompat.STATE_PLAYING;

    }

    public void pausePlayer() {
        paused = true;
        playbackPaused = true;
        state = PlaybackStateCompat.STATE_PAUSED;
        musicSrv.pausePlayer();

    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission1 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        if (permission1 != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public static int getPosn() {
        return posn;
    }

    public static void setPosn(int posn) {
        MainActivity.posn = posn;
    }

    public static void setIsPlayed(boolean isPlayed) {
        MainActivity.isPlayed = isPlayed;
    }


    public static void setIsShuffle(boolean isShuffle) {
        MainActivity.isShuffle = isShuffle;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (musicSrv != null){
            doUnbindService();
        }


    }

    void doUnbindService() {
        if (musicBound) {
            // Release information about the service's state
            unbindService(musicConnection);
            musicBound = false;
        }
    }

    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            //get service
            musicSrv = binder.getService();

            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    public void bindMusicService() {
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            startService(playIntent); // starting the service and then binding it - so it will continue running after closing app
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
        }
    }
}
