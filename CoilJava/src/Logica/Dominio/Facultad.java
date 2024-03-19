package Logica.Dominio;

public class Facultad {
    private String nombre;
    private Region region;

    public Facultad () {

    }

    public Facultad (String nombre, Region region) {
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

}
