package Logica.Dominio;

public abstract class Academico extends Persona {
    private int cedulaProfesional;
    private String areaEstudios;
    private String correoElectronico;
    private String numeroTelefono;

    public Academico() {
    }

    public int getCedulaProfesional() {
        return cedulaProfesional;
    }

    public void setCedulaProfesional(int cedulaProfesional) {
        this.cedulaProfesional = cedulaProfesional;
    }

    public String getAreaEstudios() {
        return areaEstudios;
    }

    public void setAreaEstudios(String areaEstudios) {
        this.areaEstudios = areaEstudios;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }
}
