package com.example.spotifyclone.ui.playlist;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifyclone.R;
import com.example.spotifyclone.objects.Playlist;

import java.util.ArrayList;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.MyViewHolder> {


    private Context context;
    private ArrayList<Playlist> ListOfPlaylist;
    private playlistcallback callback;

    public PlaylistAdapter(Context context, ArrayList<Playlist> listOfPlaylist, playlistcallback callback) {
        this.context = context;
        ListOfPlaylist = listOfPlaylist;
        this.callback = callback;
    }

    @NonNull
    @Override
    public PlaylistAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_card, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistAdapter.MyViewHolder holder, int position) {
        holder.TxtPlaylistName.setText(ListOfPlaylist.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return ListOfPlaylist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView TxtPlaylistName;
        public Button BtnDelete;
        public ConstraintLayout layoutview;

        public MyViewHolder(@NonNull View v) {
            super(v);

            v.setOnClickListener(this);
            TxtPlaylistName = v.findViewById(R.id.TxtPlaylistName);
            BtnDelete = v.findViewById(R.id.BtnDelete);
            BtnDelete.setOnClickListener(this);
            layoutview = (ConstraintLayout) v.findViewById(R.id.layoutview);
            layoutview.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.BtnDelete:
                    int pos1 = getLayoutPosition();
                    callback.DeletePlaylist(pos1);
//                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                    builder.setTitle("Confirm");
//                    builder.setMessage("Are you sure do you want to delete this playlist?");
//                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            MusicHelper musicHelper = new MusicHelper(context);
//                            int pos = getLayoutPosition();
//                            final Playlist playlist = ListOfPlaylist.get(pos);
//                            int playId = playlist.getId();
//                            boolean massDelete = musicHelper.massDelete(playId);
//                            if (massDelete){
//                                Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();
//                            }else{
//                                Toast.makeText(context, "Failed to Delete", Toast.LENGTH_SHORT).show();
//                            }
//                            boolean delete = musicHelper.deletePlaylist(playId);
//                            if (delete){
//                                Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();
//                            }else{
//                                Toast.makeText(context, "Failed to Delete", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//
//
//                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                        }
//                    });
//
//                    AlertDialog alert = builder.create();
//                    alert.show();


                    break;
                case R.id.layoutview:
                    int pos = getLayoutPosition();
                    int play_Id = ListOfPlaylist.get(pos).getId();
                    String playlistName = ListOfPlaylist.get(pos).getName();
                    Intent intent = new Intent(context, MyPlaylist.class);
                    intent.putExtra("play_Id", play_Id);
                    intent.putExtra("playlist_Name", playlistName);
                    context.startActivity(intent);
            }

        }
    }
}