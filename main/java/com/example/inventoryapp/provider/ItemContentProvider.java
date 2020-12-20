package com.example.inventoryapp.provider;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.example.inventoryapp.MyDBHandler;

public class ItemContentProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.inventoryapp.provider.ItemContentProvider";
    private static final String ITEMS_TABLE = "items";
    public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY +"/"+ ITEMS_TABLE);

    public static final int ITEMS = 2;
    public static final int ITEMS_CODE =3;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY,ITEMS_TABLE,ITEMS);
        sURIMatcher.addURI(AUTHORITY,ITEMS_TABLE + "/#",ITEMS_CODE);
    }

    private MyDBHandler myDB;
    public ItemContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = myDB.getWritableDatabase();
        int rowsDeleted = 0;

        switch (uriType) {
            case ITEMS:
                rowsDeleted = sqlDB.delete(MyDBHandler.TABLE_ITEMS,selection,selectionArgs);
                break;
            case ITEMS_CODE:
                String id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(MyDBHandler.TABLE_ITEMS,MyDBHandler.COLUMN_CODE + "=" + id, null);
                } else{
                    rowsDeleted = sqlDB.delete(MyDBHandler.TABLE_ITEMS,MyDBHandler.COLUMN_CODE + "=" + id + " and " +
                            selection,selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: "+ uri);

        }
        getContext().getContentResolver().notifyChange(uri,null);
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = myDB.getWritableDatabase();

        long id = 0;
        switch (uriType) {
            case ITEMS:
                id = sqlDB.insert(MyDBHandler.TABLE_ITEMS, null, values);
                break;
            default:
                throw new IllegalArgumentException("UNknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uri.parse(ITEMS_TABLE + "/" + id);

    }

    @Override
    public boolean onCreate() {
        myDB = new MyDBHandler(getContext(),null, null,1);
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MyDBHandler.TABLE_ITEMS);

        int uriType = sURIMatcher.match(uri);

        switch(uriType) {
            case ITEMS_CODE:
                queryBuilder.appendWhere(MyDBHandler.COLUMN_CODE + "="
                        + uri.getLastPathSegment());
                break;
            case ITEMS:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        Cursor cursor = queryBuilder.query(myDB.getReadableDatabase(),projection,selection, selectionArgs,null,null,sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = myDB.getWritableDatabase();
        int rowsUpdated =0;

        switch(uriType) {
            case ITEMS:
                rowsUpdated = sqlDB.update(MyDBHandler.TABLE_ITEMS,values,selection,selectionArgs);
                break;
            case ITEMS_CODE:
                String id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)){
                    rowsUpdated = sqlDB.update(MyDBHandler.TABLE_ITEMS,values,MyDBHandler.COLUMN_CODE + "=" +id,null);
                } else {
                    rowsUpdated = sqlDB.update(MyDBHandler.TABLE_ITEMS, values,MyDBHandler.COLUMN_CODE + "="+id + " and " +
                            selection,selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: "+ uri);

        }
        getContext().getContentResolver().notifyChange(uri,null);
        return rowsUpdated;
    }
}
