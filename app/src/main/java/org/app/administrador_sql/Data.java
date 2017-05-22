package org.app.administrador_sql;

import java.util.List;

/**
 * Created by Alexander Jimenez (alexanderenriquejm@gmail.com) on 1/15/17.
 */

public class Data extends Item {

    List<String> data;
    public Data(List<String> data){
        this.data = data;
    }

    public List<String> getData() {
        return data;
    }
}
