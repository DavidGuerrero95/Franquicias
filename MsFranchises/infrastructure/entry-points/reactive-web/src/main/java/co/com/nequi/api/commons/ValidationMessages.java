package co.com.nequi.api.commons;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationMessages {

    public static final String MESSAGE_NULL_OR_EMPTY = "No debe ser nulo o vacio.";
    public static final String EMAIL_NOT_VALID = "Formato de correo inválido";
    public static final String MESSAGE_MIN_SIZE = "El valor mínimo permitido es 1.";
    public static final String MESSAGE_MAX_SIZE = "El valor máximo permitido es 99999.";
    public static final String MESSAGE_REGEXP_NAME = "El nombre debe tener hasta 50 caracteres y" +
            " solo puede contener letras (con acentos y Ñ), dígitos, espacios, y los siguientes símbolos: , ; {} + * |";
    public static final String MESSAGE_REGEXP_DESCRIPTION = "La descripción debe tener hasta 200 caracteres y " +
            "solo puede contener letras (con acentos y Ñ), dígitos, espacios, y los siguientes símbolos: , ; {} + * |";
}
