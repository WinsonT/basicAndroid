package com.example.secondapp;

public class Chat {
	private long id, timestamp;
	private String comment;
	private int move;

	public Chat(long a1, String a2, int a3){
		this(0, a1, a2, a3);
	}

	public Chat(long a0, long a1, String a2, int a3){
		id = a0;
		timestamp = a1;
		comment = a2; //-1 if not a move (i.e., a comment)
		move = a3; //"" if not a comment
	}

	
	public long getId() {return id;}
	public long getTime() {return timestamp;}
	public String getComment() {return comment;}
	public int getMove() {return move;}
	
	
	
	public void setId(long i) {id = i;}
	
	@Override
	//method for ListView arrayAdapter
	public String toString() {return comment;}
}
