package DTO;

import Utilidades.ErrorDAO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Clase que representa una cuenta de usuario.
 */
public class CuentaDTO {

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
        verificarUsuario(nombreUsuario);
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena () {
        return contrasena;
    }

    public void setContrasena (String contrasena) {
        verificarContrasena(contrasena);
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

    /**
     * Verifica la validez del nombre de usuario.
     *
     * @param usuario el nombre de usuario.
     * @throws ErrorDAO si el nombre de usuario no es válido.
     */
    private void verificarUsuario (String usuario) {
        String usuarioRegex = "[A-z0-9]{1,50}";

        Pattern patron = Pattern.compile(usuarioRegex);
        if (usuario == null || usuario.isEmpty()) {
            throw new ErrorDAO("El nombre de usuario no puede estar vacío", ErrorDAO.Tipo.VALIDACION);
        }
        Matcher matcher = patron.matcher(usuario);
        if (!matcher.matches()) {
            throw new ErrorDAO("""
                                                       El nombre de usuario no es válido.
                                                       1. La longitud debe ser de mínimo 2 caracteres y máximo 50
                                                       2. No debe tener espacios al principio ni al final.
                                                       3. No se permiten caracteres especiales.""", ErrorDAO.Tipo.VALIDACION);
        }
    }

    /**
     * Verifica la validez de la contraseña.
     *
     * @param contrasena la contraseña.
     * @throws ErrorDAO si la contraseña no es válida.
     */
    private void verificarContrasena (String contrasena) {
        String contrasenaRegex = "^.{1,300}$";
        Pattern patron = Pattern.compile(contrasenaRegex);
        if (contrasena == null || contrasena.isEmpty()) {
            throw new ErrorDAO("La contraseña no puede estar vacía", ErrorDAO.Tipo.VALIDACION);
        }
        Matcher matcher = patron.matcher(contrasena);
        if (!matcher.matches()) {
            throw new ErrorDAO("""
                                                       La contraseña no es válida.
                                                       1. La longitud de la contraseña debe ser mayor a 2 y menor a 300 caracteres.""", ErrorDAO.Tipo.VALIDACION);
        }
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

    public boolean esLongitudValida () {
        return nombreUsuario.length() <= 50 &&
                contrasena.length() <= 300;
    }

    /**
     * Compara esta cuenta con otro objeto para verificar la igualdad.
     *
     * @param obj el objeto a comparar.
     * @return true si los objetos son iguales, false en caso contrario.
     */
    @Override
    public boolean equals (Object obj) {
        boolean igual;
        if (this == obj) {
            igual = true;
        }
        else if (!(obj instanceof CuentaDTO)) {
            igual = false;
        }
        else {
            CuentaDTO cuentaDTO = (CuentaDTO) obj;
            igual = this.idCuenta == cuentaDTO.getIdCuenta() && this.idPersona == cuentaDTO.getIdPersona() &&
                    this.nombreUsuario.equals(cuentaDTO.getNombreUsuario()) && this.contrasena.equals(cuentaDTO.getContrasena()) &&
                    this.estado.toString()
                               .equals(cuentaDTO.getEstado()
                                             .toString()) && this.tipo.toString()
                                                                      .equals(cuentaDTO.getTipo()
                                                                                    .toString());
        }
        return igual;
    }
}
