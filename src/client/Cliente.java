/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.OutputStream;
import java.net.Socket;
import business.PDU;
import java.util.Scanner;

/**
 *
 * @author Jorge
 */
public class Cliente {
    
    public static void main(String[] args) throws Exception {
        
        Scanner in = new Scanner(System.in);
        System.out.print(">>");
        String user = in.nextLine();
        
        Socket clientSocket = new Socket("localhost", 6789);
        
        byte[] pdu = new PDU().registerPDU(user, "ip", (byte)1);
        
        OutputStream out = clientSocket.getOutputStream();
        out.write(pdu);
        
        while(true){            
            System.out.print(">>");
            String aux = in.nextLine();
            
            //O dummy só serve para testar se a conexao esta direita
            pdu = new PDU().dummyPDU();
            
            out = clientSocket.getOutputStream();
            out.write(pdu);
        }
                
        //clientSocket.close();
    }
}
