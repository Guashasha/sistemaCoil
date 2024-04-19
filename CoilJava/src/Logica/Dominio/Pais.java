package Logica.Dominio;

public class Pais {
    private int id;
    private String iso;
    private String nombre;

    public Pais () {
    }

    public Pais (int id) {
        this.id = id;
    }

    public Pais (String nombre) {
        this.nombre = nombre;
    }

    public Pais (int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Pais (int id, String iso, String nombre) {
        this.id = id;
        this.iso = iso;
        this.nombre = nombre;
    }

    public void setId (int id) {
        this.id = id;
    }

    public int getId () {
        return this.id;
    }

    public void setIso (String iso) {
        this.iso = iso;
    }

    public String getIso () {
        return this.iso;
    }

    public void setNombre (String nombre) {
        this.nombre = nombre;
    }

    public String getNombre () {
        return this.nombre;
    }

    @Override
    public boolean equals (Object obj) {
        boolean igual;
        if (this == obj) {
            igual = true;
        }
        else if (!(obj instanceof Pais)) {
            igual = false;
        }
        else {
            Pais pais = (Pais) obj;
            igual = this.id == pais.getId() && this.iso.equals(pais.getIso()) && this.nombre.equals(pais.getNombre())? true:false;
        }
        return igual;
    }
}
