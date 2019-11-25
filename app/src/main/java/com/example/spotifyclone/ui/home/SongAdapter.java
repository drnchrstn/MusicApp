package com.example.spotifyclone.ui.home;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
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
import com.example.spotifyclone.R;
import com.example.spotifyclone.ui.favorites.FavoriteDb;
import com.example.spotifyclone.ui.favorites.FavoriteProvider;

import static com.example.spotifyclone.MusicService.state;

import java.io.File;
import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder> {


    private Context context;
    ArrayList<File> ListOfSongs;
    MusicHelper musicHelper;

    public SongAdapter(Context context, ArrayList<File> listOfSongs) {
        this.context = context;
        this.ListOfSongs = listOfSongs;
        musicHelper = new MusicHelper(context);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.songcard, parent, false);



        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (musicHelper.isFavorite(ListOfSongs.get(position).getName().replace(".mp3",""))){
            holder.BtnNotFav.setVisibility(View.INVISIBLE);
            holder.BtnFav.setVisibility(View.VISIBLE);
        }else{
            holder.BtnNotFav.setVisibility(View.VISIBLE);
            holder.BtnFav.setVisibility(View.INVISIBLE);
        }

        holder.TxtSongName.setText(ListOfSongs.get(position).getName().replace(".mp3", ""));


    }

    @Override
    public int getItemCount() {
        return ListOfSongs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView TxtSongName;
        public Button BtnNotFav,BtnFav;
        public ConstraintLayout songLayout;

        public MyViewHolder(@NonNull View v) {
            super(v);

            BtnNotFav = v.findViewById(R.id.BtnNotFav);
            BtnNotFav.setOnClickListener(this);
            TxtSongName = v.findViewById(R.id.TxtSongName);
            BtnFav = v.findViewById(R.id.BtnFav);
            BtnFav.setVisibility(View.INVISIBLE);
            BtnFav.setOnClickListener(this);
            songLayout = (ConstraintLayout)v.findViewById(R.id.songLayout);
            songLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getLayoutPosition();
            String name = ListOfSongs.get(pos).getName().replace(".mp3", "");
//
            switch (view.getId()) {
                case R.id.BtnNotFav:
                    Uri newUri;
                    ContentValues newValues = new ContentValues();
                    newValues.put(FavoriteDb.KEY_NAME, name);
                    newUri = context.getContentResolver().insert(FavoriteProvider.CONTENT_URI, newValues);
                        Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show();
                        BtnNotFav.setVisibility(View.INVISIBLE);
                        BtnFav.setVisibility(View.VISIBLE);


//                    boolean AddFavorite = musicHelper.insertFavorite(name);
//
//                    if (AddFavorite) {
//                        Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show();
//                        BtnNotFav.setVisibility(View.INVISIBLE);
//                        BtnFav.setVisibility(View.VISIBLE);
//                    } else {
//                        Toast.makeText(context, "Failed to add favorites", Toast.LENGTH_SHORT).show();
//
//                    }



                    break;


                case R.id.songLayout:
                    int pos1 = getLayoutPosition();
                    MainActivity.setIsPlayed(true);
                    state = PlaybackStateCompat.STATE_PLAYING;
                    MainActivity.musicSrv.setPlayTrack(pos1);


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
            }
        }
    }
}
