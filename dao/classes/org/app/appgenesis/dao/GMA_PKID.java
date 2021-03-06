package org.app.appgenesis.dao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table GMA__PKID.
 */
public class GMA_PKID {

    private Long id;
    private String Tabla;
    private Integer Cantidad;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public GMA_PKID() {
    }

    public GMA_PKID(Long id) {
        this.id = id;
    }

    public GMA_PKID(Long id, String Tabla, Integer Cantidad) {
        this.id = id;
        this.Tabla = Tabla;
        this.Cantidad = Cantidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTabla() {
        return Tabla;
    }

    public void setTabla(String Tabla) {
        this.Tabla = Tabla;
    }

    public Integer getCantidad() {
        return Cantidad;
    }

    public void setCantidad(Integer Cantidad) {
        this.Cantidad = Cantidad;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
