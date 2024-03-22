package Logica.Dominio;

public class RetroalimentacionActividad extends Retroalimentacion {
    private int idActividad;
    private int dificultad;
    private int interes;

    public RetroalimentacionActividad () {
        super();
    }

    public int getId () {
        return idActividad;
    }

    public void setId (int id) {
        this.idActividad = id;
    }

    public int getDificultad () {
        return dificultad;
    }

    public void setDificultad (int dificultad) {
        this.dificultad = dificultad;
    }

    public int getInteres () {
        return interes;
    }

    public void setInteres (int interes) {
        this.interes = interes;
    }
}
