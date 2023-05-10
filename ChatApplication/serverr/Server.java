/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverr;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javazoom.jl.player.Player;

public class Server
{
    private JFrame serverframe;
    private JTextArea ta;
    private JScrollPane scrollpane;
    private JTextField tf;
    
    private ServerSocket serversocket;
    
    private InetAddress inet_address;
    
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    
    //---------------------------Thread created--------------------------------------
    Thread thread=new Thread(){
        public void run()
        {
            while(true)
            {
                readMessage();
            }
        }
    };
    //-------------------------------------------------------------------------------
    
    Server()
    {
        serverframe=new JFrame("Server");
        serverframe.setSize(500,500);
        serverframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        ta=new JTextArea();
        ta.setEditable(false);
        Font font=new Font("Arial", 1, 16);
        ta.setFont(font);
        scrollpane=new JScrollPane(ta);
        serverframe.add(scrollpane);
        
        tf=new JTextField();
        tf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage(tf.getText());
                ta.append(tf.getText()+"\n");
                tf.setText("");
            }
        });
        tf.setEditable(false);
        serverframe.add(tf, BorderLayout.SOUTH);
        
        serverframe.setVisible(true);
    }
    
    public void waitingForClient()
    {
        try
        {
            String ipaddress=getIpAddress();
            
            serversocket=new ServerSocket(1111);
            ta.setText("To connect with server, please provide IP Address : "+ipaddress);
            socket=serversocket.accept();
            ta.setText("Client Connected\n");
            ta.append("----------------------------------------------------\n");
            
            tf.setEditable(true);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    public String getIpAddress()
    {
        String ip_address="";
        try
        {
            inet_address=InetAddress.getLocalHost();
            ip_address=inet_address.getHostAddress();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        return ip_address;
    }
    
    void setIoStreams()
    {
        try
        {
            dis=new DataInputStream(socket.getInputStream());
            dos=new DataOutputStream(socket.getOutputStream());
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        thread.start();
    }
    
    public void sendMessage(String message)
    {
        try
        {
            dos.writeUTF(message);
            dos.flush();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    
    public void readMessage()
    {
        try
        {
            String message=dis.readUTF();
            showMessage("Client : "+message);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    
    public void showMessage(String message)
    {
        ta.append(message+"\n");
        chatSound();
    }
    
    public void chatSound()
    {
        try
        {
            File file_name=new File("src\\sound\\chat_sound.mp3");
            FileInputStream fis=new FileInputStream(file_name.getAbsolutePath());
            Player p=new Player(fis);
            p.play();
            
//            FileInputStream fis=new FileInputStream("C:\\Users\\Deepak\\Documents\\NetBeansProjects\\ChatApplication\\src\\sound\\chat_sound.mp3");
//            Player p=new Player(fis);
//            p.play();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}
