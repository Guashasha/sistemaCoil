package Logica.Dominio;

public class CuentaAcademico {
    private enum EstadoCuenta {
        aceptada,
        rechazada
    }
    private Academico academico;
    private String nombreUsuario;
    private String contrasena;
    private EstadoCuenta estado;

    public Academico getAcademico () {
        return academico;
    }

    public void setAcademico (Academico academico) {
        this.academico = academico;
    }

    public String getNombreUsuario () {
        return nombreUsuario;
    }

    public void setNombreUsuario (String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena () {
        return contrasena;
    }

    public void setContrasena (String contrasena) {
        this.contrasena = contrasena;
    }

    public EstadoCuenta getEstado () {
        return estado;
    }

    public void setEstado (EstadoCuenta estado) {
        this.estado = estado;
    }
}
