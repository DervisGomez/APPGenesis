package org.app.appgenesis.dao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table GOP__ATRIBUTOS.
 */
public class GOP_ATRIBUTOS {

    private Long id;
    private String json;
    private String titr;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public GOP_ATRIBUTOS() {
    }

    public GOP_ATRIBUTOS(Long id) {
        this.id = id;
    }

    public GOP_ATRIBUTOS(Long id, String json, String titr) {
        this.id = id;
        this.json = json;
        this.titr = titr;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getTitr() {
        return titr;
    }

    public void setTitr(String titr) {
        this.titr = titr;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}