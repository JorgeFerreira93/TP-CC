package servidor;

import business.PDU;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

public class ServidorThread extends Thread{

    protected ConcurrentHashMap<String, Utilizador> table;
    private Socket client;
    private Utilizador utilizador;

    public ServidorThread(ConcurrentHashMap<String, Utilizador> newtable, Socket newclient){
        this.table=newtable;
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
                    int id = registaPDU(pdu);

                    OutputStream out = client.getOutputStream();
                    out.write(id);

                    break;
                case 2:
                    
                    System.out.println("O utilizador " + utilizador.getIdTabela()+ " fez um pedido request");
                    
                    byte[] res = consultRequest(pdu);
                    DataOutputStream dout = new DataOutputStream(client.getOutputStream());
                    dout.writeInt(res.length);
                    dout.write(res);
                    break;
                default:
                    break;
            }
        }

      }catch(Exception e){}//POR AQUI ALGUMA COISA
    }

    private int registaPDU(byte[] pdu){

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

        if(table.containsKey(nome)){
            return -1;
        }

        table.put(nome, utilizador);
        
        System.out.println("O utilizador " + nome + " conectou-se");
        System.out.println(table.size());

        return utilizador.getIdTabela();
    }

    private byte[] consultRequest(byte[] pdu) throws Exception{

        ArrayList<byte[]> aux = new ArrayList<>();

        for(Utilizador u: table.values()){
            if(u.getIdTabela() != utilizador.getIdTabela()){

                /* Criar threads para cada cliente? */

                System.out.println("A enviar para o utilizador " + u.getIdTabela());
                
                Socket clientSocket = new Socket(u.getIp(), Integer.parseInt(u.getPort()));

                OutputStream out = clientSocket.getOutputStream();
                out.write(pdu);
                
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());

                int pduLenght = in.readInt();
                byte[] pduResponse = new byte[pduLenght];

                in.read(pduResponse);
                
                if((char)pduResponse[pduLenght-3] == '1'){
                    aux.add(Arrays.copyOfRange(pduResponse, 7, pduResponse.length - 4));
                    System.out.println("O utilizador " + u.getIdTabela() + " tem o ficheiro");
                }
                else{
                    System.out.println("O utilizador " + u.getIdTabela() + " não tem o ficheiro");
                }
            }
        }

        byte[] res = PDU.consultResponseListaPDU(aux);        
        
        return res;
    }
}
