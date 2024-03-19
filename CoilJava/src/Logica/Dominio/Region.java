package Logica.Dominio;

public class Region {
    private String nombre;

    public Region () {

    }

    private Region (String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
