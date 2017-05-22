package org.app.appgenesis.dao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table ORDE_PERS.
 */
public class OrdePers {

    private Long id;
    private Long idPersona;
    private Long idOrden;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public OrdePers() {
    }

    public OrdePers(Long id) {
        this.id = id;
    }

    public OrdePers(Long id, Long idPersona, Long idOrden) {
        this.id = id;
        this.idPersona = idPersona;
        this.idOrden = idOrden;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Long idPersona) {
        this.idPersona = idPersona;
    }

    public Long getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(Long idOrden) {
        this.idOrden = idOrden;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
