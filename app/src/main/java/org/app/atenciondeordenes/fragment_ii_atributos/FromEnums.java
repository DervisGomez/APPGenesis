package org.app.atenciondeordenes.fragment_ii_atributos;

/**
 * Created by Alexander Jimenez (alexanderenriquejm@gmail.com) on 12/29/16.
 */

public enum FromEnums {
    CHECK_BOX("CheckBox"),
    RADIO_BUTTON("RadioButton"),
    SPINNER("Spinner"),
    EDIT_TEXT_TEXT("EditTextText"),
    EDIT_TEXT_DATE("EditTextDate"),
    EDIT_TEXT_AREA("EditTextArea"),
    EDIT_TEXT_NUMERIC("EditTextNumeric");
    private String type;


    FromEnums(String type){

        this.type = type;
    }

    public static FromEnums getEnumByType(String type) {
        for (FromEnums items : FromEnums.values()) {
            if (items.type.equals(type) ) {
                return items;
            }
        }
        return null;
    }
}
