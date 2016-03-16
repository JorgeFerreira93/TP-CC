package servidor;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Servidor extends Thread {
    
    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(6789);

        while(true){
            Socket connectionSocket = serverSocket.accept();

            InputStream in = connectionSocket.getInputStream();

            byte[] b = new byte[8];

            in.read(b);

            System.out.println("Recebi: " + b.toString());

            OutputStream out = connectionSocket.getOutputStream();           
            out.write(b);
        }
    }
}
