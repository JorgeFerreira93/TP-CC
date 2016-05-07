package client;

import business.PDU;
import java.io.DataOutputStream;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;

public class ClienteThread extends Thread{

    private Socket client;
    private int idUtilizador;
    private String ip;

    public ClienteThread(Socket newclient, int idUtilizador, String ip){
        this.client = newclient;
        this.idUtilizador = idUtilizador;
        this.ip = ip;
    }

    public void run(){
        try{          
          while(true){
              InputStream in = client.getInputStream();

              byte[] pdu = new byte[50];

              in.read(pdu);

              String banda = "";
              String musica = "";

              int i;
              for(i=7; (char)pdu[i] != '\0'; i++){
                  banda += (char)pdu[i];
              }

              i++;

              for(; (char)pdu[i] != '\0'; i++){
                  musica += (char)pdu[i];
              }

              byte[] aux = PDU.consultResponsePDU(idUtilizador, this.ip, 1);

              DataOutputStream out = new DataOutputStream(client.getOutputStream());
              out.writeInt(aux.length);
              out.write(aux);
          }    
        }catch(Exception e){}//POR AQUI ALGUMA COISA
    }
}
