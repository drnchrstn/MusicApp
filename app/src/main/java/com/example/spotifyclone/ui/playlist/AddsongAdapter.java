package com.example.spotifyclone.ui.playlist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifyclone.MusicHelper;
import com.example.spotifyclone.R;

import java.io.File;
import java.util.ArrayList;

public class AddsongAdapter extends RecyclerView.Adapter<AddsongAdapter.MyViewHolder> {

    private Context context;
    ArrayList<File> ListOfSongs;
    MusicHelper musicHelper;
    int play_Id;


    public AddsongAdapter(Context context, ArrayList<File> ListOfSongs, int play_Id) {
        this.context = context;
        this.ListOfSongs = ListOfSongs;
        this.play_Id = play_Id;
        musicHelper = new MusicHelper(context);


    }


    @NonNull
    @Override
    public AddsongAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.songcard, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddsongAdapter.MyViewHolder holder, int position) {

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
        public ConstraintLayout LayoutSong;

        public MyViewHolder(@NonNull View v) {
            super(v);

            v.setOnClickListener(this);
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
            String name = ListOfSongs.get(pos).getName().replace(".mp3", "");


            switch (view.getId()){
                case R.id.BtnNotFav:

                    boolean AddFavorite = musicHelper.insertFavorite(name);

                    if (AddFavorite) {
                        Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show();
                        BtnNotFav.setVisibility(View.INVISIBLE);
                        BtnFav.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(context, "Failed to add favorites", Toast.LENGTH_SHORT).show();

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
                    int pos1 = getLayoutPosition();
                    final String Name = ListOfSongs.get(pos1).getName().replace(".mp3", "");



                    boolean checker = musicHelper.checker(Name, play_Id);
                    if (checker){


                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Duplicate Entries");
                        builder.setMessage("" + Name + " is already in this playlist, proceed anyway?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                boolean addToMusicPool = musicHelper.addToMusicPool(Name, play_Id);

                                if (addToMusicPool){
                                    Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                                }
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




                    }else{
                        boolean addToMusicPool = musicHelper.addToMusicPool(Name, play_Id);

                        if (addToMusicPool){
                            Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }




                    break;




            }
        }
    }
}