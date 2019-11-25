package com.example.spotifyclone.ui.favorites;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifyclone.MusicHelper;
import com.example.spotifyclone.R;
import com.example.spotifyclone.SingletonArraylist;
import com.example.spotifyclone.objects.favorite;

import java.io.File;
import java.util.ArrayList;

public class FavoritesFragment extends Fragment
    implements LoaderManager.LoaderCallbacks<Cursor>
{

    RecyclerView RecyclerView;
    MusicHelper musicHelper;
    Cursor mCursor;
    ArrayList<favorite> favorites;
    ArrayList <File> songs = SingletonArraylist.getInstance(getActivity()).getdefaultlist();
    FavoriteCursorAdapter favoriteCursorAdapter;
    MyFavoriteCursorAdapter myFavoriteCursorAdapter;
    private static final int FAVORITE_LOADER = 1;
    Cursor myCursor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_favorites, container, false);

        RecyclerView = v.findViewById(R.id.RecyclerView);
        RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        musicHelper = new MusicHelper(getActivity());
        getLoaderManager().initLoader(FAVORITE_LOADER, null, this);
        myFavoriteCursorAdapter = new MyFavoriteCursorAdapter(getActivity(), songs);
        RecyclerView.setAdapter(myFavoriteCursorAdapter);
//        myFavoriteCursorAdapter = new MyFavoriteCursorAdapter(getActivity(), new favoritecallback() {
//            @Override
//            public void DeleteFavorite(String Name) {
//                boolean delete = musicHelper.deleteFavoriteByName(Name);
//
//                if (delete){
//                    Toast.makeText(getActivity(), "Removed to Favorites", Toast.LENGTH_SHORT).show();
//
//
//                }else{
//                    Toast.makeText(getActivity(), "Failed to remove to favorites", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//        RecyclerView.setAdapter(myFavoriteCursorAdapter);
//        mCursor = musicHelper.getFav();
//        String[] projection = {
//                DBContract.FavoriteEntry.COL_NAME
//        };
//
//        favoriteCursorAdapter = new FavoriteCursorAdapter(getActivity(), mCursor, new favoritecallback() {
//            @Override
//            public void DeleteFavorite(int Id) {
//                boolean delete = musicHelper.deleteFavorite(Id);
//                if (delete){
//
//                    mCursor.requery();
//                    favoriteCursorAdapter.notifyDataSetChanged();
//                }
//            }
//        });


        return v;


    }
//
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        if (id == 1){
            return favoriteLoader();
        }
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        myFavoriteCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        myFavoriteCursorAdapter.swapCursor(null);
    }


    private Loader<Cursor> favoriteLoader() {
        Uri uri = FavoriteProvider.CONTENT_URI;
        String[] projection = {
                FavoriteDb.KEY_ROWID,
                FavoriteDb.KEY_NAME
        };
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = FavoriteDb.KEY_ROWID + " DESC";

        return new CursorLoader(
                getActivity(),
                uri,
                projection,
                selection,
                selectionArgs,
                sortOrder);
    }
}
