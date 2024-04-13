package Logica.Dominio;

import AccesoADatos.UniversidadDB;

public class Universidad {
    private int id;
    private String nombre;
    private int idPais;

    public Universidad () {
    }

    public Universidad (int id) {
        this.id = id;
    }

    public Universidad (String nombre, int idPais) {
        this.nombre = nombre;
        this.idPais = idPais;
    }

    public Universidad (int id, String nombre, int idPais) {
        this.id = id;
        this.nombre = nombre;
        this.idPais = idPais;
    }

    public int getId () {
        return this.id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdPais () {
        return this.idPais;
    }

    public void setIdPais (int idPais) {
        this.idPais = idPais;
    }

    @Override
    public boolean equals (Object obj) {
        boolean igual;
        if (this == obj) {
            igual = true;
        }
        else if (!(obj instanceof Universidad)) {
            igual = false;
        }
        else {
            Universidad universidad = (Universidad) obj;
            igual = this.id == universidad.getId() && this.nombre.equals(universidad.getNombre()) && this.idPais == universidad.getIdPais()? true:false;
        }
        return igual;
    }

}
