package edu.nmsl.crowdsourcing.broker.other;

import java.io.Serializable;

public class Answer implements Serializable{

	int queryID = 0;
	String userID;
	String content;
	String time;
	String location;
	
	public Answer(int queryID, String userID, String content, String time, String location){
		this.queryID = queryID;
		this.userID = userID;
		this.content = content;
		this.time = time;
		this.location = location;
	}
	
	public int getQueryID(){
		return queryID;
	}
	
	public String getUserID(){
		return userID;
	}
	
	public String getContent(){
		return content;
	}
	
	public String getTime(){
		return time;
	}
	
	public String getLocation(){
		return location;
	}
}
