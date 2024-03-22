package Logica.Dominio;

public class Region {
    int id;
    private String nombre;

    public Region () {

    }

    private Region (String nombre) {
        this.nombre = nombre;
    }

    private Region (int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getNombre () {
        return this.nombre;
    }

    public void setNombre (String nombre) {
        this.nombre = nombre;
    }

    public int getId () {
        return this.id;
    }

    public void setId (int id) {
        this.id = id;
    }
}
