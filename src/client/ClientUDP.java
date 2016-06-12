/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import business.PDU;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

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

                InetAddress ip;
                int porta;
                
                byte[] buffer = new byte[256];
                
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(packet);

                byte[] receive = packet.getData();
                
                switch(receive[2]){
                    case 4:
                        byte[] pdu = PDU.probeResponsePDU(System.currentTimeMillis());
                        
                        ip = packet.getAddress();
                        porta = packet.getPort();
                        
                        packet = new DatagramPacket(pdu, pdu.length, ip, porta);
                        
                        serverSocket.send(packet);
                        break;
                    
                    case 6:
                        ArrayList<byte[]> data = getPDUData(receive, packet);
                        
                        ip = packet.getAddress();
                        porta = packet.getPort();
                        
                        for(byte[] b: data){
                            packet = new DatagramPacket(b, b.length, ip, porta);
                            serverSocket.send(packet);
                            
                            byte[] res = new byte[1];                
                            packet = new DatagramPacket(res, res.length);
                            serverSocket.receive(packet);
                        }
                        
                        byte[] end = PDU.endTransfer();
                        
                        packet = new DatagramPacket(end, end.length, ip, porta);
                        serverSocket.send(packet);
                        
                        break;
                }
            }
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }
    
    private ArrayList<byte[]> getPDUData(byte[] pdu, DatagramPacket packet) throws IOException{
        
        String banda ="", musica="", porta="";
        int i = 7;
            
        for(; (char)pdu[i] != '\0'; i++){
            banda += (char)pdu[i];
        }

        i++;

        for(; (char)pdu[i] != '\0'; i++){
            musica += (char)pdu[i];
        }
        
        String p = "./files/" + banda + "-" + musica + ".mp3";
        Path path = Paths.get(p);
                
        byte[] data = Files.readAllBytes(path);
        
        System.out.println(data.length);
        
        ArrayList<byte[]> response = PDU.data(data);
        
        return response;
    }
}
