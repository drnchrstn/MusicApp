package com.example.spotifyclone.ui.playlist;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.spotifyclone.MusicHelper;

import java.net.MalformedURLException;

public class PlaylistProvider extends ContentProvider {

    MusicHelper musicHelper;
    private static final int ALL_PlAYLIST =1;
    private static final int SINGLE_PLAYLIST = 2;
    private static final String AUTHORITY = "com.example.spotifyclone.playlist.contentprovider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/playlists");
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "playlists", ALL_PlAYLIST);
        uriMatcher.addURI(AUTHORITY, "playlists/#", SINGLE_PLAYLIST);
    }


    @Override
    public boolean onCreate() {
        musicHelper = new MusicHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = musicHelper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(PlaylistDb.TABLE_NAME);
        switch (uriMatcher.match(uri)){
            case ALL_PlAYLIST:
                //nothing
                break;
            case SINGLE_PLAYLIST:
                String id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(PlaylistDb.KEY_ROWID + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " +uri);
        }
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        switch (uriMatcher.match(uri)){
            case ALL_PlAYLIST:
                return "vnd.android.cursor.dir/vnd.com.example.android.spotifyclone.contentprovider.playlists";
            case SINGLE_PLAYLIST:
                return "vnd.android.cursor.item/vnd.com.example.android.spotifyclone.contentprover.playlists";
            default:
                throw new IllegalArgumentException("Unsupported URI: " +uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        SQLiteDatabase db = musicHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)){
            case ALL_PlAYLIST:
                //nothing
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " +uri);
        }
        long id = db.insert(PlaylistDb.TABLE_NAME, null, values);
        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.parse(CONTENT_URI + "/" +id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase db = musicHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)){
            case ALL_PlAYLIST:
                //nothing
                break;
            case SINGLE_PLAYLIST:
                String id = uri.getPathSegments().get(1);
                selection = PlaylistDb.KEY_ROWID + "=" +id +
                        (!TextUtils.isEmpty(selection) ?
                                "AND ("+selection+')' : "");
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int deleteCount = db.delete(PlaylistDb.TABLE_NAME, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return deleteCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase db = musicHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)){
            case ALL_PlAYLIST:
                //do nothing
                break;
            case SINGLE_PLAYLIST:
                String id = uri.getPathSegments().get(1);
                selection = PlaylistDb.KEY_ROWID + "=" +id +
                        (!TextUtils.isEmpty(selection) ?
                                "AND ("+selection+')' : "");
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " +uri);
        }
        int updateCount = db.update(PlaylistDb.TABLE_NAME, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return updateCount;
    }
}
