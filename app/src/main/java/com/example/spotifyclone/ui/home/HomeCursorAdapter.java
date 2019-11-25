package com.example.spotifyclone.ui.home;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
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
import com.example.spotifyclone.ui.favorites.BaseCursorAdapter;

public class HomeCursorAdapter extends BaseCursorAdapter<HomeCursorAdapter.HomeViewHolder> {
    MusicHelper musicHelper;
    Context context;
    public HomeCursorAdapter(Context context) {
        super(null);
        this.context = context;
        musicHelper = new MusicHelper(context);
    }

    @Override
    public void onBindViewHolder(final HomeViewHolder holder, final Cursor cursor) {

                final String name = cursor.getString(0).replace(".mp3", "");
                holder.TxtSongName.setText(name);


        if (musicHelper.isFavorite(name)){
            holder.BtnNotFav.setVisibility(View.INVISIBLE);
            holder.BtnFav.setVisibility(View.VISIBLE);
        }else{
            holder.BtnNotFav.setVisibility(View.VISIBLE);
            holder.BtnFav.setVisibility(View.INVISIBLE);
        }

        holder.BtnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean delete = musicHelper.deleteFavoriteByName(name);
                if (delete){
                    Toast.makeText(context, "Removed to Favorites", Toast.LENGTH_SHORT).show();
                    holder.BtnNotFav.setVisibility(View.VISIBLE);
                    holder.BtnFav.setVisibility(View.INVISIBLE);
                }else{
                    Toast.makeText(context, "Failed to remove to favorites", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.BtnNotFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean AddFavorite = musicHelper.insertFavorite(name);

                if (AddFavorite) {
                    Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show();
                    holder.BtnNotFav.setVisibility(View.INVISIBLE);
                    holder.BtnFav.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(context, "Failed to add favorites", Toast.LENGTH_SHORT).show();

                }
            }
        });



//        String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
//
//
//        holder.TxtSongName.setText(name);


    }



    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.songcard, parent, false);


        return new HomeViewHolder(view);
    }

    @Override
    public void swapCursor(Cursor newCursor) {
        super.swapCursor(newCursor);
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        public TextView TxtSongName;
        public Button BtnNotFav,BtnFav;
        public ConstraintLayout songLayout;

        public HomeViewHolder(@NonNull View v) {
            super(v);
            TxtSongName = v.findViewById(R.id.TxtSongName);
            BtnNotFav = v.findViewById(R.id.BtnNotFav);
            BtnNotFav = v.findViewById(R.id.BtnNotFav);
            BtnFav = v.findViewById(R.id.BtnFav);
            BtnFav.setVisibility(View.INVISIBLE);
            songLayout = (ConstraintLayout)v.findViewById(R.id.songLayout);
            songLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = getLayoutPosition();
                    Toast.makeText(context, "asd" + id, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
