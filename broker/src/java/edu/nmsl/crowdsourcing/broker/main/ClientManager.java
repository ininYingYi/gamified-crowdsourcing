package edu.nmsl.crowdsourcing.broker.main;

import java.net.Socket;
import java.util.ArrayList;

public class ClientManager {

	ArrayList<ClientHandler> clients;
	
	public ClientManager(){
		clients = new ArrayList<ClientHandler>();
	}
	
	public void addClient(ClientHandler handler){
		clients.add(handler);
	}
	
}
