package edu.nmsl.crowdsourcing.broker.database;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.nmsl.crowdsourcing.broker.other.Answer;
import edu.nmsl.crowdsourcing.broker.other.FusionData;
import edu.nmsl.crowdsourcing.broker.other.LoginFile;
import edu.nmsl.crowdsourcing.broker.other.Query;
import edu.nmsl.crowdsourcing.broker.other.SensordataObject;

import com.mysql.jdbc.ResultSet;

public class Function {
	
	DatabaseController db;
	int maxReturnedQueries = 30;
	
	public Function(String database){
		db = new DatabaseController(database);
	}
	
	public boolean checkUser(LoginFile login){
		ResultSet r = db.select(db.userTable, "user_id = '" + login.name + "'");
		
		try {
			if(!r.next()){
				db.insertUser(login.name, login.password);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("SQLException");
			e.printStackTrace();
		}
		
		return true;
	}
	
	public void initialCurrentQueryID(){
		db.initialCurrentQueryID();
	}
	
	synchronized public void insertUser(String id, String pass){
		db.insertUser(id, pass);
	}
	
	synchronized public void insertAnswer(Answer a){
		db.insertAnswer(a);
	}
	
	synchronized public void insertSensordata(SensordataObject so){
		db.insertSensordata(so);
	}
	
	synchronized public void insertQuery(Query q){
		db.insertQuery(q);
	}
	
	synchronized public Query[] getQueries(String userID){
		ResultSet r = db.select(db.queryTable, "user_id != '" + userID + "' order by query_id DESC");
		ArrayList<Query> list = new ArrayList<Query>();
		Query[] queries = new Query[1];
		int count = 0;
		try {
			while(r.next() && count<maxReturnedQueries){
				if(!db.select(db.answerTable,"query_id = " + r.getInt("query_id") + " and answerer_id = '" + userID + "'").next()){
					count++;
					Timestamp st = r.getTimestamp("start_time");
					Timestamp et = r.getTimestamp("end_time");
					double[] leftTop = new double[]{r.getDouble("left_top_lat"), r.getDouble("left_top_lon")};
					double[] rightTop = new double[]{r.getDouble("right_top_lat"), r.getDouble("right_top_lon")};
					double[] leftBot = new double[]{r.getDouble("left_bot_lat"), r.getDouble("left_bot_lon")};
					double[] rightBot = new double[]{r.getDouble("right_bot_lat"), r.getDouble("right_bot_lon")};
					double[] userLocation = new double[]{r.getDouble("user_location_lat"), r.getDouble("user_location_lon")};
					
					Query q = new Query(r.getInt("query_id"), r.getString("content"), timestampToDate(st), 
							timestampToHour(st), timestampToDate(et), 
							timestampToHour(et), r.getString("user_id"),
							leftTop, rightTop,
							leftBot, rightBot,
							userLocation);
					
					list.add(q);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("SQLException");
		}
		
		if(list.size()==0){
			return null;
		}
		
		return list.toArray(queries);
	}
	
	synchronized public Query[] getMyQueries(String userID){
		ResultSet r = db.select(db.queryTable, "user_id = '" + userID + "' order by query_id DESC");
		ArrayList<Query> list = new ArrayList<Query>();
		Query[] queries = new Query[1];
		int count = 0;
		try {
			while(r.next() && count<maxReturnedQueries){
				if(!db.select(db.answerTable,"query_id = " + r.getInt("query_id") + " and answerer_id = '" + userID + "'").next()){
					count++;
					Timestamp st = r.getTimestamp("start_time");
					Timestamp et = r.getTimestamp("end_time");
					double[] leftTop = new double[]{r.getDouble("left_top_lat"), r.getDouble("left_top_lon")};
					double[] rightTop = new double[]{r.getDouble("right_top_lat"), r.getDouble("right_top_lon")};
					double[] leftBot = new double[]{r.getDouble("left_bot_lat"), r.getDouble("left_bot_lon")};
					double[] rightBot = new double[]{r.getDouble("right_bot_lat"), r.getDouble("right_bot_lon")};
					double[] userLocation = new double[]{r.getDouble("user_location_lat"), r.getDouble("user_location_lon")};
					
					Query q = new Query(r.getInt("query_id"), r.getString("content"), timestampToDate(st), 
							timestampToHour(st), timestampToDate(et), 
							timestampToHour(et), r.getString("user_id"),
							leftTop, rightTop,
							leftBot, rightBot,
							userLocation);
					
					list.add(q);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("SQLException");
		}
		
		if(list.size()==0) return null;
		return list.toArray(queries);
	}

	synchronized public Answer[] getAnswersOfMyQueries(String userName){
		ResultSet r = db.select(db.queryTable, "user_id = '" + userName + "'");
		ArrayList<Answer> list = new ArrayList<Answer>();
		
		
		Answer[] answers = new Answer[1];
		int count = 0;
		try {
			while(r.next() && count<maxReturnedQueries){
				count++;
				ResultSet tmp = db.select(db.answerTable, "query_id = " + r.getInt("query_id"));
				if(tmp.next()){
					double[] d = new double[]{tmp.getDouble("answerer_location_lat"), tmp.getDouble("answerer_location_lon")};
					
					Answer a = new Answer(tmp.getInt("query_id"), tmp.getString("answerer_id"), tmp.getString("content"), 
							timestampToTime(tmp.getTimestamp("answer_time")), latLngToString(d));
					
					list.add(a);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("SQLException");
		}
		
		if(list.size()==0){
			return null;
		}
		
		return list.toArray(answers);
	}
	
	synchronized public FusionData getFusionResultOfMyQuery (int queryID) {
		FusionData fd = null;
		ResultSet rs = db.select(db.fusionTable, "query_id = "+queryID+ " ORDER BY data_reading_timestamp DESC");
		try {
			if (rs.next()) {
				double[] fusion_variances = new double[3];
				double[] estimate_values = new double[3];
				for (int i=0; i<3; i++) {
					fusion_variances[i] = rs.getDouble("variances"+i);
					estimate_values[i] = rs.getDouble("estimate_value"+i);
				}
				fd = new FusionData (rs.getInt("query_id"), rs.getString("answerer_id"), rs.getString("sensor_type"),
						rs.getInt("number_values"), rs.getInt("number_sensors"), 
						rs.getTimestamp("data_reading_timestamp")+"", 
						fusion_variances[0], fusion_variances[1], fusion_variances[2],
						estimate_values[0], estimate_values[1], estimate_values[2]);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("SQLException in getFusionData");
		}
		
		return fd;
	}
	
	public String timestampToDate(Timestamp time){
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		return df.format(time);
	}
	
	public String timestampToHour(Timestamp time){
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		return df.format(time);
	}
	
	public String timestampToTime(Timestamp time){
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd,HH:mm:ss");
		return df.format(time);
	}
	
	public String latLngToString(double[] d){
		return d[0] + "," + d[1];
	}
	
	public String previousDay(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		Timestamp t = new Timestamp(System.nanoTime());
		
		try {
			Date d = df.parse(df.format(t));
			Timestamp today = new Timestamp(d.getTime());
			Timestamp yesterday = new Timestamp(today.getTime()-86400000);
			
			return df.format(yesterday);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "2014/01/01";
	}
}
