package com.example.enums;

public enum Estado {
    PUBLICADO,
    CANCELADO,
    ACEPTADO,
    RECHAZADO;

    public static Estado fromString(String abbr) {
        switch (abbr) {
            case "P":
                return PUBLICADO;
            case "A":
                return ACEPTADO;
            case "C":
                return CANCELADO;
            case "R":
                return RECHAZADO;
            default:
                throw new IllegalArgumentException("Valor desconocido para Estado: " + abbr);
        }
    }
}
    

