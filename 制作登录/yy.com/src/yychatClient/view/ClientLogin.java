package yychatClient.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import javax.swing.*;

import yychat.model.Message;
import yychat.model.User;

import com.yychat.controller.ClientConnetion;

public class ClientLogin extends JFrame implements ActionListener {//类名，继承
	public static HashMap hmFriendList=new HashMap<String,FriendList>();
	
	JLabel jbl1;
	
    JTabbedPane jtp1;//选项卡组建
    JPanel jp2,jp3,jp4;
    JLabel jbl2,jbl3,jbl4,jbl5;
    JTextField jtf1;
    JPasswordField jpf1;
    JButton jb4;
    JCheckBox jcb1,jcb2;//复选框
    
	JButton jb1,jb2,jb3;
	JPanel jpl1;
	
    public ClientLogin(){//构造方法
		jbl1=new JLabel(new ImageIcon("images/tou.gif"));
		this.add(jbl1,"North");
		
		jtp1=new JTabbedPane();
		jp2=new JPanel(new GridLayout(3,3));
		jp3=new JPanel();
		jtf1=new JTextField();
		jtf1.addActionListener(this);
		jp4=new JPanel();jpf1=new JPasswordField();
		jbl2=new JLabel("YY号码",JLabel.CENTER);jbl3=new JLabel("YY密码",JLabel.CENTER);
		jbl4=new JLabel("忘记密码",JLabel.CENTER);jbl5=new JLabel("申请密码保护",JLabel.CENTER);
        jcb1=new JCheckBox("隐身登陆");jcb2=new JCheckBox("记住密码");
		jbl4.setForeground(Color.BLUE);jb4=new JButton(new ImageIcon("images/clear.gif"));
        
		
		jp2.add(jbl2);jp2.add(jtf1);jp2.add(jb4);
		jp2.add(jbl3);jp2.add(jpf1);jp2.add(jbl4);
		jp2.add(jcb1);jp2.add(jcb2);jp2.add(jbl5);
		jtp1.add(jp2,"YY号码");jtp1.add(jp3,"手机号码");jtp1.add(jp4,"电子邮箱");
		this.add(jtp1);
		
		
		jb1=new JButton(new ImageIcon("images/denglu.gif"));
		jb1.addActionListener(this);
		jb2=new JButton(new ImageIcon("images/zhuce.gif"));
		jb3=new JButton(new ImageIcon("images/quxiao.gif"));
		jpl1=new JPanel();
		jpl1.add(jb1);jpl1.add(jb2);jpl1.add(jb3);
		this.add(jpl1,"South");
		
		
		this.setSize(350, 250);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
	
	
	public static void main(String[] args) {
		ClientLogin clientLogin=new ClientLogin();

	}
	 

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource()==jb1){
			String userName=jtf1.getText();
			String passWord=new String(jpf1.getPassword());
			User user=new User();
			user.setUserName(userName);
			user.setPassWord(passWord);
			//密码验证，密码是123456验证成功，否则验证失败
			
			
			Message mess=new ClientConnetion().loginValidate(user);
			if(mess.getMessageType().equals("1")){
				FriendList friendList=new FriendList(userName);
				hmFriendList.put(userName,friendList);
				
				Message mess1=new Message();
				mess1.setSender(userName);
				mess1.setReceiver("Sever");
				mess1.setMessageType(Message.message_RequreOnlineFriend);
				Socket s=(Socket)ClientConnetion.hmSocket.get(userName);
				ObjectOutputStream oos;
				try {
					oos=new ObjectOutputStream(s.getOutputStream());
					oos.writeObject(mess1);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
				this.dispose();
				
			}else{
				JOptionPane.showMessageDialog(this,"密码错误");
			}
		}
	}
}
