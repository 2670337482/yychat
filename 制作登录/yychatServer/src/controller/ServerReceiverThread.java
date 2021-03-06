package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import yychat.model.Message;

public class ServerReceiverThread extends Thread{
	Socket s;
	HashMap hmSocket;
	Message mess;
	String sender;
	
	public ServerReceiverThread(Socket s,HashMap hmSocket){
    	this.s=s;
    	this.hmSocket=hmSocket;
    	}
  public void run(){
	 while(true){ 
		 ObjectInputStream ois;
 	 try {
			ois=new ObjectInputStream(s.getInputStream());
			mess=(Message)ois.readObject();
			System.out.println(mess.getSender()+"对"+mess.getReceiver()+"说:"+mess.getContent());
			sender=mess.getSender();
			if(mess.getMessageType().equals(Message.message_common)){
				Socket s1=(Socket)hmSocket.get(mess.getReceiver());
				sendMessage(s1,mess);
				System.out.println("服务器转发信息"+mess.getSender()+"对"+mess.getReceiver()+"说:"+mess.getContent());
			}
			
			if(mess.getMessageType().equals(Message.message_RequreOnlineFriend)){
				Set friendSet=StartServer.hmSocket.keySet();
				Iterator it=friendSet.iterator();
				String friendName;
				String friendString=" ";
				while(it.hasNext()){
					friendName=(String)it.next();
					if(!friendName.equals(mess.getSender()))
						friendString=friendName+" "+friendString;
				}
				System.out.println("全部好友的名字："+friendString);
				
				//给mess赋值
				mess.setContent(friendString);
				mess.setReceiver(sender);
				mess.setSender("Server");
				mess.setMessageType(Message.message_OnlineFriend);
				
				//发送mess
				Socket s1=(Socket)hmSocket.get(sender);
				sendMessage(s1,mess);
			}
				
				if(mess.getMessageType().equals(Message.message_UpnewFriend)){
					Set friendSet1=StartServer.hmSocket.keySet();
					Iterator it1=friendSet1.iterator();
					String friendName1;
					mess.setMessageType(Message.message_upOnlineFriend);
					while(it1.hasNext()){
						friendName1=(String)it1.next();
						System.out.println("键值为："+friendName1);
						if(!friendName1.equals(mess.getSender())){
							Socket s2=(Socket)hmSocket.get(friendName1);
							mess.setReceiver(friendName1);
							ObjectOutputStream oos;
							oos=new ObjectOutputStream(s2.getOutputStream());
							oos.writeObject(mess);
						}
						System.out.println("有执行代码："+mess.getReceiver());
						}		
				}
				
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	 }
}
public void sendMessage(Socket s,Message mess) throws IOException {
	ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
	oos.writeObject(mess);
}}