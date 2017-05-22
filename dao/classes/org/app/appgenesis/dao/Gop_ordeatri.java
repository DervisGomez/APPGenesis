package org.app.appgenesis.dao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table GOP_ORDEATRI.
 */
public class Gop_ordeatri {

    private Long id;
    private Long atricons;
    private String atridesc;
    private String comp_extjs;
    private String comp_android;
    private String grupo;
    private String requerido;
    private String valores;
    private String valor;
    private String creacion;
    private String usuario;
    private String respuesta;
    private Long IdOrden;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Gop_ordeatri() {
    }

    public Gop_ordeatri(Long id) {
        this.id = id;
    }

    public Gop_ordeatri(Long id, Long atricons, String atridesc, String comp_extjs, String comp_android, String grupo, String requerido, String valores, String valor, String creacion, String usuario, String respuesta, Long IdOrden) {
        this.id = id;
        this.atricons = atricons;
        this.atridesc = atridesc;
        this.comp_extjs = comp_extjs;
        this.comp_android = comp_android;
        this.grupo = grupo;
        this.requerido = requerido;
        this.valores = valores;
        this.valor = valor;
        this.creacion = creacion;
        this.usuario = usuario;
        this.respuesta = respuesta;
        this.IdOrden = IdOrden;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAtricons() {
        return atricons;
    }

    public void setAtricons(Long atricons) {
        this.atricons = atricons;
    }

    public String getAtridesc() {
        return atridesc;
    }

    public void setAtridesc(String atridesc) {
        this.atridesc = atridesc;
    }

    public String getComp_extjs() {
        return comp_extjs;
    }

    public void setComp_extjs(String comp_extjs) {
        this.comp_extjs = comp_extjs;
    }

    public String getComp_android() {
        return comp_android;
    }

    public void setComp_android(String comp_android) {
        this.comp_android = comp_android;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getRequerido() {
        return requerido;
    }

    public void setRequerido(String requerido) {
        this.requerido = requerido;
    }

    public String getValores() {
        return valores;
    }

    public void setValores(String valores) {
        this.valores = valores;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getCreacion() {
        return creacion;
    }

    public void setCreacion(String creacion) {
        this.creacion = creacion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public Long getIdOrden() {
        return IdOrden;
    }

    public void setIdOrden(Long IdOrden) {
        this.IdOrden = IdOrden;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}