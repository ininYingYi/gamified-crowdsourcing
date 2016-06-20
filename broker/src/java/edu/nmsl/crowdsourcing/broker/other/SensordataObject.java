package edu.nmsl.crowdsourcing.broker.other;

import java.io.Serializable;

public class SensordataObject implements Serializable{
	int queryID = 0;
	String userID;
	String location;
	String sensorType;
	String time;
	String sensorValues;
	int number_values;
	
	public SensordataObject (int queryID, String userID, String location,
			String sensorType, String time, String sensorValues, int number_values){
		this.queryID = queryID;
		this.userID = userID;
		this.location = location;
		this.sensorType = sensorType;
		this.time = time;
		this.sensorValues = sensorValues;
		this.number_values = number_values;
	}
	
	public void setQueryID (int queryID) {
		this.queryID = queryID;
	}
	public void setUserID (String userID) {
		this.userID = userID;
	}
	public void setLocation (String location) {
		this.location = location;
	}
	public void setSensortype (String sensorType) {
		this.sensorType = sensorType;
	}
	public void setTime (String time) {
		this.time = time;
	}
	public void setSensorvalues (String sensorValues) {
		this.sensorValues = sensorValues;
	}
	public void setNumberValues (int number_values) {
		this.number_values = number_values;
	}
	
	public int getQueryID () {
		return queryID;
	}
	public String getUserID () {
		return userID;
	}
	public String getLocation () {
		return location;
	}
	public String getSensortype () {
		return sensorType;
	}
	public String getTime () {
		return time;
	}
	public String getSensorvalues () {
		return sensorValues;
	}
	public int getNumberValues () {
		return number_values;
	}
}
