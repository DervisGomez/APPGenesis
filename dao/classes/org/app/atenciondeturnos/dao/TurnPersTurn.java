package org.app.atenciondeturnos.dao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table TURN_PERS_TURN.
 */
public class TurnPersTurn {

    private Long id;
    private Long idPersona;
    private Long idTurno;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public TurnPersTurn() {
    }

    public TurnPersTurn(Long id) {
        this.id = id;
    }

    public TurnPersTurn(Long id, Long idPersona, Long idTurno) {
        this.id = id;
        this.idPersona = idPersona;
        this.idTurno = idTurno;
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

    public Long getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(Long idTurno) {
        this.idTurno = idTurno;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}