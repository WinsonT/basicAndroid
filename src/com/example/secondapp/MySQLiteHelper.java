package com.example.secondapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//best practice to use 1 class per table
public class MySQLiteHelper extends SQLiteOpenHelper{
	//a table named chatlog
	public static final String TABLE_CHATLOG = "chatlog";
	//columns
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_TIMESTAMP = "timestamp";
	public static final String COLUMN_COMMENT = "comment";
	public static final String COLUMN_MOVE = "move";
	//a database named chatlog.db
	public static final String DATABASE_NAME = "chatlog.db";
	public static final int DATABASE_VERSION = 1;
	
	//database creation SQL statement
	//these integers are actually long
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_CHATLOG + "(" + COLUMN_ID 
			+ " integer primary key autoincrement, " 
			+ COLUMN_TIMESTAMP + " integer not null, "
			+ COLUMN_COMMENT + " text not null, "
			+ COLUMN_MOVE + " integer not null);";
	
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
    public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
	    "Upgrading database from version " + oldVersion + " to "
		+ newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHATLOG);
	    onCreate(db);
	 }
}
