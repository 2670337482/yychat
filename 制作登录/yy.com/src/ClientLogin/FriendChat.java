package ClientLogin;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.*;

import yychat.model.Message;

import com.yychat.controller.ClientConnetion;

public class FriendChat extends JFrame implements ActionListener,Runnable{

	JScrollPane jsp;
	JTextArea jta;
	
	JPanel jp;
	JTextField jtf;
	JButton jb;
	
	String sender;
	String receiver;
	
	public FriendChat(String sender,String receiver){
	    this.sender=sender;
	    this.receiver=receiver;
	
		jta=new JTextArea();
		jta.setEditable(false);
		jta.setForeground(Color.red);
		jsp=new JScrollPane(jta);
		
		jp=new JPanel();
		jb=new JButton("发送");
		jb.addActionListener(this);
		jtf=new JTextField(15);
	    jp.add(jtf);jp.add(jb);
	    
	    this.add(jsp,"Center");
	    this.add(jp,"South");
	    this.setTitle(sender+"正在和"+receiver+"聊天");
		this.setLocationRelativeTo(null);
		this.setSize(350,240);
		this.setVisible(true);
		
	}
		
	
	
	public static void main(String[] args) {
		//FriendChat friendChat=new FriendChat();
	}



	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource()==jb){
			String content=jtf.getText();
			jta.append(content+"\r\n");
			//发送Message对象到服务器
			Message mess=new Message();
			 ObjectOutputStream oos;
			 mess.setContent(content);
		     mess.setSender(sender);
			 mess.setReceiver(receiver);
		     mess.setMessageType(Message.message_common);//common
			try {
				 oos=new ObjectOutputStream(ClientConnetion.s.getOutputStream());
				 oos.writeObject(mess);
				 
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		}

	public void run() {
		ObjectInputStream ois;
		Message mess;
		while(true){
			try {
			//接受服务器转发的Message的对象
			ois = new ObjectInputStream(ClientConnetion.s.getInputStream());
			mess=(Message)ois.readObject();
			jta.append(mess.getSender()+"对"+mess.getReceiver()+"说"+mess.getContent()+"\r\n");
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		}	
	}
}