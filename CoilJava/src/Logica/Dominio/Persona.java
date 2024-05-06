package Logica.Dominio;

public abstract class Persona {
    private int idPersona;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private int idUniversidad;

    public Persona () {}

    public Persona(int idPersona, String nombre, String apellidoPaterno, String apellidoMaterno, int idUniversidad) {
        this.idPersona = idPersona;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.idUniversidad = idUniversidad;
    }

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

    public abstract boolean validarNulos();

    protected boolean cadenaValida(String cadena) {
        return cadena != null && !cadena.isBlank();
    }

}
