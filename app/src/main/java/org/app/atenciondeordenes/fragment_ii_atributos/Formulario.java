package org.app.atenciondeordenes.fragment_ii_atributos;

import java.util.List;

/**
 * Created by Alexander Jimenez (alexanderenriquejm@gmail.com) on 12/30/16.
 */

public interface Formulario {
    Long getOrdenId();
    Long getAtricons();
    String getAtridesc();
    String getCompExtjs();
    FromEnums getCompAndroid();
    List<String>getGrupo();
    Boolean getRequerido();
    List<String> getValores();
    String getValor();
    void setValor(String valor);

}
