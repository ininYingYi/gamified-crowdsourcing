package edu.nmsl.crowdsourcing.broker.other;

import java.io.Serializable;


public class Query implements Serializable{
	
	int queryID = 0;
	String title = "";
	
	String startDate = "";
	String startTime = "";
	String endDate = "";
	String endTime = "";
	String userName = "";
	
	double[] leftTop = new double[2];
	double[] leftBottom = new double[2];
	double[] rightTop = new double[2];
	double[] rightBottom = new double[2];
	
	double[] currentLocation = new double[2];
	
	public Query(){
		
	}
	
	public Query(int queryID, String title, String startDate, String startTime, 
			String endDate, String endTime, String userName, 
			double[] leftTop, double[] rightTop, double[] leftBottom, 
			double[] rightBottom, double[] currentLocation){
		this.queryID = queryID;
		this.title = title;
		this.startDate = startDate;
		this.startTime = startTime;
		this.endDate = endDate;
		this.endTime = endTime;
		this.userName = userName;
		this.leftTop = leftTop;
		this.leftBottom = leftBottom;
		this.rightTop = rightTop;
		this.rightBottom = rightBottom;
		this.currentLocation = currentLocation;
	}
	
	public void setLeftTop(double left, double top){
		leftTop[0] = left;
		leftTop[1] = top;
	}
	
	public void setLeftBottom(double left, double bottom){
		leftBottom[0] = left;
		leftBottom[1] = bottom;
	}
	
	public void setRightTop(double right, double top){
		rightTop[0] = right;
		rightTop[1] = top;
	}
	
	public void setRightBottom(double right, double bottom){
		rightBottom[0] = right;
		rightBottom[1] = bottom;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setStartDate(String date){
		this.startDate = date;
	}
	
	public void setStartTime(String time){
		this.startTime = time;
	}
	
	public void setEndDate(String date){
		this.endDate = date;
	}
	
	public void setEndTime(String time){
		this.endTime = time;
	}
	
	public void setUserName(String name){
		this.userName = name;
	}
	
	public void setCurrentLocation(double[] d){
		this.currentLocation[0] = d[0];
		this.currentLocation[1] = d[1];
	}
	
	public double[] getLeftTop(){
		return leftTop;
	}
	
	public double[] getLeftBottom(){
		return leftBottom;
	}
	
	public double[] getRightTop(){
		return rightTop;
	}
	
	public double[] getRightBottom(){
		return rightBottom;
	}
	
	public String getStartDate(){
		return this.startDate;
	}
	
	public String getStartTime(){
		return this.startTime;
	}
	
	public String getEndDate(){
		return this.endDate;
	}
	
	public String getEndTime(){
		return this.endTime;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public String getUserName(){
		return userName;
	}
	
	public int getQueryID(){
		return this.queryID;
	}
	
	public double[] getCenter(){
		double[] d = new double[2];
		d[0] = leftTop[0] + rightTop[0] + leftBottom[0] + rightBottom[0];
		d[0] = d[0]/4.0;
		d[1] = leftTop[1] + rightTop[1] + leftBottom[1] + rightBottom[1];
		d[1] = d[1]/4.0;
		
		return d;
	}
	
	public double[] getCurrentLocation(){
		return currentLocation;
	}
	
}
