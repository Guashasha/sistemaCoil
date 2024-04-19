package Logica.Dominio;

public class Cuenta {

    public enum TipoUsuario {
        academico,
        estudiante,
        administrador
    }
    public enum EstadoCuenta {
        pendiente,
        aceptada,
        rechazada
    }
    private int idCuenta;
    private int idPersona;
    private String nombreUsuario;
    private String contrasena;
    private EstadoCuenta estado;
    private TipoUsuario tipo;

    public int getIdCuenta () {
        return idCuenta;
    }

    public void setIdCuenta (int idCuenta) {
        this.idCuenta = idCuenta;
    }

    public int getIdPersona () {
        return idPersona;
    }

    public void setIdPersona (int idPersona) {
        this.idPersona = idPersona;
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

    public TipoUsuario getTipo () {
        return tipo;
    }

    public void setTipo (TipoUsuario tipo) {
        this.tipo = tipo;
    }

    public boolean validarNulos() {
        return cadenaValida(nombreUsuario) &&
                cadenaValida(contrasena) &&
                estado != null &&
                tipo != null;
    }

    private boolean cadenaValida(String cadena) {
        return cadena != null && !cadena.isBlank();
    }

    @Override
    public boolean equals (Object obj) {
        boolean igual;
        if (this == obj) {
            igual = true;
        }
        else if (!(obj instanceof Cuenta)) {
            igual = false;
        }
        else {
            Cuenta cuenta = (Cuenta) obj;
            igual = this.idCuenta == cuenta.getIdCuenta() && this.idPersona == cuenta.getIdPersona() &&
                    this.nombreUsuario.equals(cuenta.getNombreUsuario()) && this.contrasena.equals(cuenta.getContrasena()) &&
                    this.estado.toString().equals(cuenta.getEstado().toString()) && this.tipo.toString().equals(cuenta.getTipo().toString())
                    ? true:false;
        }
        return igual;
    }
}
