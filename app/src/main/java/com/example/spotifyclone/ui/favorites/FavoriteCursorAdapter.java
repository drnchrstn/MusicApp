package com.example.spotifyclone.ui.favorites;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifyclone.MainActivity;
import com.example.spotifyclone.MusicHelper;
import com.example.spotifyclone.R;
import com.example.spotifyclone.SingletonArraylist;
import com.example.spotifyclone.objects.favorite;
import static com.example.spotifyclone.MusicService.state;

import java.io.File;
import java.util.ArrayList;

public class FavoriteCursorAdapter extends BaseCursorAdapter<FavoriteCursorAdapter.FavoriteViewHolder> {

    Context context;
    ArrayList<favorite> favorites;
    ArrayList<File> songs;
    private favoritecallback callback;
    MusicHelper musicHelper;

    public FavoriteCursorAdapter(Context context, Cursor cursor, favoritecallback callback) {
        super(cursor);
        songs = SingletonArraylist.getInstance(context).getdefaultlist();
        this.context = context;
        musicHelper = new MusicHelper(context);
        favorites = musicHelper.ListOfFavorite();
        this.callback = callback;
    }

    @Override
    public void onBindViewHolder(FavoriteCursorAdapter.FavoriteViewHolder holder, Cursor cursor) {

        final int Id = Integer.parseInt(cursor.getString(0));
        final String Name = cursor.getString(1);
        final int position = cursor.getPosition();
        holder.TxtSongName.setText(Name);
        holder.BtnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Name = favorites.get(position).getName();


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure do you want to delete this from favorites?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        favorites.clear();
                        favorites.addAll(musicHelper.ListOfFavorite());
                        callback.DeleteFavorite(Id);

//                                boolean delete= musicHelper.deleteFavorite(Id);
//                                if (delete){
//                                    Toast.makeText(context, Name + " was removed from favorites", Toast.LENGTH_SHORT).show();
//                                }else{
//                                    Toast.makeText(context, "Failed to do operation", Toast.LENGTH_SHORT).show();
//                                }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();



            }
        });
        holder.layoutview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainActivity.setIsPlayed(true);

                int a = 0;

                for (int i = 0; i< songs.size(); i++){

                    String nameofSong = songs.get(i).getName().replace(".mp3", "");

                    if (Name.equals(nameofSong)){
                        a=i;
                        break;
                    }
                }
                state = PlaybackStateCompat.STATE_PLAYING;
                MainActivity.musicSrv.setPlayTrack(a);



            }
        });

    }

    @NonNull
    @Override
    public FavoriteCursorAdapter.FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_card, parent, false);


        return new FavoriteViewHolder(view);
    }

    @Override
    public void swapCursor(Cursor newCursor) {
        super.swapCursor(newCursor);
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView TxtSongName;
        public Button BtnFav;
        public ConstraintLayout layoutview;

        public FavoriteViewHolder(@NonNull View v) {
            super(v);

            v.setOnClickListener(this);

            TxtSongName = v.findViewById(R.id.TxtSongName);
            BtnFav = v.findViewById(R.id.BtnFav);
            BtnFav.setOnClickListener(this);
            layoutview = (ConstraintLayout)v.findViewById(R.id.layoutview);
            layoutview.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
