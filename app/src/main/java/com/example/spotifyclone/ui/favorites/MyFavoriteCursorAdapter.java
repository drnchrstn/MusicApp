package com.example.spotifyclone.ui.favorites;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
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
import com.example.spotifyclone.R;
import com.example.spotifyclone.SingletonArraylist;
import com.example.spotifyclone.SingletonMusicReader;

import java.io.File;
import java.util.ArrayList;

import static com.example.spotifyclone.MusicService.state;

public class MyFavoriteCursorAdapter extends BaseCursorAdapter<MyFavoriteCursorAdapter.MyFavoriteViewholder>{
    ArrayList<File> songs;
    Context context;
//    favoritecallback callback;
    public MyFavoriteCursorAdapter(Context context, ArrayList<File> songs) {
        super(null);
        this.context = context;
        this.songs = songs;
//        this.callback = callback;
//        songs = SingletonArraylist.getInstance(context).getdefaultlist();
    }

    @Override
    public void onBindViewHolder(MyFavoriteViewholder holder, final Cursor cursor) {
        final String id = cursor.getString(0);
        final String name = cursor.getString(1);
        holder.TxtSongName.setText(name);

        holder.BtnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure do you want to delete this from favorites?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ContentResolver cr = context.getContentResolver();
                        cr.delete(FavoriteProvider.CONTENT_URI, FavoriteDb.KEY_ROWID + " = " +id, null);
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

                    if (name.equals(nameofSong)){
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
    public MyFavoriteViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_card, parent, false);


        return new MyFavoriteViewholder(view);
    }

    public class MyFavoriteViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView TxtSongName;
        public Button BtnFav;
        public ConstraintLayout layoutview;

        public MyFavoriteViewholder(@NonNull View v) {
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
            switch (view.getId()){
            }
        }
    }



    public ArrayList<File> readSongs(File root){
        ArrayList<File> arrayList = new ArrayList<File>();
        File[] files = root.listFiles();
        for (File file : files ){
            if (file.isDirectory() && !file.isHidden()){
                arrayList.addAll(readSongs(file));
            }else{
                if (file.getName().endsWith(".mp3")){
                    arrayList.add(file);
                }
            }
        }
        return arrayList;
    }
}
