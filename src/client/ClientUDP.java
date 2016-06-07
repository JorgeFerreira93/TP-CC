/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import business.PDU;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author Jorge
 */
public class ClientUDP extends Thread {
    int port;
    int idUtilizador;

    public ClientUDP(int port, int idUtilizador){
        this.port = port;
        this.idUtilizador = idUtilizador;
    }

    @Override
    public void run(){
        try{
            DatagramSocket serverSocket = new DatagramSocket(this.port);
            
            while(true){

                byte[] buffer = new byte[256];
                
                System.out.println("À espera...");
                
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(packet);
                
                byte[] receive = packet.getData();
                
                switch(receive[2]){
                    case 4:
                        byte[] pdu = PDU.probeResponsePDU(System.currentTimeMillis());
                        
                        InetAddress ip = packet.getAddress();
                        int porta = packet.getPort();
                        packet = new DatagramPacket(pdu, pdu.length, ip, porta);
                        
                        serverSocket.send(packet);
                }
            }
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }
}
