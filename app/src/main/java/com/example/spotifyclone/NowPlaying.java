package com.example.spotifyclone;

import android.content.Intent;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spotifyclone.ui.playlist.AddPlaylist;

import static com.example.spotifyclone.MusicService.state;



public class NowPlaying extends AppCompatActivity {

    MusicHelper musicHelper;
    TextView TxtSongName, TxtDuration, TxtLength;
    Button BtnPause, BtnPlay, BtnNext, BtnPrev, BtnBack, BtnShuffleOff, BtnAddPlaylist, BtnFav, BtnNotFav, BtnShuffleOn, BtnPlus10, BtnMinusTen;
    SeekBar SeekBar;
    Handler handler;
    Runnable runnable;
    public static MediaSessionCompat mediaSession;
    public static MediaSessionCompat.Token sessionToken;
    private PlaybackStateCompat.Builder mStateBuilder;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nowplaying);
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();
        musicHelper = new MusicHelper(NowPlaying.this);
        handler = new Handler();
        TxtSongName = findViewById(R.id.TxtSongName);
        BtnMinusTen = findViewById(R.id.BtnMinusTen);
        BtnPlus10 = findViewById(R.id.BtnPlus10);
        TxtDuration = findViewById(R.id.TxtDuration);
        TxtLength = findViewById(R.id.TxtLength);
        BtnPause = findViewById(R.id.BtnPause);
        BtnPlay = findViewById(R.id.BtnPlay);
        BtnNext = findViewById(R.id.BtnNext);
        BtnPrev = findViewById(R.id.BtnPrev);
        BtnBack = findViewById(R.id.BtnBack);
        BtnShuffleOff = findViewById(R.id. BtnShuffleOff);
        BtnShuffleOn = findViewById(R.id.BtnShuffleOn);
        BtnAddPlaylist = findViewById(R.id.BtnAddPlaylist);
        BtnFav = findViewById(R.id.BtnFav);
        BtnNotFav = findViewById(R.id.BtnNotFav);
        SeekBar = findViewById(R.id.SeekBar);
        BtnPause.setVisibility(View.INVISIBLE);
        BtnFav.setVisibility(View.INVISIBLE);
        BtnPlus10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = SingletonMedia.getInstance(NowPlaying.this).getCurrentPosition();
               SingletonMedia.getInstance(NowPlaying.this).getMediaPlayer().seekTo(progress + 10000);
            }
        });

        BtnMinusTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = SingletonMedia.getInstance(NowPlaying.this).getCurrentPosition();
                SingletonMedia.getInstance(NowPlaying.this).getMediaPlayer().seekTo(progress - 10000);
            }
        });

//        BtnPlus10.setVisibility(View.INVISIBLE);
//        BtnMinusTen.setVisibility(View.INVISIBLE);

        if (MainActivity.isShuffle){
            BtnShuffleOn.setVisibility(View.VISIBLE);
            BtnShuffleOff.setVisibility(View.INVISIBLE);
        }else{
            BtnShuffleOn.setVisibility(View.INVISIBLE);
            BtnShuffleOff.setVisibility(View.VISIBLE);
        }

        BtnShuffleOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BtnShuffleOn.setVisibility(View.INVISIBLE);
                BtnShuffleOff.setVisibility(View.VISIBLE);
                MainActivity.setIsShuffle(false);
            }
        });

        BtnShuffleOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BtnShuffleOff.setVisibility(View.INVISIBLE);
                BtnShuffleOn.setVisibility(View.VISIBLE);
                MainActivity.setIsShuffle(true);
            }
        });

        BtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
        BtnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = PlaybackStateCompat.STATE_PAUSED;
                MainActivity.musicSrv.pausePlayer();
                BtnPause.setVisibility(View.INVISIBLE);
                BtnPlay.setVisibility(View.VISIBLE);
            }
        });

        BtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SingletonMedia.getInstance(NowPlaying.this).getMediaPlayer() != null) {
                    if (MainActivity.isPlayed){
                        state = PlaybackStateCompat.STATE_PLAYING;
                        BtnPause.setVisibility(View.VISIBLE);
                        BtnPlay.setVisibility(View.INVISIBLE);
                        MainActivity.musicSrv.go();
                        playcycle();

                    }else{
                        MainActivity.setIsPlayed(true);
                        state = PlaybackStateCompat.STATE_PLAYING;
                        BtnPause.setVisibility(View.VISIBLE);
                        BtnPlay.setVisibility(View.INVISIBLE);
                        MainActivity.musicSrv.setPlayTrack(MusicService.getSongPosn());
                        playcycle();

                    }
                }
            }
        });

        BtnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SingletonMedia.getInstance(NowPlaying.this).getMediaPlayer() != null) {
                    if (MainActivity.isPlayed) {
                        state = PlaybackStateCompat.STATE_PLAYING;
                        MainActivity.musicSrv.playPrev();
                        playcycle();
                    }else{
                        MainActivity.setIsPlayed(true);
                        state = PlaybackState.STATE_PLAYING;
                        MainActivity.musicSrv.playPrev();
                        playcycle();

                    }

                }
            }
        });

        BtnNotFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean AddFavorite = musicHelper.insertFavorite(MusicService.getSongName().replace(".mp3", ""));

                if (AddFavorite) {
                    Toast.makeText(NowPlaying.this, "Added to Favorites", Toast.LENGTH_SHORT).show();
                    BtnNotFav.setVisibility(View.INVISIBLE);
                    BtnFav.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(NowPlaying.this, "Failed to add favorites", Toast.LENGTH_SHORT).show();

                }
            }
        });

        BtnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean addFavorite = musicHelper.deleteFavoriteByName(MusicService.getSongName().replace(".mp3", ""));

                if (addFavorite){
                    Toast.makeText(NowPlaying.this, "Successfully removed from favorites", Toast.LENGTH_SHORT).show();
                    BtnFav.setVisibility(View.VISIBLE);
                    BtnNotFav.setVisibility(View.INVISIBLE);
                }else{
                    BtnNotFav.setVisibility(View.VISIBLE);
                    Toast.makeText(NowPlaying.this, "Failed to to operation", Toast.LENGTH_SHORT).show();
                }
            }
        });


        BtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SingletonMedia.getInstance(NowPlaying.this).getMediaPlayer() != null) {
                    if (MainActivity.isPlayed) {
                        state = PlaybackStateCompat.STATE_PLAYING;
                        MainActivity.musicSrv.playNext();
                        playcycle();

                    }else{
                        MainActivity.setIsPlayed(true);
                        state = PlaybackState.STATE_PLAYING;
                        MainActivity.musicSrv.playNext();
                        playcycle();

                    }
                }

            }
        });

        BtnAddPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NowPlaying.this, AddPlaylist.class);
                startActivity(intent);
            }
        });



        SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(android.widget.SeekBar seekBar, int i, boolean b) {
                if (b) {
                    SingletonMedia.getInstance(NowPlaying.this).getMediaPlayer().seekTo(i);
                    TxtDuration.setText(createTimeLabel(SingletonMedia.getInstance(NowPlaying.this).getCurrentPosition()) + "");
                    int FinalLength = SingletonMedia.getInstance(NowPlaying.this).getMediaPlayer().getDuration() - SingletonMedia.getInstance(NowPlaying.this).getCurrentPosition();
                    TxtLength.setText( "-" +createTimeLabel(FinalLength));

                }
            }

            @Override
            public void onStartTrackingTouch(android.widget.SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(android.widget.SeekBar seekBar) {

            }
        });
        if (MainActivity.isPlayed) {
            playcycle();
            initMusic();
        }


    }



    public void playcycle(){
        SeekBar.setMax(SingletonMedia.getInstance(NowPlaying.this).getDuration());
        SeekBar.setProgress(SingletonMedia.getInstance(NowPlaying.this).getCurrentPosition());
        TxtDuration.setText(createTimeLabel(SingletonMedia.getInstance(NowPlaying.this).getCurrentPosition()) + "");
        int FinalLength = SingletonMedia.getInstance(NowPlaying.this).getMediaPlayer().getDuration() - SingletonMedia.getInstance(NowPlaying.this).getCurrentPosition();
        TxtLength.setText( "-" +createTimeLabel(FinalLength));
        if (SingletonMedia.getInstance(NowPlaying.this).isPlaying()){
            BtnPlay.setVisibility(View.INVISIBLE);
            BtnPause.setVisibility(View.VISIBLE);
        }else{
            BtnPause.setVisibility(View.INVISIBLE);
            BtnPlay.setVisibility(View.VISIBLE);
        }


            runnable = new Runnable() {
                @Override
                public void run() {
                    try{

                        if (SingletonMedia.getInstance(NowPlaying.this).isPlaying()){
                            BtnPlay.setVisibility(View.INVISIBLE);
                            BtnPause.setVisibility(View.VISIBLE);
                        }else{
                            BtnPause.setVisibility(View.INVISIBLE);
                            BtnPlay.setVisibility(View.VISIBLE);
                        }

                        TxtDuration.setText(createTimeLabel(SingletonMedia.getInstance(NowPlaying.this).getCurrentPosition()) + "");
                        int FinalLength = SingletonMedia.getInstance(NowPlaying.this).getMediaPlayer().getDuration() - SingletonMedia.getInstance(NowPlaying.this).getCurrentPosition();
                        TxtLength.setText( "-" +createTimeLabel(FinalLength));
                        playcycle();

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            };
            handler.postDelayed(runnable, 1000);
    }

    public void initMusic(){
        String name = MusicService.getSongName().replace(".mp3", "");
        TxtSongName.setText(name);

        if (musicHelper.isFavorite(name)){
            BtnNotFav.setVisibility(View.INVISIBLE);
            BtnFav.setVisibility(View.VISIBLE);
        }else{
            BtnFav.setVisibility(View.INVISIBLE);
            BtnNotFav.setVisibility(View.VISIBLE);
        }
        runnable = new Runnable() {
            @Override
            public void run() {
               initMusic();
            }
        };
        handler.post(runnable);
    }

    public String createTimeLabel(int time){
        String timelabel = "";
        int min = time / 1000/ 60;
        int sec = time / 1000 % 60;

        timelabel = min + ":";
        if (sec < 10) timelabel +=0;
        timelabel +=sec;
        return timelabel;
    }





    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }
}
