/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.net.ServerSocket;
import java.net.Socket;
import servidor.ServidorThread;
import servidor.Utilizador;

/**
 *
 * @author Jorge
 */
public class ClienteLeitura extends Thread {
    
    int port;
    int idUtilizador;
    
    public ClienteLeitura(int port, int idUtilizador){
        this.port = port;
        this.idUtilizador = idUtilizador;
    }
    
    public void run(){
        try{
            
            ServerSocket serverSocket = new ServerSocket(port);
            
            while(true){
            
                Socket connectionSocket = serverSocket.accept();
                
                ClienteThread ct = new ClienteThread(connectionSocket, idUtilizador);
                ct.start();
            }
        }
        catch(Exception e){}
    }
}