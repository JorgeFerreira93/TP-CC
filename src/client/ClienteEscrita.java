/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.OutputStream;
import java.net.Socket;
import business.PDU;
import java.io.InputStream;
import java.util.Scanner;

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

        Socket clientSocket = new Socket("localhost", 6789);

        port=PDU.getPort();
        byte[] pdu = PDU.registerPDU(user, clientSocket.getLocalAddress().getHostAddress(), (byte)1, port);

        OutputStream out = clientSocket.getOutputStream();
        out.write(pdu);

        InputStream inp = clientSocket.getInputStream();
        
        int id = inp.read();
        
        ClienteLeitura cl = new ClienteLeitura(port, id);        
        cl.start();
        
        while(true){
            System.out.print(">>");
            user = in.nextLine();
            
            pdu = PDU.consultRequestPDU("banda", "musica");
            
            out.write(pdu);
        }
    }
}
