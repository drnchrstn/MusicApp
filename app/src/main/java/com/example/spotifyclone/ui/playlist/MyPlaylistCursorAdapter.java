package com.example.spotifyclone.ui.playlist;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import com.example.spotifyclone.ui.favorites.BaseCursorAdapter;
import com.example.spotifyclone.ui.favorites.MyFavoriteCursorAdapter;

public class MyPlaylistCursorAdapter extends BaseCursorAdapter<MyPlaylistCursorAdapter.MyPlaylistAdapterViewholder> {
    Context context;
    MusicHelper musicHelper;

    public MyPlaylistCursorAdapter(Context context) {
        super(null);
        this.context = context;
    }

    @Override
    public void onBindViewHolder(MyPlaylistAdapterViewholder holder, Cursor cursor) {
        final String id = cursor.getString(0);
        final String name = cursor.getString(1);
        holder.TxtPlaylistName.setText(name);


        holder.BtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Confirm");
                    builder.setMessage("Are you sure do you want to delete this playlist?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            musicHelper = new MusicHelper(context);
                            musicHelper.massDelete(Integer.parseInt(id));
                            ContentResolver cr = context.getContentResolver();
                            cr.delete(PlaylistProvider.CONTENT_URI, PlaylistDb.KEY_ROWID + " = " +id, null);

                            if (!cr.equals(true)){
                                Toast.makeText(context, "" + name + " was deleted", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
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
            }
        });

        holder.layoutview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MyPlaylist.class);
                intent.putExtra("play_Id", id);
                intent.putExtra("playlist_Name", name);
                context.startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public MyPlaylistAdapterViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_card, parent, false);


        return new MyPlaylistAdapterViewholder(view);
    }

    public class MyPlaylistAdapterViewholder extends RecyclerView.ViewHolder {
        public TextView TxtPlaylistName;
        public Button BtnDelete;
        public ConstraintLayout layoutview;


        public MyPlaylistAdapterViewholder(@NonNull View v) {
            super(v);

            TxtPlaylistName = v.findViewById(R.id.TxtPlaylistName);
            BtnDelete = v.findViewById(R.id.BtnDelete);
            layoutview = (ConstraintLayout) v.findViewById(R.id.layoutview);
        }
    }
}
