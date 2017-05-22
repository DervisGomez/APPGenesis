package org.app.appgenesis;

/**
 * Created by Administrador on 05/01/2017.
 */
public class Globals {

    private static Globals instance;

    // Token de acceso a los servicios
    private String access_token;

    // Tipo de Token
    private String token_type;

    // Token de refrescar
    private String refresh_token;

    // Tiempo de expiracion
    private Long expires_in;

    //Ambito del Token
    private String scope;

    private  String jti;

    // Direccion del servidor
    private String server;

    // Direccion del servidor para obtener el Token
    private String server_token;

    private String usuario_dominio;

    private String usuario_cons;


    private Globals(){}

    public static void setInstance(Globals instance) {
        Globals.instance = instance;
    }

    public String getUsuario_dominio() {
        return usuario_dominio;
    }

    public void setUsuario_dominio(String usuario_dominio) {
        this.usuario_dominio = usuario_dominio;
    }

    public String getUsuario_cons() {
        return usuario_cons;
    }

    public void setUsuario_cons(String usuario_cons) {
        this.usuario_cons = usuario_cons;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public String getServer() {
        return server;
    }


    public String getServer_token() {        return server_token;    }

    public void setServer_token(String server_token) {        this.server_token = server_token;    }

    public void setServer(String server) {
        this.server = server;
    }


    @Override
    public String toString() {
        return "Globals{" +
                "access_token=" + access_token +
                ", token_type=" + token_type +
                ", refresh_token=" + refresh_token +
                ", expires_in=" + expires_in +
                ", scope=" + scope +
                ", jti=" + jti +
                ", server=" + server +
                "}";
    }

    public static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
        }
        return instance;
    }
}
