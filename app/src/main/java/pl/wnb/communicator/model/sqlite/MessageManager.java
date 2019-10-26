package pl.wnb.communicator.model.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import pl.wnb.communicator.model.Message;
import pl.wnb.communicator.model.util.ContextUtil;

public class MessageManager {

    private CommunicatorDbHelper dbHelper;
    private SQLiteDatabase db;

    public MessageManager(){
        dbHelper = new CommunicatorDbHelper(ContextUtil.getAppContext());
        db = dbHelper.getWritableDatabase();
    }

    public void deleteWithName(String name){
        String selection = CommunicatorContract.MessageEntry.columnNameSender + " LIKE ?";
        String[] selectionArgs = { name };
        db.delete(CommunicatorContract.MessageEntry.tableName, selection, selectionArgs);
    }

    public void deleteAll(){
        db.delete(CommunicatorContract.MessageEntry.tableName, null, null);
    }

    @SuppressLint("SimpleDateFormat")
    public void addMessage(String name, String message, int myMsg) {
        ContentValues values = new ContentValues();
        values.put(CommunicatorContract.MessageEntry.columnNameSender, name);
        values.put(CommunicatorContract.MessageEntry.columnNameTime, new SimpleDateFormat("HH.mm").format(new Date()));
        values.put(CommunicatorContract.MessageEntry.columnNameMessage, message);
        values.put(CommunicatorContract.MessageEntry.columnNameMyMsg, myMsg);
        db.insert(CommunicatorContract.MessageEntry.tableName, null, values);
    }

    public ArrayList<Message> getMessagesWithUser(String name){
        String[] projection = {
                BaseColumns._ID,
                CommunicatorContract.MessageEntry.columnNameSender,
                CommunicatorContract.MessageEntry.columnNameTime,
                CommunicatorContract.MessageEntry.columnNameMessage,
                CommunicatorContract.MessageEntry.columnNameMyMsg
        };

        String selection = CommunicatorContract.MessageEntry.columnNameSender + " = ?";
        String[] selectionArgs = {name};

        Cursor cursor = db.query(
                CommunicatorContract.MessageEntry.tableName,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        ArrayList<Message> messagesList = new ArrayList<>();

        while (cursor.moveToNext()) {
            messagesList.add(new Message(
                    cursor.getString(cursor.getColumnIndex(CommunicatorContract.MessageEntry.columnNameSender)),
                    cursor.getString(cursor.getColumnIndex(CommunicatorContract.MessageEntry.columnNameMessage)),
                    cursor.getString(cursor.getColumnIndex(CommunicatorContract.MessageEntry.columnNameTime)),
                    cursor.getInt(cursor.getColumnIndex(CommunicatorContract.MessageEntry.columnNameMyMsg)) == 1));

        }
        cursor.close();
        return messagesList;
    }
}
