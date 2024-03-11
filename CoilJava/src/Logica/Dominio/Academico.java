package Logica.Dominio;

public class Academico extends Persona {
    private int cedulaProfesional;
    private Universidad institucion;
    private String areaEstudios;
    private String correoElectronico;
    private String numeroTelefono;

    public Academico() {
    }

    public int getCedulaProfesional() {
        return this.cedulaProfesional;
    }

    public void setCedulaProfesional(int cedulaProfesional) {
        this.cedulaProfesional = cedulaProfesional;
    }

    public Universidad getInstitucion() {
        return this.institucion;
    }

    public void setInstitucion(Universidad institucion) {
        this.institucion = institucion;
    }

    public String getAreaEstudios() {
        return this.areaEstudios;
    }

    public void setAreaEstudios(String areaEstudios) {
        this.areaEstudios = areaEstudios;
    }

    public String getCorreoElectronico() {
        return this.correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getNumeroTelefono() {
        return this.numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }
}
