package Logica.Dominio;

public class Facultad {
    private int id;
    private String nombre;
    private Region region;

    public Facultad () {

    }

    public Facultad (String nombre, Region region) {
        this.nombre = nombre;
        this.region = region;
    }

    public Facultad (int id, String nombre, Region region) {
        this.id = id;
        this.nombre = nombre;
        this.region = region;
    }

    public String getNombre () {
        return nombre;
    }

    public void setNombre (String nombre) {
        this.nombre = nombre;
    }

    public void setRegion (Region region) {
        this.region = region;
    }

    public Region getRegion () {
        return this.region;
    }

    public int getId () {
        return this.id;
    }

    public void setId (int id) {
        this.id = id;
    }

}
