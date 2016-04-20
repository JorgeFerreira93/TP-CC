package servidor;

import java.net.Socket;
import java.util.HashMap;
import java.io.InputStream;
import java.io.OutputStream;

public class ServidorThread extends Thread{

    protected HashMap<String, Utilizador> table;
    private Socket client;
    private Utilizador utilizador;

    public ServidorThread(HashMap<String, Utilizador> newtable,Socket newclient){
      this.table=newtable;
      this.client=newclient;
    }

    public void run(){
      try{

        while(true){
            InputStream in = client.getInputStream();

            byte[] pdu = new byte[50];

            in.read(pdu);

            if(pdu[2] == 1){
                registaPDU(pdu);
            }
            else{
                dummyPDU();
            }
        }

      }catch(Exception e){}//POR AQUI ALGUMA COISA
    }

    private boolean registaPDU(byte[] pdu){

        String nome = "";
        String ip = "";
        String porta = "";
        int i;

        for(i=8; (char)pdu[i] != '\0'; i++){
            nome += (char)pdu[i];
        }

        i++;

        for(; (char)pdu[i] != '\0'; i++){
            ip += (char)pdu[i];
        }
        i++;

        for(; (char)pdu[i] != '\0'; i++){
            porta += (char)pdu[i];
        }
        i++;

        utilizador = new Utilizador(nome, porta, ip, table.size() + 1);

        if(tablea.containsKey(nome))
          return false;

        table.put(nome, utilizador);
         return true;

    }
}
