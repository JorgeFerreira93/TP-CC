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
import java.util.Arrays;

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
                        byte[] realData = Arrays.copyOf( packet.getData(), packet.getLength() );
                        System.out.println("Tamanho?: " + realData.length);
                        
                        serverSocket.send(packet);
                        break;
                    
                    case 6:
                        byte[] data = getPDUData(receive, packet);
                        
                        ip = packet.getAddress();
                        porta = packet.getPort();
                        
                        packet = new DatagramPacket(data, data.length, ip, porta);
                        
                        serverSocket.send(packet);
                        
                        break;
                }
            }
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }
    
    private byte[] getPDUData(byte[] pdu, DatagramPacket packet) throws IOException{
        
        String banda ="", musica="", porta="";
        int i = 7;
            
        for(; (char)pdu[i] != '\0'; i++){
            banda += (char)pdu[i];
        }

        i++;

        for(; (char)pdu[i] != '\0'; i++){
            musica += (char)pdu[i];
        }
        
        String p = "./src/client/files/" + banda + "-" + musica + ".mp3";
        Path path = Paths.get(p);
        
        byte[] data = Files.readAllBytes(path);
        
        byte[] pduData = PDU.data(data);
        
        return pduData;
    }
}
