package servidor;

import jdk.nashorn.internal.runtime.regexp.joni.EncodingHelper;

public class Utilizador{
  private String id;
  private String port;
  private String ip;
  private int idTabela;

  public Utilizador(String newid,String newport,String newip, int idTabela){
    this.id=newid;
    this.port=newport;
    this.ip=newip;
    this.idTabela = idTabela;
  }

    public String getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }
    
    public int getIdTabela() {
        return idTabela;
    }
}
