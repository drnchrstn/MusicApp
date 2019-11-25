package com.example.spotifyclone.ui.bottomplaying;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.spotifyclone.MainActivity;
import com.example.spotifyclone.MusicService;
import com.example.spotifyclone.MyNowPlaying;
import com.example.spotifyclone.NowPlaying;
import com.example.spotifyclone.R;
import com.example.spotifyclone.SingletonMedia;

import static com.example.spotifyclone.MainActivity.isPlayed;
import static com.example.spotifyclone.MainActivity.mediaSession;
import static com.example.spotifyclone.MainActivity.sessionToken;
import static com.example.spotifyclone.MusicService.state;

public class BottomPlaying extends Fragment {
    TextView TxtSongName;
    Button BtnPlay, BtnForward, BtnPause;
    ProgressBar songProgress;
    Handler handler;
    Runnable runnable;
    public static PlaybackStateCompat.Builder mStateBuilder;
    ConstraintLayout BottomLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.bottomplaying, container, false);
        TxtSongName = v.findViewById(R.id.TxtSongName);
        TxtSongName.setSelected(true);
        TxtSongName.setHorizontallyScrolling(true);
        BtnPlay = v.findViewById(R.id.BtnPlay);
        BtnPause = v.findViewById(R.id.BtnPause);
        BtnForward = v.findViewById(R.id.BtnForward);
        songProgress = v.findViewById(R.id.songProgress);
        BtnPause.setVisibility(View.INVISIBLE);
        handler = new Handler();
        BottomLayout = v.findViewById(R.id.BottomLayout);
        BtnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.musicSrv.pausePlayer();
                BtnPause.setVisibility(View.INVISIBLE);
                BtnPlay.setVisibility(View.VISIBLE);
            }
        });

        BtnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.musicSrv.playNext();
            }
        });

        BtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = PlaybackStateCompat.STATE_PLAYING;
                if (MainActivity.isPlayed){
                    MainActivity.musicSrv.go();
                    BtnPause.setVisibility(View.VISIBLE);
                    BtnPlay.setVisibility(View.INVISIBLE);


                }else{

                    MainActivity.musicSrv.setPlayTrack(MusicService.getSongPosn());
                    MainActivity.setIsPlayed(true);
                    BtnPause.setVisibility(View.VISIBLE);
                    BtnPlay.setVisibility(View.INVISIBLE);
                }


            }
        });
        BottomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MainActivity.isPlayed){
                    Intent i = new Intent(getActivity(), NowPlaying.class);
                    getActivity().startActivity(i);
                    getActivity().overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                }else{
                    Toast.makeText(getActivity(), "Please Select a song", Toast.LENGTH_SHORT).show();
                }


            }
        });


        return v;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initMusic();
            playCycle();


    }




    public void playCycle(){
        songProgress.setMax(SingletonMedia.getInstance(getActivity()).getDuration());
        songProgress.setProgress(SingletonMedia.getInstance(getActivity()).getCurrentPosition());

        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (SingletonMedia.getInstance(getActivity()).isPlaying()){
                        BtnPlay.setVisibility(View.INVISIBLE);
                        BtnPause.setVisibility(View.VISIBLE);
                    }else{
                        BtnPause.setVisibility(View.INVISIBLE);
                        BtnPlay.setVisibility(View.VISIBLE);
                    }
                    playCycle();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1000);


    }

    public void initMusic(){
        String name = MainActivity.musicSrv.getSongName().replace(".mp3", "");
        TxtSongName.setSelected(true);
        TxtSongName.setText(name);

        runnable = new Runnable() {
            @Override
            public void run() {
                initMusic();
            }
        };
        handler.post(runnable);
    }



}
