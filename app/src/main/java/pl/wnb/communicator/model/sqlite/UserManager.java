package pl.wnb.communicator.model.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import pl.wnb.communicator.model.SqlUser;
import pl.wnb.communicator.model.util.ContextUtil;
import pl.wnb.communicator.model.util.GlobalUserUtil;

public class UserManager {

    private CommunicatorDbHelper dbHelper;
    private SQLiteDatabase db;

    public UserManager(){
        dbHelper = new CommunicatorDbHelper(ContextUtil.getAppContext());
        db = dbHelper.getWritableDatabase();
    }

    public void addUser(String name, String email, String password, String publicKey, String privateKey){
        dbHelper = new CommunicatorDbHelper(ContextUtil.getAppContext());
        db = dbHelper.getWritableDatabase();

        String selection = CommunicatorContract.UserEntry.columnNameLoggedUsername + " LIKE ?";
        String[] selectionArgs = { GlobalUserUtil.getInstance().getName() };
        db.delete(CommunicatorContract.UserEntry.tableName, selection, selectionArgs);

        ContentValues values = new ContentValues();
        values.put(CommunicatorContract.UserEntry.columnNameUsername, name);
        values.put(CommunicatorContract.UserEntry.columnNameLoggedUsername, GlobalUserUtil.getInstance().getName());
        values.put(CommunicatorContract.UserEntry.columnNameEmail, email);
        values.put(CommunicatorContract.UserEntry.columnNamePassword, password);
        values.put(CommunicatorContract.UserEntry.columnNamePrivateKey, privateKey);
        values.put(CommunicatorContract.UserEntry.columnNamePublicKey, publicKey);

        db.insert(CommunicatorContract.UserEntry.tableName, null, values);
    }

    public SqlUser getUser(){
        dbHelper = new CommunicatorDbHelper(ContextUtil.getAppContext());
        db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                CommunicatorContract.UserEntry.columnNameUsername,
                CommunicatorContract.UserEntry.columnNameLoggedUsername,
                CommunicatorContract.UserEntry.columnNameEmail,
                CommunicatorContract.UserEntry.columnNamePassword,
                CommunicatorContract.UserEntry.columnNamePrivateKey,
                CommunicatorContract.UserEntry.columnNamePublicKey
        };

        String selection = CommunicatorContract.UserEntry.columnNameLoggedUsername + " = ?";
        String[] selectionArgs = {GlobalUserUtil.getInstance().getName()};

        String sortOrder =
                CommunicatorContract.UserEntry.columnNameUsername + " DESC";

        Cursor cursor = db.query(
                CommunicatorContract.UserEntry.tableName,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        cursor.moveToFirst();
        return new SqlUser(
                cursor.getString(cursor.getColumnIndex(CommunicatorContract.UserEntry.columnNameUsername)),
                cursor.getString(cursor.getColumnIndex(CommunicatorContract.UserEntry.columnNameLoggedUsername)),
                cursor.getString(cursor.getColumnIndex(CommunicatorContract.UserEntry.columnNameEmail)),
                cursor.getString(cursor.getColumnIndex(CommunicatorContract.UserEntry.columnNamePassword)),
                cursor.getString(cursor.getColumnIndex(CommunicatorContract.UserEntry.columnNamePrivateKey)),
                cursor.getString(cursor.getColumnIndex(CommunicatorContract.UserEntry.columnNamePublicKey))
        );
    }
}
