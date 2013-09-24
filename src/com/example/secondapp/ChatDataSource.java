package com.example.secondapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ChatDataSource {
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = {MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_TIMESTAMP,
			MySQLiteHelper.COLUMN_COMMENT,
			MySQLiteHelper.COLUMN_MOVE};
	
	public ChatDataSource(Context context){
		dbHelper = new MySQLiteHelper(context);
	}
	
	public void open() throws SQLException{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public Chat createChat(String comment, int move){
		//create Chat object and
		Date date = new Date();
		Chat newChat = new Chat(date.getTime(), comment, move);
		//write into database
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_TIMESTAMP, newChat.getTime());
		values.put(MySQLiteHelper.COLUMN_COMMENT, newChat.getComment());
		values.put(MySQLiteHelper.COLUMN_MOVE, newChat.getMove());
		
		long insertId = database.insert(MySQLiteHelper.TABLE_CHATLOG, null, values);
		
		//to use cursor .query(String dbName, String[] columns, String whereClause
		//, String[] selectionArgs (replace ? if you include ? in whereClause),
		//String[] groupBy, String[] having (filter for the group), String[] orderBy);
//		Cursor cursor = database.query(MySQLiteHelper.TABLE_CHATLOG,
//		        allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
//		        null, null, null);
//		cursor.moveToFirst(); //access first row of query result
//	    cursor.close();
		
		//tell Chat object his id
		newChat.setId(insertId);
		return newChat;
	}
	
	public void deleteChat(Chat chat) {
	    long id = chat.getId();
	    System.out.println("Chat deleted with id: " + id);
	    database.delete(MySQLiteHelper.TABLE_CHATLOG, MySQLiteHelper.COLUMN_ID
	        + " = " + id, null);
	 }
	
	public List<Chat> getAllChats() {
	    List<Chat> chats = new ArrayList<Chat>();
        Log.w(ChatDataSource.class.getName(),allColumns[3]);

	    Cursor cursor = database.query(MySQLiteHelper.TABLE_CHATLOG,
	        allColumns, null, null, null, null, null);
        Log.w(ChatDataSource.class.getName(),"asdfgh");

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Chat chat = cursorToChat(cursor);
	      chats.add(chat);
	      cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();
	    return chats;
	  }
	
	private Chat cursorToChat(Cursor cursor) {
		Chat chat = new Chat(cursor.getLong(0), cursor.getLong(1), cursor.getString(2), cursor.getInt(3));
		return chat;
	}
}
