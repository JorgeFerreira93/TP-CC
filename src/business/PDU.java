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

    public static byte[] consultResponsePDU(int id, String ip, boolean res){

        ByteBuffer aux;
        int porta = getPort();
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

        String infoString = String.valueOf(1) + '\0' + String.valueOf(nHosts);

        int tamanho = 0;

        for(byte[] b: lista){
            tamanho += b.length;
        }

        byte[] pduLista = new byte[tamanho];
        ByteBuffer pdus = ByteBuffer.wrap(pduLista);

        for(byte[] b: lista){
            pdus.put(b);
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
        aux.put((byte)1);

        return pdu;
    }

    public static int getPort(){

        Random random = new Random();

        int number = random.nextInt((70000 - 49152) + 1) + 49152;

        return number;
    }
}
