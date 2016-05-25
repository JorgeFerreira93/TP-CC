/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Jorge
 */
public class PDU {

    public static byte[] registerPDU(String user, String ip, byte type, int port){

        ByteBuffer aux;
        String infoString = user + '\0' + ip + '\0' + port;

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

    public static byte[] consultRequestPDU(String banda, String musica){

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

    public static byte[] consultResponsePDU(int id, String ip, boolean res, int porta){

        ByteBuffer aux;
        //int porta = getPort();
        int resposta = res ? 1 : 0;
        String infoString = String.valueOf(id) + '\0' + ip + '\0' + String.valueOf(porta) + '\0' + String.valueOf(resposta);

        byte[] info = infoString.getBytes();
        int pduSize = 9 + info.length;
        byte[] pdu = new byte[pduSize];
        byte[] pduAux = new byte[7];

        aux = ByteBuffer.wrap(pdu);

        pduAux[0] = 1;
        pduAux[1] = 0;
        pduAux[2] = 3;
        pduAux[3] = 0;
        pduAux[4] = 0;
        pduAux[5] = 0;
        pduAux[6] = 0;

        aux.put(pduAux);
        aux.put(info);

        return pdu;
    }

    public static byte[] consultResponseListaPDU(ArrayList<byte[]> lista){

        ByteBuffer aux;

        int nHosts = lista.size();
        
        if(nHosts == 0){
            String infoString = String.valueOf(0) + '\0' + String.valueOf(nHosts);
            
            byte[] info = infoString.getBytes();
            int pduSize = 8 + info.length;
            byte[] pdu = new byte[pduSize];
            byte[] pduAux = new byte[7];

            aux = ByteBuffer.wrap(pdu);

            pduAux[0] = 1;
            pduAux[1] = 0;
            pduAux[2] = 3;
            pduAux[3] = 0;
            pduAux[4] = 0;
            pduAux[5] = 0;
            pduAux[6] = 0;

            aux.put(pduAux);
            aux.put(info);
            return pdu;
        }
        String infoString = String.valueOf(1) + '\0' + String.valueOf(nHosts);

        int tamanho = 0;

        for(byte[] b: lista){
            tamanho += b.length;
        }

        byte[] pduLista = new byte[tamanho + nHosts];
        ByteBuffer pdus = ByteBuffer.wrap(pduLista);
        
        for(byte[] b: lista){
            byte[] tmp = new byte[b.length + 1];
            byte[] end = (new String("\0")).getBytes();
            System.arraycopy(end, 0, tmp, 0, end.length);
            System.arraycopy(b, 0, tmp, 1, b.length);
            System.out.println(new String(tmp));
            pdus.put(tmp);
        }

        byte[] info = infoString.getBytes();
        int pduSize = 8 + info.length + pduLista.length;
        byte[] pdu = new byte[pduSize];
        byte[] pduAux = new byte[7];

        aux = ByteBuffer.wrap(pdu);

        pduAux[0] = 1;
        pduAux[1] = 0;
        pduAux[2] = 3;
        pduAux[3] = 0;
        pduAux[4] = 0;
        pduAux[5] = 0;
        pduAux[6] = 0;

        aux.put(pduAux);
        aux.put(info);
        aux.put(pduLista);
        aux.put((byte)'\0');
        System.out.println(new String(pdu));
        return pdu;
    }
    
    public static byte[] probeRequestPDU(){
        byte[] pdu = new byte[7];

        pdu[0] = 1;
        pdu[1] = 0;
        pdu[2] = 4;
        pdu[3] = 0;
        pdu[4] = 0;
        pdu[5] = 0;
        pdu[6] = 0;

        return pdu;
    }

    public static int getPort(){

        Random random = new Random();

        int number = random.nextInt((65534 - 49152) + 1) + 49152;

        return number;
    }
}
