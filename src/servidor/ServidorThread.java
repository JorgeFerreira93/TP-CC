package servidor;

import java.net.Socket;
import java.util.HashMap;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class ServidorThread extends Thread{

    protected HashMap<Integer, Utilizador> tabel;
    private Socket client;
    private Utilizador utilizador;

    public ServidorThread(HashMap<Integer, Utilizador> newtabel,Socket newclient){
      this.tabel=newtabel;
      this.client=newclient;
    }

    public void run(){
      try{
          
        while(true){
            InputStream in = client.getInputStream();

            byte[] pdu = new byte[50];

            in.read(pdu);

            switch(pdu[2]){
                case 1:
                    registaPDU(pdu);
                    break;
                case 2:
                    consultRequest(pdu);
                    break;
                default:
                    break;
            }
        }
        
      }catch(Exception e){}//POR AQUI ALGUMA COISA
    }
    
    private void registaPDU(byte[] pdu) throws Exception{
        
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
        
        utilizador = new Utilizador(nome, porta, ip, tabel.size() + 1);
        
        tabel.put(tabel.size()+1, utilizador);
        
        System.out.println("User: " + nome + ", id: " + tabel.size() + ", ip: " + ip + ", porta: " + porta);
        OutputStream out = client.getOutputStream();
        out.write(utilizador.getIdTabela());
    }
    
    private void consultRequest(byte[] pdu) throws Exception{
        
        ArrayList<String> aux = new ArrayList<>();
        
        for(Utilizador u: tabel.values()){
            if(u.getIdTabela() != utilizador.getIdTabela()){
                
                Socket clientSocket = new Socket(u.getIp(), Integer.parseInt(u.getPort()));
                
                OutputStream out = clientSocket.getOutputStream();
                out.write(pdu);
                
                InputStream in = clientSocket.getInputStream();
                
                byte[] pduResponse = new byte[50];
                in.read(pduResponse);
                
                int i;
                String id = "";

                for(i=7; (char)pduResponse[i] != '\0'; i++){
                    id += (char)pduResponse[i];
                }
                
                aux.add(id);
            }
        }
        
        System.out.println(aux.toString());
    }
}
