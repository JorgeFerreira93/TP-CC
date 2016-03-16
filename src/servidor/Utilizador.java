package servidor;

public class Utilizador{
  private String id;
  private String port;
  private String ip;

  public Utilizador(String newid,String newport,String newip){
    this.id=newid;
    this.port=newport;
    this.ip=newip;
  }

  String getId(){
    return this.id;
  }

  String getPort(){
    return this.port;
  }

  String getIp(){
    return this.ip;
  }
}
