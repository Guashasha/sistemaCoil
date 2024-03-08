package Logica.Dominio;

public abstract class Persona {
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;

    public Persona() {
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



}
