package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import yychat.model.Message;

public class ServerReceiverThread extends Thread{
	Socket s;
	HashMap hmSocket;
	
	public ServerReceiverThread(Socket s,HashMap hmSocket){
    	this.s=s;
    	this.hmSocket=hmSocket;
    	}
  public void run(){
	 while(true){ 
		 ObjectInputStream ois;
 	 try {
			ois=new ObjectInputStream(s.getInputStream());
			Message mess=(Message)ois.readObject();
			System.out.println(mess.getSender()+"对"+mess.getReceiver()+"说:"+mess.getContent());
			
			if(mess.getMessageType().equals(Message.message_common)){
				Socket s1=(Socket)hmSocket.get(mess.getReceiver());
				ObjectOutputStream oos=new ObjectOutputStream(s1.getOutputStream());
				oos.writeObject(mess);
				System.out.println("服务器转发信息"+mess.getSender()+"对"+mess.getReceiver()+"说:"+mess.getContent());
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		 
	 }
	 
}}