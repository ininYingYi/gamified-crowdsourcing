package edu.nmsl.crowdsourcing.broker.other;

import java.util.ArrayList;

public class QueryManager {

	static ArrayList<String> queries = new ArrayList<String>();
	
	synchronized static public void putInArrayList(String query){
		queries.add(query);
	}
}
