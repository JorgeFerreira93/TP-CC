package client;

import business.PDU;
import java.io.DataOutputStream;
import java.net.Socket;
import java.io.InputStream;
import java.io.File;
import java.util.StringTokenizer;

public class ClienteThread extends Thread{

    private final Socket client;
    private final int idUtilizador;
    private final String ip;
    private final int porta;

    public ClienteThread(Socket newclient, int idUtilizador, String ip, int porta){
        this.client = newclient;
        this.idUtilizador = idUtilizador;
        this.ip = ip;
        this.porta = porta;
    }

    @Override
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

                File path = new File("./files");
                String[] children = path.list();
                boolean found = false;
              
                if(children!=null){
                    for (int j = 0; j < children.length && !found; j++) {                        
                        found = verificaFicheiro(banda, musica, children[j]);
                    }
                }

                if(found){
                    ClientUDP cUDP = new ClientUDP(this.porta, idUtilizador);
                    cUDP.start();
                }
                
                byte[] aux = PDU.consultResponsePDU(idUtilizador, this.ip, found, this.porta);
                
                DataOutputStream out = new DataOutputStream(client.getOutputStream());
                out.writeInt(aux.length);
                out.write(aux);
          }
        }catch(Exception e){}//POR AQUI ALGUMA COISA
    }
    
    private boolean verificaFicheiro(String banda, String musica, String file){
        
                        
        StringTokenizer strtok = new StringTokenizer(file, ".");
        
        if(strtok.countTokens() != 2){
            return false;
        }
        
        strtok = new StringTokenizer(strtok.nextToken(), "-");
        
        if(strtok.countTokens() != 2){
            return false;
        }
        
        if(banda.equals(strtok.nextToken()) && musica.equals(strtok.nextToken())){
            return true;
        }
        else{
            return false;
        }
    }
}
