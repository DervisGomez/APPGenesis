package org.app.atenciondeordenes.fragment_ii_atributos;

import org.app.appgenesis.dao.Gop_ordeatri;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Alexander Jimenez (alexanderenriquejm@gmail.com) on 12/30/16.
 */

public class FormularioImpl implements Formulario {

    Long ordenId;
    Long atricons;
    String atridesc;
    String compExtjs;
    FromEnums compAndroid;
    List<String> grupo;
    Boolean requerido;
    List<String> valores;
    String valor;


    public FormularioImpl(Long ordenId,Long atricons,String atridesc,String compExtjs,String compAndroid,List<String> grupo,Boolean requerido,String valores, String valor){
        this.ordenId = ordenId;
        this.atricons = atricons;
        this.atridesc = atridesc;
        this.compExtjs = compExtjs;
        this.compAndroid = FromEnums.getEnumByType(compAndroid);
        this.grupo = grupo;
        this.requerido = requerido;
        this.valores = new ArrayList<>();
        this.valores = Arrays.asList(valores.split(","));
        this.valor = valor;

    }

    public FormularioImpl(JSONObject jsonObj) throws JSONException {
        this.atricons = jsonObj.getLong("atricons");
        this.atridesc = jsonObj.getString("atridesc");
        this.compExtjs = jsonObj.getString("comp_extjs");
        this.compAndroid = FromEnums.getEnumByType(jsonObj.getString("comp_android"));
        this.grupo = Arrays.asList(jsonObj.getString("grupo").split("-"));
        if(jsonObj.getString("requerido").equals("SI")){
            this.requerido = true;

        }else if(jsonObj.getString("requerido").equals("NO")){
            this.requerido = false;
        }
        this.valores = new ArrayList<>();
        this.valores = Arrays.asList(jsonObj.getString("valores").split(","));

    }

    public FormularioImpl(Gop_ordeatri gopOrdeatri) {
        this.atricons = gopOrdeatri.getAtricons();
        this.atridesc = gopOrdeatri.getAtridesc();
        this.compExtjs = gopOrdeatri.getComp_extjs();
        this.compAndroid = FromEnums.getEnumByType(gopOrdeatri.getComp_android());
        this.grupo = Arrays.asList(gopOrdeatri.getGrupo().split("-"));
        if(gopOrdeatri.getRequerido().equals("SI")){
            this.requerido = true;

        }else if(gopOrdeatri.getRequerido().equals("NO")){
            this.requerido = false;
        }
        this.valores = new ArrayList<>();
        this.valores = Arrays.asList(gopOrdeatri.getValores().split(","));
        this.valor = gopOrdeatri.getValor();

    }

    @Override
    public Long getOrdenId() {
        return ordenId;
    }

    @Override
    public Long getAtricons() {
        return atricons;
    }

    @Override
    public String getAtridesc() {
        return atridesc;
    }

    @Override
    public String getCompExtjs() {
        return compExtjs;
    }

    @Override
    public FromEnums getCompAndroid() {
        return compAndroid;
    }

    @Override
    public List<String> getGrupo() {
        return grupo;
    }

    @Override
    public Boolean getRequerido() {
        return requerido;
    }

    @Override
    public List<String> getValores() {
        return valores;
    }

    @Override
    public String getValor() {
        return valor;
    }

    @Override
    public void setValor(String valor) {
        this.valor = valor;
    }
}
