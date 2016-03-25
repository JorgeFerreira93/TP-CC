/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import java.nio.ByteBuffer;
import java.util.Random;

/**
 *
 * @author Jorge
 */
public class PDU {

    public PDU() {
    }
    
    public byte[] registerPDU(String user, String ip, byte type){
        
        ByteBuffer aux;
        String infoString = user + '\0' + ip + '\0';
        
        int port = getPort();
        byte[] portBytes = ByteBuffer.allocate(4).putInt(port).array();
        byte[] info = infoString.getBytes();        
        int pduSize = 9 + info.length + portBytes.length;
        byte[] pdu = new byte[pduSize];
        byte[] pduAux = new byte[8];
        
        for(int j=0; j<portBytes.length; j++){
            System.out.println(portBytes[j]);
        }
        
        aux = ByteBuffer.wrap(pdu);
        
        pduAux[0] = 1;
        pduAux[1] = 0;
        pduAux[2] = 1;
        pduAux[3] = 0;
        pduAux[4] = 0;
        pduAux[5] = 0;
        pduAux[6] = 0;
        pduAux[7] = type;
        
        aux.put(pduAux);
        aux.put(info);
        aux.put(portBytes);
        aux.put((byte)'c');
        
        return pdu;
    }
    
    private int getPort(){
        
        Random random = new Random();
        
        int number = random.nextInt((100000 - 49152) + 1) + 49152;
        
        System.out.println(number);
        
        return number;
    }
}
