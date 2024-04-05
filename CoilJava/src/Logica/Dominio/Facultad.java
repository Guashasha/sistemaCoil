package Logica.Dominio;

public class Facultad {
    private int id;
    private String nombre;
    private int idRegion;

    public Facultad () {

    }

    public Facultad (int id) {
        this.id = id;
    }

    public Facultad (String nombre, int idRegion) {
        this.nombre = nombre;
        this.idRegion = idRegion;
    }

    public Facultad (int id, String nombre, int idRegion) {
        this.id = id;
        this.nombre = nombre;
        this.idRegion = idRegion;
    }

    public int getId () {
        return this.id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getNombre () {
        return nombre;
    }

    public void setNombre (String nombre) {
        this.nombre = nombre;
    }

    public void setIdRegion (int idRegion) {
        this.idRegion = idRegion;
    }

    public int getIdRegion () {
        return this.idRegion;
    }
}
