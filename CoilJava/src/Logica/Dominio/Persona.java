package Logica.Dominio;

public abstract class Persona {
    private int idPersona;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private int idUniversidad;

    public int getIdPersona () {
        return idPersona;
    }

    public void setIdPersona (int idPersona) {
        this.idPersona = idPersona;
    }

    public String getNombre () {
        return this.nombre;
    }

    public String getApellidoPaterno () {
        return this.apellidoPaterno;
    }

    public String getApellidoMaterno () {
        return this.apellidoMaterno;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public int getIdUniversidad () {
        return idUniversidad;
    }

    public void setIdUniversidad (int idUniversidad) {
        this.idUniversidad = idUniversidad;
    }
}
