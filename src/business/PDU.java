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
        int port = getPort();
        String infoString = user + '\0' + ip + '\0'+port;

        byte[] info = infoString.getBytes();
        int pduSize = 9 + info.length;
        byte[] pdu = new byte[pduSize];
        byte[] pduAux = new byte[8];

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

        return pdu;
    }
    
    public byte[] consultRequestPDU(String banda, String musica){
        
        ByteBuffer aux;
        String infoString = banda + '\0' + musica;

        byte[] info = infoString.getBytes();
        int pduSize = 8 + info.length;
        byte[] pdu = new byte[pduSize];
        byte[] pduAux = new byte[7];

        aux = ByteBuffer.wrap(pdu);

        pduAux[0] = 1;
        pduAux[1] = 0;
        pduAux[2] = 2;
        pduAux[3] = 0;
        pduAux[4] = 0;
        pduAux[5] = 0;
        pduAux[6] = 0;

        aux.put(pduAux);
        aux.put(info);

        return pdu;
    }
    
    public byte[] dummyPDU(){
        byte[] pduAux = new byte[7];
        
        pduAux[0] = 0;
        pduAux[1] = 0;
        pduAux[2] = 0;
        pduAux[3] = 0;
        pduAux[4] = 0;
        pduAux[5] = 0;
        pduAux[6] = 0;
        
        return pduAux;
    }

    private int getPort(){

        Random random = new Random();

        int number = random.nextInt((100000 - 49152) + 1) + 49152;

        System.out.println(number);

        return number;
    }
}
