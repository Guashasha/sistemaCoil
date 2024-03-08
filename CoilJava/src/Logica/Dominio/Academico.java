package Logica.Dominio;

public abstract class Academico extends Persona {
    private int cedulaProfesional;
    private String areaEstudios;
    private String correoElectronico;
    private String numeroTelefono;

    public Academico (int cedulaProfesional, String apelldioPaterno, String apellidoMaterno, String nombre, String areaEstudios, String correoElectronico, String numeroTelefono) {
        super(apelldioPaterno, apellidoMaterno, nombre);
        this.cedulaProfesional = cedulaProfesional;

    }

}
