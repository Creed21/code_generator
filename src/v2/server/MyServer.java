/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aca
 */
public class MyServer {

    public MyServer() { 
        try {
            ServerSocket serverSocket = new ServerSocket(65111);
            System.out.println("serverSocket has been created: "+serverSocket);

            int i = 0;

            while(true) {
                System.out.println("\n\nWaiting for the client . . .");
                Socket client = serverSocket.accept();
                System.out.println((++i)+". client: " + client+"\n");
                
                ClientThread ct = new ClientThread(client);
                ct.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(MyServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
