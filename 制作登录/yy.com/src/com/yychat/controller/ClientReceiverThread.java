package com.yychat.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import yychat.model.Message;
import yychatClient.view.ClientLogin;
import yychatClient.view.FriendChat1;
import yychatClient.view.FriendList;

public class ClientReceiverThread extends Thread{
    Socket s; 
	
    public ClientReceiverThread(Socket s){
    	 this.s=s;
     }

    public void run(){
    	ObjectInputStream ois;
		Message mess;
		while(true){
			try {
			//接受服务器转发的Message的对象
			ois = new ObjectInputStream(s.getInputStream());
			mess=(Message)ois.readObject();
			if(mess.getMessageType().equals(Message.message_common)){
				String chatMessageString=(mess.getSender()+"对"+mess.getReceiver()+"说"+mess.getContent()+"\r\n");
				System.out.println(chatMessageString);
				//拿到显示聊天信息的friend对象
				FriendChat1 friendChat1=(FriendChat1)FriendList.hmFriendChat1.get(mess.getReceiver()+"to"+mess.getSender());
			    //将聊天信息在JTextArea
				friendChat1.appendJta(chatMessageString);
			}
			
			if(mess.getMessageType().equals(Message.message_OnlineFriend)){
				System.out.println("在线好友："+mess.getContent());
				//怎么激活对应的图标
				//首先要拿到FriendList对象
				FriendList friendList=(FriendList)ClientLogin.hmFriendList.get(mess.getReceiver());
				friendList.setEnabledOnlineFriend(mess.getContent());
			}
			
			if(mess.getMessageType().equals(Message.message_upOnlineFriend)){
				FriendList friendList=(FriendList)ClientLogin.hmFriendList.get(mess.getReceiver());
				friendList.UpNewOnlineFriend(mess.getContent());
			}
		
			
			} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		}	
    }
}