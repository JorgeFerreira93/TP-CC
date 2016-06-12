/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.OutputStream;
import java.net.Socket;
import business.PDU;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jorge
 */
public class ClienteEscrita {

    public static void main(String[] args) throws Exception {

        Scanner in = new Scanner(System.in);
        System.out.print(">>");
        String user = in.nextLine();
        int port;

        Socket clientSocket = new Socket("localhost", 10000);

        port=PDU.getPort();
        String ip = clientSocket.getLocalAddress().getHostAddress();
        byte[] pdu = PDU.registerPDU(user, ip, (byte)1, port);

        OutputStream out = clientSocket.getOutputStream();
        out.write(pdu);

        InputStream inp = clientSocket.getInputStream();
        int id = inp.read();
        
        if(id == 255){
            System.out.println("Nome de utilizador já existente");
            return;
        }

        ClienteLeitura cl = new ClienteLeitura(ip, port, id);
        cl.start();

        while(true){
            System.out.print(">>");
            String request = in.nextLine();

            StringTokenizer strtok = new StringTokenizer(request, " ");

            switch(strtok.nextToken()){
                case "request":

                    String banda = strtok.nextToken();
                    String musica = strtok.nextToken();

                    byte[] resposta = sendRequest(banda, musica, clientSocket);
                    
                    if(resposta == null){
                        System.out.println("Não foram encontrados clients com o ficheiro");
                    }
                    else{
                        probeRequest(resposta, banda, musica);
                    }

                    break;
                default:
                    System.out.println("Erro no comando");
                    break;
            }
        }
    }

    private static byte[] sendRequest(String banda, String musica, Socket clientSocket) throws Exception{

        OutputStream out = clientSocket.getOutputStream();

        byte[] pdu = PDU.consultRequestPDU(banda, musica);
        out.write(pdu);

        DataInputStream din = new DataInputStream(clientSocket.getInputStream());

        int pduLenght = din.readInt();
        byte[] pduResponse = new byte[pduLenght];
        din.read(pduResponse);
        
        if((char)pduResponse[7] == '1'){
            return pduResponse;
        }
        else{
            return null;
        }
    }
    
    private static void probeRequest(byte[] pdu, String banda, String musica) throws IOException{
        
        int nHosts = Character.getNumericValue((char)pdu[9]);
        int i=11;
        long max = -1;
        String idM="", ipM="", portaM="";
        
        for(int n=0; n<nHosts; n++){
            
            String id ="", ip="", porta="";            
            
            for(; (char)pdu[i] != '\0'; i++){
                id += (char)pdu[i];
            }

            i++;

            for(; (char)pdu[i] != '\0'; i++){
                ip += (char)pdu[i];
            }
            i++;

            for(; (char)pdu[i] != '\0'; i++){
                porta += (char)pdu[i];
            }
            i++;
            
            System.out.println("id: " + id + " ip: " + ip + " porta: " + porta);
            
            long res = sendProbeRequest(ip, porta);
            
            if(max < res){
                max = res;
                idM = id;
                ipM = ip;
                portaM = porta;
            }
        }
        
        sendRequest(idM, ipM, portaM, banda, musica);
    }
    
    private static long sendProbeRequest(String address, String porta) throws UnknownHostException, IOException{
        try {
            DatagramSocket sock = new DatagramSocket();
            
            byte[] buf = PDU.probeRequestPDU();
            InetAddress ip = InetAddress.getByName(address);
                                    
            DatagramPacket packet = new DatagramPacket(buf, buf.length, ip, Integer.parseInt(porta));
            sock.send(packet);
            
            byte[] response = new byte[36];
            packet = new DatagramPacket(response, response.length);
            sock.receive(packet);
            
            String tmp = "";
            
            for(int i=7; packet.getData()[i] !=  '\0'; i++){
                tmp += (char)packet.getData()[i];
            }
            
            long tempo = System.currentTimeMillis() - Long.parseLong(tmp);
            
            return tempo;
            
        } catch (SocketException ex) {
            Logger.getLogger(ClienteEscrita.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return -1;
    }
    
    private static void sendRequest(String id, String address, String porta, String banda, String musica) throws SocketException, IOException{
        
        byte[] pdu = PDU.request(banda, musica);
        
        DatagramSocket sock = new DatagramSocket();
        
        InetAddress ip = InetAddress.getByName(address);

        DatagramPacket packet = new DatagramPacket(pdu, pdu.length, ip, Integer.parseInt(porta));
        sock.send(packet);
        
        System.out.println("Enviei");
        
        byte[] response = new byte[48000];
        packet = new DatagramPacket(response, response.length);
        sock.receive(packet);
        
        System.out.println("Recebi");
        
        int j;
        
        for(j=7; (char) packet.getData()[j] != '\0'; j++){
            System.out.print("'" + packet.getData()[j] + "'");
        }
        
        byte[] res = new byte[j-7];
        System.arraycopy(packet.getData(), 7, res, 0, j-7);
        
        //FileOutputStream fileOputputStream = new FileOutputStream("teste");
        Files.write(Paths.get("./src/teste.txt"), res);
        
    }
}
