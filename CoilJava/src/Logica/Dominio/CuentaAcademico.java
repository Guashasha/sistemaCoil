package Logica.Dominio;

public class CuentaAcademico {
    public enum EstadoCuenta {
        aceptada,
        rechazada
    }
    private String idAcademico;
    private int idCuenta;
    private String nombreUsuario;
    private String contrasena;
    private EstadoCuenta estado;

    public String getIdAcademico () {
        return idAcademico;
    }

    public void setIdAcademico (String idAcademico) {
        this.idAcademico = idAcademico;
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

    public int getIdCuenta () {
        return idCuenta;
    }

    public void setIdCuenta (int idCuenta) {
        this.idCuenta = idCuenta;
    }
}
