package Logica.Dominio;

public class Region {
    private int id;
    private String nombre;

    public Region () {

    }

    public Region (String nombre) {
        this.nombre = nombre;
    }

    public Region (int id, String nombre) {
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

    @Override
    public boolean equals (Object obj) {
        boolean igual;
        if (this == obj) {
            igual = true;
        }
        else if (!(obj instanceof Region)) {
            igual = false;
        }
        else {
            Region region = (Region) obj;
            igual = this.id == region.getId() && this.nombre.equals(region.getNombre())? true:false;
        }
        return igual;
    }
}
