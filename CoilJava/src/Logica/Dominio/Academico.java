package Logica.Dominio;

public class Academico extends Persona {
    private String cedulaProfesional;
    private String numeroPersonal;
    private String areaEstudios;
    private String correoElectronico;
    private String numeroTelefonico;
    private String categoriaContratacion;
    private int idFacultad;

    public String getCedulaProfesional () {
        return cedulaProfesional;
    }

    public void setCedulaProfesional (String cedulaProfesional) {
        this.cedulaProfesional = cedulaProfesional;
    }

    public String getNumeroPersonal () {
        return numeroPersonal;
    }

    public void setNumeroPersonal (String numeroPersonal) {
        this.numeroPersonal = numeroPersonal;
    }

    public String getAreaEstudios () {
        return areaEstudios;
    }

    public void setAreaEstudios (String areaEstudios) {
        this.areaEstudios = areaEstudios;
    }

    public String getCorreoElectronico () {
        return correoElectronico;
    }

    public void setCorreoElectronico (String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getNumeroTelefonico () {
        return numeroTelefonico;
    }

    public void setNumeroTelefonico (String numeroTelefonico) {
        this.numeroTelefonico = numeroTelefonico;
    }

    public String getCategoriaContratacion () {
        return categoriaContratacion;
    }

    public void setCategoriaContratacion (String categoriaContratacion) {
        this.categoriaContratacion = categoriaContratacion;
    }

    public int getIdFacultad () {
        return idFacultad;
    }

    public void setIdFacultad (int idFacultad) {
        this.idFacultad = idFacultad;
    }
}
