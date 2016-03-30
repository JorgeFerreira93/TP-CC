package servidor;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


public class Servidor{

    public static void main(String[] args) throws Exception {

        HashMap<String,Utilizador> tabela = new HashMap<>();
        ServerSocket serverSocket = new ServerSocket(6789);
 
        while(true){
            Socket connectionSocket = serverSocket.accept();
            System.out.println(connectionSocket.getLocalAddress().toString());
            ServidorThread t=new ServidorThread(tabela,connectionSocket);
            t.start();
        }
    }
}
