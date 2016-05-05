package client;

import business.PDU;
import servidor.*;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;

public class ClienteThread extends Thread{

    private Socket client;
    private int idUtilizador;

    public ClienteThread(Socket newclient, int idUtilizador){
      this.client = newclient;
      this.idUtilizador = idUtilizador;
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
            
            byte[] aux = PDU.consultResponsePDU(idUtilizador, "ip234567", "porta", 1);
            
            OutputStream out = client.getOutputStream();
            out.write(aux);
        }    
      }catch(Exception e){}//POR AQUI ALGUMA COISA
    }
}
