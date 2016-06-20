package edu.nmsl.crowdsourcing.broker.main;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import edu.nmsl.crowdsourcing.broker.database.DatabaseController;
import edu.nmsl.crowdsourcing.broker.database.Function;
import edu.nmsl.crowdsourcing.broker.other.Answer;
import edu.nmsl.crowdsourcing.broker.other.FusionData;
import edu.nmsl.crowdsourcing.broker.other.Query;
import edu.nmsl.crowdsourcing.broker.other.LoginFile;
import edu.nmsl.crowdsourcing.broker.other.SensordataObject;

public class ClientHandler {

	Socket clientSocket;
	ObjectInputStream is;
	ObjectOutputStream os;
	Function function;
	String userName;
	Query query;
	
	private static final String[] CLIENT_COMMANDS= {"", "", ""};
	
	public ClientHandler(Socket socket, Function function){
		clientSocket = socket;
		this.function = function;
		
		new Thread(){
			public void run(){
				handle();
			}
		}.start();
	}
	
	public void handle(){
		String request = "";
		
		try {
			os = new ObjectOutputStream(clientSocket.getOutputStream());
			is = new ObjectInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("get I/O stream failed");
			e.printStackTrace();
		}
		System.out.println("get I/O stream");
		
		while(true){
			try {
				//is.reset();
				request = is.readUTF();
			} catch(EOFException e){
				System.out.println("EOFException in clientHandler.handle(): no more request");
				break;
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("IOException in clientHandler.handle()");
				break;
			} catch(Exception e){
				System.out.println("Excpetion");
				e.printStackTrace();
				break;
			}
			System.out.println("get request: "+ request);
			
			if (request.equals("login")) {
				System.out.println("client login");
				login();
			} else if (request.equals("submitquery")) {
				System.out.println("receive query");
				processQuery();
			} else if (request.equals("throughput")) {
				System.out.println("measure throughput");
				measureThroughput();
			} else if (request.equals("getqueries")) {
				System.out.println("return queries to client");
				returnQueries();
			} else if (request.equals("echo")) {
				System.out.println("echo");
				echo();
			} else if (request.equals("submitAnswer")) {
				System.out.println("submitAnswer");
				getAnswer();
			} else if (request.equals("submitSensordata")) {
				System.out.println("submit sensor data");
				getSensordata();
			} else if (request.equals("getmyqueryanswers")) {
				System.out.println("getmyqueryanswers");
				returnAnswersOfMyQueries();
			} else if (request.equals("getmyqueryfusionresult")) {
				System.out.println("get my query fusion result");
				returnFusionResultOfMyQuery();
			} else if (request.equals("getmyqueries")) {
				System.out.println("getmyqueries");
				returnMyQueries();
			}
			System.out.println("process request okay: "+request);
		}
		
		try {
			System.out.println("request EOF: close client socket");
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOException");
			e.printStackTrace();
		} catch (Exception e){
			System.out.println("Exception");
			e.printStackTrace();
		}
		
	}
	
	public void login(){
		LoginFile loginFile = null;
		try {
			//is.reset();
			loginFile = (LoginFile) is.readObject();
			System.out.println("Client login information: "+loginFile.name+ ", "+loginFile.password);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("cannot read LoginFile from client");
			e.printStackTrace();
			return;
		} catch (ClassNotFoundException e){
			System.out.println("cannot read obejct as a loginFile");
			e.printStackTrace();
			return;
		} catch (Exception e){
			System.out.println("Exception");
			e.printStackTrace();
			return;
		}
		
		String response = "";
		
		if(function.checkUser(loginFile)){
			response = "OK";
			userName = loginFile.name;
		}else{
			response = "error";
		}
		
		try {
			os.writeUTF(response);
			os.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void processQuery(){
		query = null;
		
		try {
			query = (Query) is.readObject();
		}catch(IOException e){
			System.out.println("IOExcpetion");
			e.printStackTrace();
			return;
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("ClassNotFoundException");
			e.printStackTrace();
			return;
		}
		
		try {
			os.writeUTF("got the query");
			os.flush();
			double[] lt = query.getLeftTop();
			double[] rt = query.getRightTop();
			double[] lb = query.getLeftBottom();
			double[] rb = query.getRightBottom();
			double[] cl = query.getCurrentLocation();
			
			query.setUserName(userName);
			function.insertQuery(query);
			
			System.out.println("got the query from user : " + userName);
			
			System.out.println("user current location is : " + getLatLngString(cl));
			System.out.println("corners are " + getLatLngString(lt) + " " + getLatLngString(rt) + " " + getLatLngString(lb) + " " + getLatLngString(rb));
			System.out.println("title = " + query.getTitle());
			System.out.println("start from : " + query.getStartDate() + " " + query.getStartTime());
			System.out.println("end : " + query.getEndDate() + " " + query.getEndTime());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getLatLngString(double[] d){
		return d[0] + "," + d[1];
	}
	
	public void measureThroughput(){
		byte[] b = new byte[1024];
		
		try {
			int read = is.read(b);
			
			if(read>0){
				os.writeUTF("OK");
			}else{
				os.writeUTF("fail");
			}
			
			os.flush();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}
	
	public void returnQueries(){
		Query[] q = function.getQueries(userName);
		try {
			os.writeUTF("OK");
			int length = 0;
			if(q==null){
				os.writeUTF("" + 0);
			}else{
				os.writeUTF(q.length + "");
				length = q.length;
			}
			
			os.flush();
			
			for(int i=0;i<length;i++){
				if(q[i]!=null){
					os.reset();
					os.writeObject(q[i]);
					os.flush();
				}
			}
			
			System.out.println("write objects to client");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("failed to write query obects to client");
		} catch (Exception e){
			e.printStackTrace();
			System.out.println("Exception");
		}
	}

	public void returnMyQueries(){
		Query[] q = function.getMyQueries(userName);
		try {
			os.writeUTF("OK");
			int length = 0;
			if(q==null){
				os.writeUTF("" + 0);
			}else{
				os.writeUTF(q.length + "");
				length = q.length;
			}
			
			os.flush();
			
			for(int i=0;i<length;i++){
				if(q[i]!=null){
					os.reset();
					os.writeObject(q[i]);
					os.flush();
				}
			}
			
			System.out.println("write query objects to client");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("failed to write query objects to client");
		} catch (Exception e){
			e.printStackTrace();
			System.out.println("Exception");
		}
	}
	
	public void returnAnswersOfMyQueries(){
		Answer[] a = null;
		try {
			String userName = is.readUTF();
			os.writeUTF("OK");
			os.flush();
			
			a = function.getAnswersOfMyQueries(userName);
			if(a==null){
				os.writeUTF(0 + "");
				os.flush();
				return;
			}else{
				os.writeUTF(a.length+"");
				os.flush();
			}
			
			
			for(int i=0;i<a.length;i++){
				if(a[i]!=null){
					os.reset();
					os.writeObject(a[i]);
					os.flush();
				}
			}
			
			System.out.println("write objects to client");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("failed to write query obects to client");
		} catch (Exception e){
			e.printStackTrace();
			System.out.println("Exception");
		}
	}
	
	public void returnFusionResultOfMyQuery() {
		FusionData fd = null;
		try {
			int queryID = (Integer.valueOf(is.readUTF())).intValue();
			System.out.println("(Fusion) queryID: "+ queryID);
			os.writeUTF("OK");
			os.flush();
			
			fd = function.getFusionResultOfMyQuery(queryID);
			if (fd == null) {
				os.writeUTF(0+"");
				os.flush();
				return;
			} else {
				os.writeUTF(1+"");
				os.flush();
			}
			if (fd != null) {
				os.reset();
				os.writeObject(fd);
				os.flush();
				System.out.println("write fusiondata object to client");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getAnswer(){
		try {
			os.writeUTF("OK");
			os.flush();
			Answer a = (Answer) is.readObject();
			
			function.insertAnswer(a);
			
			System.out.println("read answer objects from client");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("failed to read answer obects to client");
			e.printStackTrace();
		} catch (Exception e){
			System.out.println("Exception");
			e.printStackTrace();
		}
	}
	
	public void getSensordata () {
		try {
			os.writeUTF("OK");
			os.flush();
			
			SensordataObject sdo = (SensordataObject) is.readObject();
			function.insertSensordata(sdo);
			System.out.println("Insert sensor data from client: OK");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOException in getSensordata: "+e);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("ClassNotFoundException in getSensordata: "+e);
			e.printStackTrace();
		}
	}
	
	public void echo(){
		try {
			os.writeUTF("echo");
			os.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOException");
			e.printStackTrace();
		} catch (Exception e){
			System.out.println("Exception");
			e.printStackTrace();
		}
		
	}

}
