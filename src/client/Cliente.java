/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import business.PDU;

/**
 *
 * @author Jorge
 */
public class Cliente {
    
    public static void main(String[] args) throws Exception {
        
        Socket clientSocket = new Socket("localhost", 6789);
        
        byte[] pdu = new PDU().registerPDU("user", "ip", (byte)1);
                
        OutputStream out = clientSocket.getOutputStream();
        out.write(pdu);
                
        clientSocket.close();  
    }
}
