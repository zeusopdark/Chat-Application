/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverr;

public class ServerMain
{
    public static void main(String[] args)
    {
        Server s=new Server();  //it will invoke the GUI part
        s.waitingForClient();   //it will wait for the client
        s.setIoStreams();       //it will set the streams through which we will transfer the data
    }
}
