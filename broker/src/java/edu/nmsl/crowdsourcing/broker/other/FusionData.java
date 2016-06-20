package edu.nmsl.crowdsourcing.broker.other;

import java.io.Serializable;

public class FusionData implements Serializable {
	private int query_id;
	private String answerer_id;
	private String sensor_type;
	private int number_values;
	private int number_sensors;
	private String data_reading_timestamp;
	//private String[] individualVariances;
	//private String[] weightVectors;
	//private double[] variances;
	//private double[] estimate_values;
	private double variances0;
	private double variances1;
	private double variances2;
	private double estimates0;
	private double estimates1;
	private double estimates2;
	
	public FusionData (int query_id, String answerer_id, String sensor_type,
					int number_values, int number_sensors, String data_reading_timestamp,
					double variances0, double variances1, double variances2, 
					double estimates0, double estimates1, double estimates2) {
		this.query_id = query_id;
		this.answerer_id = answerer_id;
		this.sensor_type = sensor_type;
		this.number_values = number_values;
		this.number_sensors = number_sensors;
		this.data_reading_timestamp = data_reading_timestamp;
		this.variances0 = variances0;
		this.variances1 = variances1;
		this.variances2 = variances2;
		this.estimates0 = estimates0;
		this.estimates1 = estimates1;
		this.estimates2 = estimates2;
	}

	public void setQueryID (int id) {
		this.query_id = id;
	}
	public void setAnswererID (String id) {
		this.answerer_id = id;
	}
	public void setSensortype (String type) {
		this.sensor_type = type;
	}
	public void setNumberValues (int num) {
		this.number_values = num;
	}
	public void setNumberSensors (int num) {
		this.number_sensors = num;
	}
	public void setReadingTime (String ts) {
		this.data_reading_timestamp = ts;
	}
	public void setVariances0 (double v) {
		this.variances0 = v;
	}
	public void setVariances1 (double v) {
		this.variances1 = v;
	}
	public void setVariances2 (double v) {
		this.variances2 = v;
	}
	public void setEstimates0 (double e) {
		this.estimates0 = e;
	}
	public void setEstimates1 (double e) {
		this.estimates1 = e;
	}
	public void setEstimates2 (double e) {
		this.estimates2 = e;
	}
	
	public int getQueryID () {
		return query_id;
	}
	public String getAnswererID (){
		return answerer_id;
	}
	public String getSensortype() {
		return sensor_type;
	}
	public int getNumberValues() {
		return number_values;
	}
	public int getNumberSensors() {
		return number_sensors;
	}
	public String getReadingTime() {
		return data_reading_timestamp;
	}
	public double getVariances0 () {
		return variances0;
	}
	public double getVariances1 () {
		return variances1;
	}
	public double getVariances2 () {
		return variances2;
	}
	public double getEstimate0 () {
		return estimates0;
	}
	public double getEstimate1 () {
		return estimates1;
	}
	public double getEstimate2 () {
		return estimates2;
	}
}
