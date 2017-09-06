package ca.uqam.projet.resources;

public class Erreur{

    Integer code;
    String message;

    public Erreur(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}