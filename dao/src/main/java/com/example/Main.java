package com.example;


import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class Main {

    public static void main(String[] args) throws Exception {
        Schema schemaTurno = new Schema(1,"org.app.atenciondeturnos.dao");
        schemaTurno.enableKeepSectionsByDefault();
        createDataBaseTurno(schemaTurno);
        //DaoGenerator generatorTurno = new DaoGenerator();
        //generatorTurno.generateAll(schemaTurno,args[0]);

        Schema schema = new Schema(1,"org.app.appgenesis.dao");
        schema.enableKeepSectionsByDefault();
        createDataBase(schema);
        DaoGenerator generator = new DaoGenerator();
        generator.generateAll(schema,args[0]);
        generator.generateAll(schemaTurno,args[0]);
    }

    public static void createDataBaseTurno(Schema schema){
        Entity turnos =schema.addEntity("GDB_TURNOS");
        turnos.addIdProperty();
        turnos.addLongProperty("TURNCONS");//ID TURNOS
        turnos.addStringProperty("TURNZONA");//ZONA DEL TURNO
        turnos.addStringProperty("TURNSECT");//CODIGO SECTOR DEL TURNO
        turnos.addStringProperty("TURNSEDE");//DESCRIPCION SECTOR DEL TURNO
        turnos.addStringProperty("TURNESTA");//ESTADO DEL TURNO
        turnos.addStringProperty("TURNUSCR");//USUARIO DE CREACION
        turnos.addStringProperty("TURNFECR");//FECHA DE CREACION
        turnos.addStringProperty("Descargado");//FECHA DE CREACION

        Entity turnpers=schema.addEntity("GDB_TURNPERS");
        turnpers.addIdProperty();
        turnpers.addLongProperty("TUPECONS");//ID PERSONAS POR TURNOS
        turnpers.addLongProperty("TUPETURN");//ID TURNOS
        turnpers.addStringProperty("TUPEPERS");//PERSONA O VALVULERO ASOCIADO AL TURNO
        turnpers.addStringProperty("TUPEFEIN");//FECHA INICIO TURNO VALVULERO
        turnpers.addStringProperty("TUPEFEFI");//FECHA FINAL TURNO VALVULERO
        turnpers.addStringProperty("TUPEOBSE");//OBSERVACION DEL TURNO
        turnpers.addStringProperty("TUPEINIC");//INICIA TURNO (S/N)
        turnpers.addStringProperty("TUPEFINA");//FINALIZA TURNO (S/N)
        turnpers.addStringProperty("TUPEESTA");//ESTADO
        turnpers.addStringProperty("TUPEUSCR");//USUARIO DE CREACION
        turnpers.addStringProperty("TUPEFECR");//FECHA DE CREACION

        Entity gdb_valvula =schema.addEntity("GDB_VALVULA");
        gdb_valvula.addIdProperty();
        gdb_valvula.addLongProperty("VALVCONS");//ID VALVULAS
        gdb_valvula.addStringProperty("VALVDESC");//DESCRIPCION
        gdb_valvula.addStringProperty("VALVDIRE");//DIRECCION DE LAS VALVULAS
        gdb_valvula.addStringProperty("VALVDIAM");//DIAMETRO (PULGADAS)
        gdb_valvula.addStringProperty("VALVCOOR");//COORDENADAS - GDB_TABLAS (N:Norte, S:Sur, SE, NE, SO, NO)
        gdb_valvula.addStringProperty("VALVESTA");//ESTADO
        gdb_valvula.addStringProperty("VALVUSCR");//USUARIO DE CREACION
        gdb_valvula.addStringProperty("VALVFECR");//FECHA DE CREACION

        Entity comentario = schema.addEntity("TurnCome");
        comentario.addIdProperty();
        comentario.addStringProperty("comecons");
        comentario.addStringProperty("comedesc");
        comentario.addStringProperty("comedano");
        comentario.addStringProperty("comeorde");
        comentario.addStringProperty("comesoli");
        comentario.addStringProperty("comequej");
        comentario.addStringProperty("comerevi");
        comentario.addStringProperty("comeuscr");
        comentario.addStringProperty("comefecr");
        comentario.addStringProperty("comeesta");

        Entity persona = schema.addEntity("TurnPers");
        persona.addIdProperty();
        persona.addIntProperty("perscons");
        persona.addIntProperty("persaror");
        persona.addIntProperty("persopid");
        persona.addIntProperty("perstipo");
        persona.addStringProperty("persnomb");
        persona.addIntProperty("perstiid");
        persona.addStringProperty("persiden");
        persona.addStringProperty("persusop");
        persona.addStringProperty("persusua");
        persona.addStringProperty("persesta");
        persona.addStringProperty("persuscr");
        persona.addStringProperty("persfecr");
        persona.addStringProperty("unoppers");
        persona.addStringProperty("peuopers");

        Entity turnPers = schema.addEntity("TurnPersTurn");
        turnPers.addIdProperty();

        Entity material = schema.addEntity("TurnMate");
        material.addIdProperty();
        material.addIntProperty("matecons");
        material.addIntProperty("matecodi");
        material.addStringProperty("matedesc");
        material.addIntProperty("mateclas");
        material.addIntProperty("mateunme");
        material.addStringProperty("mategara");
        material.addStringProperty("mateexis");
        material.addStringProperty("matecost");
        material.addIntProperty("mateesta");
        material.addStringProperty("mateuscr");
        material.addStringProperty("matefecr");
        material.addStringProperty("mattmate");

        Entity firma = schema.addEntity("TurnFirm");
        firma.addIdProperty();
        firma.addStringProperty("codigo");
        firma.addStringProperty("nombre");
        firma.addStringProperty("Correo");
        firma.addStringProperty("fecha");
        firma.addStringProperty("estado");
        firma.addStringProperty("usuario");
        firma.addStringProperty("foto");

        Entity turnMate = schema.addEntity("TurnMateTurn");
        turnMate.addIdProperty();
        turnMate.addIntProperty("cantidad");

        Property idTurnComentario = comentario.addLongProperty("idTurn").getProperty();
        ToMany turnComentario= turnos.addToMany(comentario, idTurnComentario);
        turnComentario.setName("comentario");

        Property idPersTurnPers = turnPers.addLongProperty("idPersona").getProperty();
        ToMany persTurnPers = persona.addToMany(turnPers,idPersTurnPers);
        persTurnPers.setName("turnPers");

        Property idTurnTurnPers = turnPers.addLongProperty("idTurno").getProperty();
        ToMany turnTurnPers = turnos.addToMany(turnPers,idTurnTurnPers);
        turnTurnPers.setName("turnPers");

        Property idMateTurnMate = turnMate.addLongProperty("idMaterial").getProperty();
        ToMany mateTurnMate = material.addToMany(turnMate,idMateTurnMate);
        mateTurnMate.setName("turnMate");

        Property idTurnTurnMate = turnMate.addLongProperty("idTurno").getProperty();
        ToMany turnTurnMate= turnos.addToMany(turnMate,idTurnTurnMate);
        turnTurnMate.setName("turnMate");

        Entity unidadMedida= schema.addEntity("TurnUnidMedi");
        unidadMedida.addIdProperty();
        unidadMedida.addStringProperty("consecutivo");
        unidadMedida.addStringProperty("valor");
        unidadMedida.addStringProperty("simbolo");

        Property idTurnoFirma = firma.addLongProperty("idTurno").getProperty();
        ToMany turnoFirma = turnos.addToMany(firma, idTurnoFirma);
        turnoFirma.setName("firma");

        Entity gma = schema.addEntity("GMA_PKIDTurno");
        gma.addIdProperty();
        gma.addStringProperty("Tabla");
        gma.addIntProperty("cantidad");
    }

    public static void createDataBase(Schema schema){
        Entity persona = schema.addEntity("Persona");
        persona.addIdProperty();
        persona.addIntProperty("perscons");
        persona.addIntProperty("persaror");
        persona.addIntProperty("persopid");
        persona.addIntProperty("perstipo");
        persona.addStringProperty("persnomb");
        persona.addIntProperty("perstiid");
        persona.addStringProperty("persiden");
        persona.addStringProperty("persusop");
        persona.addStringProperty("persusua");
        persona.addStringProperty("persesta");
        persona.addStringProperty("persuscr");
        persona.addStringProperty("persfecr");
        persona.addStringProperty("unoppers");
        persona.addStringProperty("peuopers");

        Entity ordePers = schema.addEntity("OrdePers");
        ordePers.addIdProperty();

        Entity material = schema.addEntity("Material");
        material.addIdProperty();
        material.addIntProperty("matecons");
        material.addIntProperty("matecodi");
        material.addStringProperty("matedesc");
        material.addIntProperty("mateclas");
        material.addIntProperty("mateunme");
        material.addStringProperty("mategara");
        material.addStringProperty("mateexis");
        material.addStringProperty("matecost");
        material.addIntProperty("mateesta");
        material.addStringProperty("mateuscr");
        material.addStringProperty("matefecr");
        material.addStringProperty("mattmate");

        Entity ordeMate = schema.addEntity("OrdeMate");
        ordeMate.addIdProperty();
        ordeMate.addStringProperty("cantidad");

        Entity dibujo =schema.addEntity("Dibujo");
        dibujo.addIdProperty();
        dibujo.addFloatProperty("x");
        dibujo.addFloatProperty("y");
        dibujo.addStringProperty("accion");

        Entity firma = schema.addEntity("Firma");
        firma.addIdProperty();
        firma.addStringProperty("documento");
        firma.addStringProperty("nombre");
        firma.addStringProperty("Correo");
        firma.addStringProperty("fecha");
        firma.addStringProperty("estado");
        firma.addStringProperty("usuario");
        firma.addStringProperty("foto");

        Entity comentario = schema.addEntity("Comentario");
        comentario.addIdProperty();
        comentario.addStringProperty("comecons");
        comentario.addStringProperty("comedesc");
        comentario.addStringProperty("comedano");
        comentario.addStringProperty("comeorde");
        comentario.addStringProperty("comesoli");
        comentario.addStringProperty("comequej");
        comentario.addStringProperty("comerevi");
        comentario.addStringProperty("comeuscr");
        comentario.addStringProperty("comefecr");
        comentario.addStringProperty("comeesta");

        Entity fotografia = schema.addEntity("Fotografia");
        fotografia.addIdProperty();
        fotografia.addLongProperty("Fecha");
        fotografia.addStringProperty("Descripcion");
        fotografia.addStringProperty("Foto");

        Entity atributo = schema.addEntity("Gop_ordeatri");
        atributo.addIdProperty();
        atributo.addLongProperty("atricons");
        atributo.addStringProperty("atridesc");
        atributo.addStringProperty("comp_extjs");
        atributo.addStringProperty("comp_android");
        atributo.addStringProperty("grupo");
        atributo.addStringProperty("requerido");
        atributo.addStringProperty("valores");
        atributo.addStringProperty("valor");
        atributo.addStringProperty("creacion");
        atributo.addStringProperty("usuario");
        atributo.addStringProperty("respuesta");

        Entity atribut = schema.addEntity("GOP_ATRIBUTOS");
        atribut.addIdProperty();
        atribut.addStringProperty("json");
        atribut.addStringProperty("titr");

        Entity estado = schema.addEntity("GOP_ORDEESTA");
        estado.addIdProperty();
        estado.addStringProperty("ORESCONS");
        estado.addStringProperty("ORESORDE");
        estado.addStringProperty("ORESESTA");
        estado.addStringProperty("ORESOBSE");
        estado.addStringProperty("ORESUSCR");
        estado.addStringProperty("ORESFECR");

        Entity cobro = schema.addEntity("Gma_costtitr");
        cobro.addIdProperty();
        cobro.addLongProperty("csttcons");
        cobro.addLongProperty("cstttitr");
        cobro.addStringProperty("csttnomb");
        cobro.addStringProperty("csttdesc");
        cobro.addStringProperty("csttvalo");
        cobro.addStringProperty("csttfein");
        cobro.addStringProperty("csttfefi");
        cobro.addLongProperty("csttesta");
        cobro.addStringProperty("csttuscr");
        cobro.addStringProperty("csttfecr");

        Entity orden = schema.addEntity("Gro_orden");
        orden.addIdProperty();
        orden.addIntProperty("ORDECONS");//
        orden.addStringProperty("ORDEORAM");//
        orden.addStringProperty("ORDECONT");//
        orden.addStringProperty("ORDECAUS");//
        orden.addStringProperty("ORDETIDO");//
        orden.addStringProperty("ORDENUDO");//
        orden.addStringProperty("ORDEPADR");//
        orden.addStringProperty("ORDEFIEJ");//
        orden.addStringProperty("ORDEFFEJ");//
        orden.addStringProperty("ORDEFEEJ");//
        orden.addStringProperty("ORDEFEAS");//
        orden.addStringProperty("ORDEFELE");//
        orden.addStringProperty("ORDEVALU");//
        orden.addStringProperty("ORDEUNOP");//
        orden.addStringProperty("ORDETITR");//
        orden.addStringProperty("ORDECALE");//
        orden.addStringProperty("ORDEMOLE");//
        orden.addStringProperty("ORDEOBSE");//
        orden.addStringProperty("ORDEEFEC");//
        orden.addStringProperty("ORDETERM");//
        orden.addStringProperty("ORDELATI");//
        orden.addStringProperty("ORDELONG");//
        orden.addStringProperty("ORDEALTI");//
        orden.addStringProperty("ORDEESTA");//
        orden.addStringProperty("ORDEUSCR");//
        orden.addStringProperty("ORDEFECR");//
        orden.addStringProperty("UNOPNOMB");//
        orden.addStringProperty("TITRDESC");//
        orden.addStringProperty("ORESESTA");//
        orden.addStringProperty("TIPODOCU");//
        orden.addStringProperty("CAUSLEGA");//
        orden.addStringProperty("MOTILEGA");//
        orden.addStringProperty("CONTCODI");//
        orden.addStringProperty("CONTNOMB");//
        orden.addStringProperty("CONCCICL");//
        orden.addStringProperty("CONTDIRE");//
        orden.addStringProperty("CONTBARR");//
        orden.addStringProperty("CONTTEFI");//
        orden.addStringProperty("CONTTECE");//
        orden.addStringProperty("CONTRULE");//
        orden.addStringProperty("VALOBARR");//
        orden.addStringProperty("DANOPURE");//
        orden.addStringProperty("DANOLATI");//
        orden.addStringProperty("DANOLONG");//
        orden.addStringProperty("DANOALTI");//
        orden.addStringProperty("CAUSDESC");
        orden.addStringProperty("ESTADO");
        orden.addStringProperty("Descargado");

        Entity unidadMedida= schema.addEntity("UnidadMedida");
        unidadMedida.addIdProperty();
        unidadMedida.addStringProperty("consecutivo");
        unidadMedida.addStringProperty("valor");
        unidadMedida.addStringProperty("simbolo");

        Entity causal= schema.addEntity("GMA_CAUSAL");
        causal.addIdProperty();
        causal.addStringProperty("causcons");
        causal.addStringProperty("causdesc");
        causal.addStringProperty("caustipo");
        causal.addStringProperty("causesta");
        causal.addStringProperty("caususcr");
        causal.addStringProperty("causfecr");

        Entity ordeCost= schema.addEntity("Ordecost");
        ordeCost.addIdProperty();
        ordeCost.addStringProperty("usua");
        ordeCost.addStringProperty("sscr");
        ordeCost.addBooleanProperty("gene");
        ordeCost.addStringProperty("valor");
        ordeCost.addStringProperty("resp");

        Entity gma = schema.addEntity("GMA_PKID");
        gma.addIdProperty();
        gma.addStringProperty("Tabla");
        gma.addIntProperty("Cantidad");

        Property idPersOrdePers = ordePers.addLongProperty("idPersona").getProperty();
        ToMany persOrdePers = persona.addToMany(ordePers,idPersOrdePers);
        persOrdePers.setName("ordePers");

        Property idOrdeOrdePers = ordePers.addLongProperty("idOrden").getProperty();
        ToMany ordeOrdePers = orden.addToMany(ordePers,idOrdeOrdePers);
        ordeOrdePers.setName("ordePers");

        Property idMateOrdeMate = ordeMate.addLongProperty("idMaterial").getProperty();
        ToMany mateOrdeMate = material.addToMany(ordeMate,idMateOrdeMate);
        mateOrdeMate.setName("ordeMate");

        Property idOrdeOrdeMate = ordeMate.addLongProperty("idOrden").getProperty();
        ToMany ordeOrdeMate= orden.addToMany(ordeMate,idOrdeOrdeMate);
        ordeOrdeMate.setName("ordeMate");

        Property idOrdenMaterial = material.addLongProperty("idOrden").getProperty();
        ToMany ordenMaterial = orden.addToMany(material, idOrdenMaterial);
        ordenMaterial.setName("material");

        Property idOrdenFirma = firma.addLongProperty("idOrden").getProperty();
        ToMany ordenFirma = orden.addToMany(firma, idOrdenFirma);
        ordenFirma.setName("firma");

        Property idFirmaDibujo = dibujo.addLongProperty("idFirma").getProperty();
        ToMany firmaDibujo = orden.addToMany(dibujo, idFirmaDibujo);
        firmaDibujo.setName("Dibujo");

        Property idOrdenComentario = comentario.addLongProperty("idOrden").getProperty();
        ToMany ordenComentario= orden.addToMany(comentario, idOrdenComentario);
        ordenComentario.setName("comentario");

        Property idOrdenFotografia = fotografia.addLongProperty("IdOrden").getProperty();
        ToMany ordenFotografia = orden.addToMany(fotografia, idOrdenFotografia);
        ordenFotografia.setName("fotografia");

        Property idOrdenAtributo = atributo.addLongProperty("IdOrden").getProperty();
        ToMany ordenAtributo = orden.addToMany(atributo, idOrdenAtributo);
        ordenAtributo.setName("atributo");

        Property idOrdenCost = ordeCost.addLongProperty("IdOrden").getProperty();
        ToMany ordenCoste= orden.addToMany(ordeCost, idOrdenCost);
        ordenCoste.setName("ordecost");

    }
}
