package com.example.spotifyclone.ui.playlist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifyclone.MusicHelper;
import com.example.spotifyclone.MusicService;
import com.example.spotifyclone.R;
import com.example.spotifyclone.SingletonArraylist;
import com.example.spotifyclone.objects.Playlist;

import java.io.File;
import java.util.ArrayList;

public class AddToPlaylistAdapter extends RecyclerView.Adapter<AddToPlaylistAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Playlist> ListOfPlaylist;
    MusicHelper musicHelper;
    ArrayList<File> songs;

    public AddToPlaylistAdapter(Context context, ArrayList<Playlist> listOfPlaylist){
        this.context = context;
        ListOfPlaylist = listOfPlaylist;
        musicHelper = new MusicHelper(context);
        songs = SingletonArraylist.getInstance(context).getSongList();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addplaylist_card, parent, false);



        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.TxtPlaylistName.setText(ListOfPlaylist.get(position).getName());
        holder.TxtSongCount.setText("");

    }

    @Override
    public int getItemCount() {
        return ListOfPlaylist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView TxtPlaylistName, TxtSongCount;


        public MyViewHolder(@NonNull View v) {
            super(v);

            v.setOnClickListener(this);
            TxtPlaylistName = v.findViewById(R.id.TxtPlaylistName);
            TxtSongCount = v.findViewById(R.id.TxtSongCount);
        }

        @Override
        public void onClick(View view) {
            int pos = getLayoutPosition();
            final int play_id = ListOfPlaylist.get(pos).getId();
            final String name = MusicService.getSongName().replace(".mp3", "");


//            String songName = MusicService.getSongName();


            boolean checker = musicHelper.checker(name, play_id);
            if (checker){

//                        Toast.makeText(context, "Already Existing", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Duplicate Entries");
                builder.setMessage("" + name + " is already in this playlist, do you wish to confirm?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean addToMusicPool = musicHelper.addToMusicPool(name, play_id);

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
                boolean addToMusicPool = musicHelper.addToMusicPool(name, play_id);

                if (addToMusicPool){
                    Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                }
            }




//            boolean addToMusicPool = musicHelper.addToMusicPool(songName, play_id);
//
//            if (addToMusicPool){
//                Toast.makeText(context, "Added to " + name, Toast.LENGTH_SHORT).show();
//            }else{
//                Toast.makeText(context, "Failed to add to to " + name, Toast.LENGTH_SHORT).show();
//            }

        }
    }
}