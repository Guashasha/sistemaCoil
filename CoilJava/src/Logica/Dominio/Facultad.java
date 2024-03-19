package Logica.Dominio;

public class Facultad {
    private String nombre;

    public Facultad () {

    }

    public Facultad (String nombre) {
        this.nombre = nombre;
    }

    public String getNombre () {
        return nombre;
    }

    public void setNombre (String nombre) {
        this.nombre = nombre;
    }
}
