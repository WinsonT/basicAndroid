package com.example.secondapp;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;


public class MainActivity extends ListActivity {
	private ChatDataSource datasource;
	public final static String EXTRA_MESSAGE = "com.example.secondapp.MESSAGE";
	public computerPlay computer = new computerPlay();
	public static final String PREFS_NAME = "com.example.secondapp.GamesRecord";
	public int gamesThisSession = 0;
	public int winsThisSession = 0;
	public int losesThisSession = 0;
	public int drawsThisSession = 0;
	public int rocksThisSession = 0;
	public int papersThisSession = 0;
	public int scissorsThisSession = 0;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        gamesThisSession = settings.getInt("GAMES", 0);
        winsThisSession = settings.getInt("WINS", 0);
        losesThisSession = settings.getInt("LOSES", 0);
        drawsThisSession = settings.getInt("DRAWS", 0);
        rocksThisSession = settings.getInt("ROCKS", 0);
        papersThisSession = settings.getInt("PAPERS", 0);
        scissorsThisSession = settings.getInt("SCISSORS", 0);


        
        //load database and display data
        datasource = new ChatDataSource(this);
        

        datasource.open();
                
        List<Chat> values = datasource.getAllChats();

        //SimpleCursorAdapter to show elements in ListView
        //simple list item 1 is a built in xml layout
        ArrayAdapter<Chat> adapter = new ArrayAdapter<Chat>(this,
        	android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    //returns -1 on draw, 1 if first argument wins, 0 otherwise
    public int isWin(int a, int b){
    	if (a == b) return -1;
    	else { if ((a-b == 1) || (b - a == 2)) return 1;
    	else return 0;
    	}
    }
    
    public void sendRock(View view) {
    	move(0);
    	rocksThisSession += 1;
    	createChatMessage("Rock!", 0);
    }
    
    public void sendPaper(View view) {
    	move(1);
    	papersThisSession += 1;
    	createChatMessage("Paper!", 1);
    }
    
    public void sendScissors(View view) {
    	move(2);
    	scissorsThisSession += 1;
    	createChatMessage("Scissors!", 2);
    }
    
    public void move(int playerMove) {
        //Rock Paper Scissor Game
        //computer's move
        int computerMove = computer.move();
        
        int win = isWin(playerMove,computerMove);
        
        //update database (chat log)
        //your and computer's move e.g., 01 means rock and paper
        //time as long int
        //number of games, wins, loses, and draws
        gamesThisSession += 1;
        if (win == 1) winsThisSession += 1;
        else {if (win == 0) losesThisSession += 1;
        else drawsThisSession += 1;}
        //dont forget to add time and moves into database
        
        //update display
    
    }

    /** Called when the user clicks the Send button */
    public void sendMessage(View view){
    	EditText editText = (EditText) findViewById(R.id.edit_message);
    	String message = editText.getText().toString();
    	
    	if (message.equals("")) {
    		return;
    	}
    	editText.setText("");
    	createChatMessage(message, -1);
    }
    
    public void createChatMessage(String message, int move){ 
    	@SuppressWarnings("unchecked")
    	ArrayAdapter<Chat> adapter = (ArrayAdapter<Chat>) getListAdapter();
    	
    	//create chat (auto update database)
    	Chat chat = datasource.createChat(message, move);
    	
    	//add chat to the list, then update view
    	adapter.add(chat);
    	adapter.notifyDataSetChanged();
    }
    
    public void viewStats(View view) {
        //update display
    	Intent intent = new Intent(this, ViewStatsActivity.class);
    	String message = "";
    	
    	message = "Games: " + String.valueOf(gamesThisSession) + "\n" +
    			"Wins: " + String.valueOf(winsThisSession) + "\n" +
    			"Loses: " + String.valueOf(losesThisSession) + "\n" + 
    			"Draws: " + String.valueOf(drawsThisSession) + "\n\n" +
    			"Rocks: " + String.valueOf(rocksThisSession) + "\n" + 
    			"Papers: " + String.valueOf(papersThisSession) + "\n" + 
    			"Scissors: " + String.valueOf(scissorsThisSession) + "\n";
        	
    	intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);       
    }
    
    @Override
    protected void onResume() {
      datasource.open();
      super.onResume();
    }

    @Override
    protected void onPause() {
      datasource.close();
      super.onPause();
    }    
    
    protected void onStop(){
    	super.onStop();

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("GAMES", gamesThisSession);
        editor.putInt("WINS", winsThisSession);
        editor.putInt("LOSES", losesThisSession);
        editor.putInt("DRAWS", drawsThisSession);
        editor.putInt("ROCKS", rocksThisSession);
        editor.putInt("PAPERS", papersThisSession);
        editor.putInt("SCISSORS", scissorsThisSession);
        
        //write to database
        
        // Commit the edits!
        editor.commit();
    }
    
}
