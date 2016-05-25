package servidor;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


public class Servidor{

    public static void main(String[] args) throws Exception {

        ConcurrentHashMap<String, Utilizador> tabela = new ConcurrentHashMap<>();
        ServerSocket serverSocket = new ServerSocket(10000);

        while(true){
            Socket connectionSocket = serverSocket.accept();
            ServidorThread t=new ServidorThread(tabela,connectionSocket);
            t.start();
        }
    }
}
