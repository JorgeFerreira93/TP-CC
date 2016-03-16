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

/**
 *
 * @author Jorge
 */
public class Cliente {
    
    public static void main(String[] args) throws Exception {
        
        Socket clientSocket = new Socket("localhost", 6789);
        
        int port = clientSocket.getLocalPort();
        
        byte[] teste = ByteBuffer.allocate(4).putInt(port).array();        
        byte[] address = clientSocket.getLocalAddress().getAddress();
        
        int tot = teste.length + address.length + 7;
        
        byte[] pduRegister = new byte[tot];
        
        pduRegister[0] = 1;
        pduRegister[1] = 0;
        pduRegister[2] = 1;
        pduRegister[3] = 0;
        pduRegister[4] = 0;
        pduRegister[5] = 0;
        pduRegister[6] = 0;
        
        System.out.println(teste.length);
        
        OutputStream out = clientSocket.getOutputStream();
        out.write(pduRegister);
                
        clientSocket.close();  
    }
}
