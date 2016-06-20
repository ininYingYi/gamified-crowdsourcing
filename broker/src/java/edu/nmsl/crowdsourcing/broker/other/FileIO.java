package edu.nmsl.crowdsourcing.broker.other;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileIO {

	String path;
	BufferedReader br;
	BufferedWriter bw;
	
	public FileIO(String path){
		this.path = path;
	}
	
	public BufferedReader getFileReader(){
		try {
			return new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("getBufferedReader failed");
			e.printStackTrace();
		}
		
		return null;
	}
	
	public BufferedWriter getFileWriter(){
		try {
			return new BufferedWriter(new FileWriter(path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("getBufferedwriter failed");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("getBufferedwriter failed");
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String readLine(){
		
		if(br==null){
			br = getFileReader();
		}
		
		try {
			
			return br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("read failed");
			e.printStackTrace();
		} catch (NullPointerException e){
			System.out.println("null pointer of buferedreader");
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void write(String c){
		if(bw==null){
			bw = getFileWriter();
		}
		
		try {
			bw.write(c);
			bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e){
			System.out.println("null pointer of buffered writer");
			e.printStackTrace();
		}
		
	}
	
	public void writeLine(String c){
		write(c + "\n");
	}
	
	public File getFile(){
		return new File(path);
	}
	
	public void close(){
		
		try {
			if(br!=null)
				br.close();
			
			if(bw!=null)
				bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
