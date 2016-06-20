package edu.nmsl.crowdsourcing.broker.database;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.nmsl.crowdsourcing.broker.other.Answer;
import edu.nmsl.crowdsourcing.broker.other.FileIO;
import edu.nmsl.crowdsourcing.broker.other.Query;
import edu.nmsl.crowdsourcing.broker.other.SensordataObject;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.DatabaseMetaData;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

public class DatabaseController {

	public static final String queryTable = "QAquery";
	public static final String answerTable = "QAquery_answer";
	public static final String userTable = "QAuser";
	public static final String sensorTable = "QAsensordata";
	public static final String fusionTable = "QAfusiondata";
	
	Connection con;
	Statement stat = null;
	PreparedStatement preStat = null;
	double currentQueryID = 0;
	String database;
	
	public DatabaseController(String database){
		this.database = database;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = (Connection) DriverManager.getConnection("jdbc:"+ database,"sensorFusion","sensorFusion");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			DatabaseMetaData dbm = (DatabaseMetaData) con.getMetaData();
			
			//create tables if not exists
			ResultSet tables = (ResultSet) dbm.getTables(null, null, userTable, null);
			if(!tables.next()){
				createTable("CREATE TABLE " + userTable + " ("
						+ "user_id VARCHAR(120)"
						+ ", user_password VARCHAR(120))");
			}
			
			tables = (ResultSet) dbm.getTables(null, null, queryTable, null);
			if(!tables.next()){
				createTable("CREATE TABLE " + queryTable + " ("
						+ "query_id int(11)"
						+ ", user_id VARCHAR(120)"
						+ ", start_time datetime"
						+ ", end_time datetime"
						+ ", content TEXT"
						+ ", left_top_lat double"
						+ ", left_top_lon double"
						+ ", right_top_lat double"
						+ ", right_top_lon double"
						+ ", left_bot_lat double"
						+ ", left_bot_lon double"
						+ ", right_bot_lat double"
						+ ", right_bot_lon double"
						+ ", user_location_lat double"
						+ ", user_location_lon double)");
			}
			
			tables = (ResultSet) dbm.getTables(null, null, answerTable, null);
			if(!tables.next()){
				createTable("CREATE TABLE " + answerTable + " ("
						+ "query_id int(11)"
						+ ", answerer_id VARCHAR(120)"
						+ ", content TEXT"
						+ ", answer_time datetime"
						+ ", answerer_location_lat double"
						+ ", answerer_location_lon double)");
			}
			
			tables = (ResultSet) dbm.getTables(null, null, sensorTable, null);
			if (!tables.next()) {
				createTable("CREATE TABLE " + sensorTable + " ("
						+ "query_id int(11)"
						+ ", answerer_id VARCHAR(120)"
						+ ", answerer_location_lat double"
						+ ", answerer_location_lon double"
						// XXX: assume to be Accelerometer currently
						+ ", sensor_type VARCHAR(120)"
						+ ", number_values int(11)"
						// time when the data is sensed on the smartphone
						+ ", data_reading_timestamp datetime"
						// time when the data is sent to the database and recorded
						+ ", data_record_timestamp timestamp DEFAULT CURRENT_TIMESTAMP"
						+ ", sensor_value0 double"
						+ ", sensor_value1 double"
						+ ", sensor_value2 double"
						+ ")");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("SQLException");
			e.printStackTrace();
		}
		
		System.out.println("database connected");
	}
	
	synchronized public void insertUser(String id, String pass){
		try {
			preStat = (PreparedStatement) con.prepareStatement("INSERT INTO " + userTable +
					" (user_id, user_password) VALUES (?,?)");
			
			preStat.setString(1, id);
			preStat.setString(2, pass);
			
			preStat.executeUpdate();
			
			preStat.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("SQLException");
			e.printStackTrace();
		}
	}
	
	synchronized public void insertQuery(Query q){
		try {
			preStat = (PreparedStatement) con.prepareStatement("INSERT INTO " + queryTable + "(" +
					"query_id," +
					"user_id," +
					"start_time," +
					"end_time," +
					"content," +
					"left_top_lat," +
					"left_top_lon," +
					"right_top_lat," +
					"right_top_lon," +
					"left_bot_lat," +
					"left_bot_lon," +
					"right_bot_lat," +
					"right_bot_lon," +
					"user_location_lat," +
					"user_location_lon) " +
					"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
			currentQueryID++;
			preStat.setDouble(1, currentQueryID);
			preStat.setString(2, q.getUserName());
			preStat.setTimestamp(3, toTimestamp(q.getStartDate() + "," + q.getStartTime()));
			preStat.setTimestamp(4, toTimestamp(q.getEndDate() + "," + q.getEndTime()));
			preStat.setString(5, q.getTitle());
			
			double[] d = q.getLeftTop();
			preStat.setDouble(6, d[0]);
			preStat.setDouble(7, d[1]);
			
			d = q.getRightTop();
			preStat.setDouble(8, d[0]);
			preStat.setDouble(9, d[1]);
			
			d = q.getLeftBottom();
			preStat.setDouble(10, d[0]);
			preStat.setDouble(11, d[1]);
			
			d = q.getRightBottom();
			preStat.setDouble(12, d[0]);
			preStat.setDouble(13, d[1]);
			
			d = q.getCurrentLocation();
			preStat.setDouble(14, d[0]);
			preStat.setDouble(15, d[1]);
			
			preStat.executeUpdate();
			
			preStat.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("SQLException");
			e.printStackTrace();
		}
	}
	
	synchronized public void insertAnswer(Answer a){
		try {
			preStat = (PreparedStatement) con.prepareStatement("INSERT INTO " + answerTable + "(" +
					"query_id," +
					"answerer_id," +
					"content," +
					"answer_time," +
					"answerer_location_lat, " +
					"answerer_location_lon) " +
					"VALUES (?,?,?,?,?,?)");
			
			preStat.setInt(1, a.getQueryID());
			preStat.setString(2, a.getUserID());
			preStat.setString(3, a.getContent());
			
			preStat.setTimestamp(4, toTimestamp2(a.getTime()));
			
			double[] d = latLngStringToDouble(a.getLocation());
			preStat.setDouble(5, d[0]);
			preStat.setDouble(6, d[1]);
			
			preStat.executeUpdate();
			
			preStat.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("SQLException");
			e.printStackTrace();
		}
	}
	
	synchronized public void insertSensordata(SensordataObject so){
		String sqlStatement = 
				"INSERT INTO " + sensorTable + "(" +
				"query_id," +
				"answerer_id," +
				"answerer_location_lat, " +
				"answerer_location_lon, " +
				"sensor_type, " +
				"number_values, "+
				"data_reading_timestamp, ";
		if (!so.getSensorvalues().contains(",")) {
			sqlStatement += "sensor_value0) " +
							"VALUES (?,?,?,?,?,?,?,?)";
		} else if (so.getSensorvalues().split(",").length == 2) {
			sqlStatement += "sensor_value0, sensor_value1) " +
							"VALUES (?,?,?,?,?,?,?,?,?)";
		} else {
			sqlStatement += "sensor_value0, sensor_value1, sensor_value2) " +
							"VALUES (?,?,?,?,?,?,?,?,?,?)";
		}
		
		try {
			preStat = (PreparedStatement) con.prepareStatement(sqlStatement);
			preStat.setInt(1, so.getQueryID());
			preStat.setString(2, so.getUserID());
			double[] d = latLngStringToDouble(so.getLocation());
			preStat.setDouble(3, d[0]);
			preStat.setDouble(4, d[1]);
			preStat.setString(5, so.getSensortype());
			preStat.setInt(6, so.getNumberValues());
			preStat.setTimestamp(7, new Timestamp(Long.valueOf(so.getTime())));
			if (!so.getSensorvalues().contains(",")) {
				preStat.setDouble(8, Double.parseDouble(so.getSensorvalues()));
			} else {
				String[] tmpValues = so.getSensorvalues().split(",");
				System.out.println(so.getSensortype()+": "+tmpValues.length);
				for (int i=0; i<tmpValues.length; i++)
					preStat.setDouble(8+i, Double.parseDouble(tmpValues[i]));
			}
			
			preStat.executeUpdate();
			preStat.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("SQLException");
			e.printStackTrace();
		}
	}
	
	public ResultSet select(String table){
		ResultSet rs = null;
		try {
			stat = (Statement) con.createStatement();
			rs = (ResultSet) stat.executeQuery("SELECT * FROM " + table);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("SQLException");
			e.printStackTrace();
		}
		
		return rs;
	}
	
	public ResultSet select(String table, String condition){
		
		ResultSet rs = null;
		try {
			stat = (Statement) con.createStatement();
			rs = (ResultSet) stat.executeQuery("SELECT * FROM " + table + " WHERE " + condition);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("SQLException");
			//con = (Connection) DriverManager.getConnection("jdbc:"+ database,"simQA","simQA");
			e.printStackTrace();
		}
		
		return rs;
	}
	
	public ResultSet selectOrder(String table, String condition){
		ResultSet rs = null;
		try {
			stat = (Statement) con.createStatement();
			rs = (ResultSet) stat.executeQuery("SELECT * FROM " + table + " ORDER BY " + condition);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("SQLException");
			e.printStackTrace();
		}
		
		return rs;
	}
	
	public void deleteTable(String tableName){
		try {
			stat = (Statement) con.createStatement();
			stat.executeUpdate("DROP TABLE " + tableName);
			
			stat.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("deleted Table: " + tableName);
		
	}
	
	public void createTable(String sql){
		try {
			stat = (Statement) con.createStatement();
			stat.executeUpdate(sql);
			
			stat.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("created table : " + sql);
	}
	
	public void initialCurrentQueryID(){
		try {
			stat = (Statement) con.createStatement();
			ResultSet r = (ResultSet) stat.executeQuery("select count(*) from QAquery");
			r.next();
			this.currentQueryID = r.getInt("COUNT(*)");
			
			stat.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Timestamp toTimestamp(String time){
		try {
			Date d = new java.text.SimpleDateFormat("yyyy/MM/dd,HH:mm").parse(time);
			Timestamp ts = new Timestamp(d.getTime());
			return ts;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Timestamp toTimestamp2(String time){
		try {
			Date d = new java.text.SimpleDateFormat("yyyy/MM/dd,HH:mm:ss").parse(time);
			Timestamp ts = new Timestamp(d.getTime());
			return ts;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public double[] latLngStringToDouble(String s){
		String[] tmp = s.split(",");
		return new double[]{Double.valueOf(tmp[0]), Double.valueOf(tmp[1])};
	}
	
}
