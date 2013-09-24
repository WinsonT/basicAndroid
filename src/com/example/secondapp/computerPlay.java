package com.example.secondapp;

//import android.util.Log;


public class computerPlay {
	private int _last_move = -1;
	
	public computerPlay(){
	}
	
	public int move(){
		_last_move = (int) (Math.random()*3);
//		Log.w("",""+_last_move);
		return _last_move;
	}
}
