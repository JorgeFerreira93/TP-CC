package servidor;

public class Utilizador{
  private final String id;
  private final String port;
  private final String ip;
  private final int idTabela;

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
