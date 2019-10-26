package pl.wnb.communicator.model.sqlite;

import android.provider.BaseColumns;

public final class CommunicatorContract {
    public static final String sqlCreateUserEntry =
            "CREATE TABLE IF NOT EXISTS " + UserEntry.tableName + " (" +
                    UserEntry._ID + " INTEGER PRIMARY KEY," +
                    UserEntry.columnNameUsername + " TEXT," +
                    UserEntry.columnNameLoggedUsername + " TEXT," +
                    UserEntry.columnNameEmail + " TEXT," +
                    UserEntry.columnNamePassword + " TEXT," +
                    UserEntry.columnNamePrivateKey + " TEXT," +
                    UserEntry.columnNamePublicKey + " TEXT)";

    public static final String sqlCreateMessageEntry =
            "CREATE TABLE IF NOT EXISTS " + MessageEntry.tableName + " (" +
                    MessageEntry._ID + " INTEGER PRIMARY KEY," +
                    MessageEntry.columnNameSender + " TEXT," +
                    MessageEntry.columnNameTime + " TEXT," +
                    MessageEntry.columnNameMessage + " TEXT," +
                    MessageEntry.columnNameMyMsg + " INTEGER)";

    public static final String sqlDeleteUserEntry =
            "DROP TABLE IF EXISTS " + UserEntry.tableName;

    public static final String sqlDeleteMessageEntry =
            "DROP TABLE IF EXISTS " + MessageEntry.tableName;

    private CommunicatorContract() {
    }

    public static class UserEntry implements BaseColumns {
        public static final String tableName = "User_";
        public static final String columnNameUsername = "username";
        public static final String columnNameLoggedUsername = "loggedUsername";
        public static final String columnNameEmail = "email";
        public static final String columnNamePassword = "password";
        public static final String columnNamePrivateKey = "privateKey";
        public static final String columnNamePublicKey = "publicKey";
    }

    public static class MessageEntry implements BaseColumns {
        public static final String tableName = "Message_";
        public static final String columnNameSender = "sender";
        public static final String columnNameTime = "time";
        public static final String columnNameMessage = "message";
        public static final String columnNameMyMsg = "myMsg";
    }
}
