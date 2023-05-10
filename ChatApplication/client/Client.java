/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javazoom.jl.player.Player;

public class Client
{
    private JFrame clientframe;
    private JTextArea ta;
    private JScrollPane scrollpane;
    private JTextField tf;
    
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    
    String ipaddress;
    
    //-----------------------------Thread creation start-------------------------------
    Thread thread=new Thread(){
        public void run()
        {
            while(true)
            {
                readMessage();
            }
        }
    };
    //-----------------------------Thread creation end---------------------------------
    
    Client()
    {
        ipaddress=JOptionPane.showInputDialog("Enter IP Address");
        
        if(ipaddress != null)
        {
            if(!ipaddress.equals(""))
            {
                connectToServer();
                
                clientframe=new JFrame("Client");
                clientframe.setSize(500,500);
                clientframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                ta=new JTextArea();
                ta.setEditable(false);
                Font font=new Font("Arial", 1, 16);
                ta.setFont(font);
                scrollpane=new JScrollPane(ta);
                clientframe.add(scrollpane);

                tf=new JTextField();
                tf.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sendMessage(tf.getText());
                        ta.append(tf.getText()+"\n");
                        tf.setText("");
                    }
                });
                clientframe.add(tf, BorderLayout.SOUTH);

                clientframe.setVisible(true);
            }
        }
    }
    
    void connectToServer()
    {
        try
        {
            socket=new Socket(ipaddress, 1111);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
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
            showMessage("Server : "+message);
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
