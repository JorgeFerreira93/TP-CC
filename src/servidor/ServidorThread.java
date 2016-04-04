package servidor;

import java.net.Socket;
import java.util.HashMap;
import java.io.InputStream;
import java.io.OutputStream;

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

            if(pdu[2] == 1){
                registaPDU(pdu);
            }
            else{
                dummyPDU();
            }
        }
        
      }catch(Exception e){}//POR AQUI ALGUMA COISA
    }
    
    private void registaPDU(byte[] pdu){
        
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
    }
    
    private void dummyPDU(){
        System.out.println("Recebi algo do User: " + this.utilizador.getId() + 
                                          ", id: " + this.utilizador.getIdTabela() + 
                                          ", ip: " + this.utilizador.getIp() + 
                                          ", porta: " + this.utilizador.getPort());
    }
}
