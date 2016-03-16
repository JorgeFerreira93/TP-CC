package servidor;

import java.net.Socket;
import java.util.HashMap;
import java.io.InputStream;
import java.io.OutputStream;

public class ServidorThread extends Thread{

    protected HashMap<String,Utilizador> tabel;
    private Socket client;

    public ServidorThread(HashMap<String,Utilizador> newtabel,Socket newclient){
      this.tabel=newtabel;
      this.client=newclient;
    }

    public void run(){
      try{
        InputStream in = client.getInputStream();

        byte[] b = new byte[8];

        in.read(b);

        System.out.println("Recebi: " + b.toString());

        OutputStream out = client.getOutputStream();
        out.write(b);
      }catch(Exception e){}//POR AQUI ALGUMA COISA
    }
}
