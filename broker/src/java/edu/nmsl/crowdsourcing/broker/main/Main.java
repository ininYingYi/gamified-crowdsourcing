package edu.nmsl.crowdsourcing.broker.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.net.ServerSocket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import edu.nmsl.crowdsourcing.broker.database.DatabaseController;
import edu.nmsl.crowdsourcing.broker.database.Function;


/* last modified 2014.11.19
 * 1. client-server self-certificate added
 */
public class Main {

	/**
	 * @param args
	 */
	private static int port = 9528;
	private static boolean run = true;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClientManager manager = new ClientManager();
		Function f = new Function("mysql://volibear.cs.nthu.edu.tw/sensorFusion");
		//DatabaseController dbController = new DatabaseController("mysql://volibear.cs.nthu.edu.tw/simQA2014");
		f.initialCurrentQueryID();
		
		try {
			// setup keystore to use server.key file
			String keystorepass = "crowdsourcing_keystorepass";
			String keypassword  = "crowdsourcing_keypass";
                        
                        String classPath = Main.class.getResource("Main.class").toString().substring(5);
                        String className = Main.class.getName() + ".class";
                        String packagePath = classPath.substring(0, classPath.length()-className.length());
			//FileInputStream ksfile = new FileInputStream ("G:\\Crowdsourcing-broker\\broker_hostname_ks");
                        FileInputStream ksfile = new FileInputStream(new File(packagePath, "broker_hostname_ks"));
			KeyStore brokerks = KeyStore.getInstance("JKS");
			brokerks.load(ksfile, keystorepass.toCharArray());
			PrivateKey privatekey = (PrivateKey) brokerks.getKey("crowdsourcing_broker_key", keypassword.toCharArray());
			PublicKey publickey = brokerks.getCertificate("crowdsourcing_broker_key").getPublicKey();
			System.err.println("Private Key: "+privatekey);
			System.err.println("Public Key: "+publickey);
			
			// setup key managers using the keystore
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(brokerks, keypassword.toCharArray());
			
			// setup ssl session
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(kmf.getKeyManagers(), null, null);
			ServerSocketFactory sslssf = sslcontext.getServerSocketFactory();
			
			// Open socket directly from SSF
			SSLServerSocket broker = (SSLServerSocket) sslssf.createServerSocket(port);
			////ServerSocket broker = new ServerSocket(port);
			
			while(run){
				System.out.println("Listen...");
				ClientHandler client = new ClientHandler((SSLSocket) broker.accept(), f);
				manager.addClient(client);
				System.out.println("new client connected");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
	}

}
