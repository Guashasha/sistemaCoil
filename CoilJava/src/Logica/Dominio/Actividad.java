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

    public Actividad (String titulo, String descripcion, TipoActividad tipo) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipo = tipo;
    }

    public Actividad () {}

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

    public boolean equals (Actividad actividad) {
        return this.titulo == actividad.getTitulo() &&
                this.descripcion == actividad.getDescripcion() &&
                this.tipo == actividad.getTipo();
    }

    public boolean esCorrecta () {
        boolean resultado = true;

        if (this.getTitulo().isBlank() || this.getTitulo() == null) {
            resultado = false;
        }
        else if (this.getDescripcion().isBlank() || this.getDescripcion() == null) {
            resultado = false;
        }
        else if (this.getTipo() == null) {
            resultado = false;
        }

        return resultado;
    }
}
