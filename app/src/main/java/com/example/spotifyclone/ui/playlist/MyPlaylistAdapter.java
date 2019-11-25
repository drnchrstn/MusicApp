package com.example.spotifyclone.ui.playlist;

import android.content.Context;
import android.content.Intent;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifyclone.MainActivity;
import com.example.spotifyclone.MusicHelper;
import com.example.spotifyclone.NowPlaying;
import com.example.spotifyclone.R;
import com.example.spotifyclone.objects.MusicPool;
import static com.example.spotifyclone.MusicService.state;

import java.io.File;
import java.util.ArrayList;

public class MyPlaylistAdapter extends RecyclerView.Adapter<MyPlaylistAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<MusicPool> musicPool;
    public ArrayList<File> songs;
    MusicHelper musicHelper;

    public MyPlaylistAdapter(Context context,ArrayList<MusicPool> musicPool, ArrayList<File> songs) {
        this.context = context;
        this.musicPool = musicPool;
        this.songs = songs;
        musicHelper = new MusicHelper(context);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.songcard, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        if (musicHelper.isFavorite(musicPool.get(position).getName().replace(".mp3",""))){
            holder.BtnNotFav.setVisibility(View.INVISIBLE);
            holder.BtnFav.setVisibility(View.VISIBLE);
        }else{
            holder.BtnNotFav.setVisibility(View.VISIBLE);
            holder.BtnFav.setVisibility(View.INVISIBLE);
        }
        holder.TxtSongName.setText(musicPool.get(position).getName().replace(".mp3", ""));

    }

    @Override
    public int getItemCount() {
        return musicPool.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView TxtSongName;
        public Button BtnNotFav,BtnFav;
        public ConstraintLayout LayoutSong;




        public MyViewHolder(@NonNull View v) {
            super(v);
            LayoutSong = (ConstraintLayout)v.findViewById(R.id.songLayout);
            LayoutSong.setOnClickListener(this);
            BtnNotFav = v.findViewById(R.id.BtnNotFav);
            BtnNotFav.setOnClickListener(this);
            TxtSongName = v.findViewById(R.id.TxtSongName);
            BtnFav = v.findViewById(R.id.BtnFav);
            BtnFav.setVisibility(View.INVISIBLE);
            BtnFav.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int pos = getLayoutPosition();
            String name = musicPool.get(pos).getName().replace(".mp3", "");
            switch (view.getId()){
                case R.id.BtnNotFav:
                    boolean AddFavorite = musicHelper.insertFavorite(name);

                    if (AddFavorite) {
                        Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show();
                        BtnNotFav.setVisibility(View.INVISIBLE);
                        BtnFav.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(context, "Failed to add favorites" + name, Toast.LENGTH_SHORT).show();

                    }
                    break;

                case R.id.BtnFav:
                    boolean delete = musicHelper.deleteFavoriteByName(name);
                    if (delete){
                        Toast.makeText(context, "Removed to Favorites", Toast.LENGTH_SHORT).show();
                        BtnNotFav.setVisibility(View.VISIBLE);
                        BtnFav.setVisibility(View.INVISIBLE);
                    }else{
                        Toast.makeText(context, "Failed to remove to favorites", Toast.LENGTH_SHORT).show();
                    }

                    break;

                case R.id.songLayout:
//                    int a = 0;
////                    for (int i=0; i<musicPool.size(); i++){
////                        String nameOfSong = musicPool.get(i).getName().replace(".mp3", "");
////                        if (name.equals(nameOfSong)){
////                            a = i;
////                            break;
////                        }
////                    }
                    MainActivity.setIsPlayed(true);
                    state = PlaybackStateCompat.STATE_PLAYING;
                    MainActivity.musicSrv.setPlayTrack(pos);
                    Intent intent = new Intent(context, NowPlaying.class);
                    context.startActivity(intent);
            }


        }
    }

}
