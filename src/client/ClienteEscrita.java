/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.OutputStream;
import java.net.Socket;
import business.PDU;
import java.awt.BorderLayout;
import java.io.DataInputStream;
import java.io.InputStream;
import java.util.Scanner;
import java.util.StringTokenizer;

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
                        probeRequest(resposta);
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
    
    private static void probeRequest(byte[] pdu){
        
        int nHosts = Character.getNumericValue((char)pdu[9]);
        int i=11;

        for(int n=0; n<nHosts; n++){
            
            String id ="", ip="", porta="";
            
            System.out.println(new String(pdu));
            
            
            for(; (char)pdu[i] != '\0'; i++){
                id += ( char)pdu[i];
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
            
            /* Enviar probe TODO */
        }
    }
}
