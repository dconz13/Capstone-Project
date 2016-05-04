package com.conz13.d.strongpasswordcreator.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by dillon on 4/17/16.
 */
public final class PasswordContract {

    public static final String CONTENT_AUTHORITY = "com.conz13.d.strongpasswordcreator.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Only path necessary to get the data for each item.
    public static final String PATH_DATA = "data";

    public PasswordContract(){

    }

    public static abstract class PasswordEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_DATA).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DATA;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DATA;

        public static final String TABLE_NAME = "password_table";

        // Title for the list item store as a String. Unique and not null.
        public static final String HEADER_TITLE = "header_title";
        // Username stored as a String. Can be null.
        public static final String USERNAME = "username";
        // Password from the generator stored as a String. Can be null.
        public static final String PASSWORD = "password";
        // Additional Info stored as a String. Can be null.
        public static final String ADD_INFO = "add_info";

        public static Uri buildPasswordUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getHeaderTitleFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }
    }
}
