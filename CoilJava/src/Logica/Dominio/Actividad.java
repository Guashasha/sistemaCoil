package Logica.Dominio;

public class Actividad {
    public enum TipoActividad {
        rompeHielo,
        intercultural,
        disciplinar,
        cierre
    }
    private int idActividad;
    private String titulo;
    private String descripcion;
    private TipoActividad tipo;

    public int getIdActividad () {
        return idActividad;
    }

    public void setIdActividad (int idActividad) {
        this.idActividad = idActividad;
    }

    public String getTitulo () {
        return titulo;
    }

    public void setTitulo (String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion () {
        return descripcion;
    }

    public void setDescripcion (String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoActividad getTipo () {
        return tipo;
    }

    public void setTipo (TipoActividad tipo) {
        this.tipo = tipo;
    }
}
