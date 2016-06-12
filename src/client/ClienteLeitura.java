/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Jorge
 */
public class ClienteLeitura extends Thread {

    int port;
    int idUtilizador;
    String ip;
    ServerSocket serverSocket;
    Boolean end = false;

    public ClienteLeitura(String ip, int port, int idUtilizador){
        this.port = port;
        this.idUtilizador = idUtilizador;
        this.ip = ip;
    }

    @Override
    public void run(){
        try{
            serverSocket = new ServerSocket(this.port);
            
            while(true || !end){
                Socket connectionSocket = serverSocket.accept();
                
                ClienteThread ct = new ClienteThread(connectionSocket, idUtilizador, ip, port);
                ct.start();
            }
        }
        catch(Exception e){
            //System.out.println(e.toString());
        }
    }
    
    public void kill() throws IOException{
        this.serverSocket.close();
        this.end = true;
    }
}
