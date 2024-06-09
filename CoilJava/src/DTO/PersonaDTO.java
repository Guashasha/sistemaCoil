package DTO;

import Utilidades.ErrorDAO;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class PersonaDTO {
    private int idPersona;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private int idUniversidad;

    public PersonaDTO() {}

    public PersonaDTO(int idPersona, String nombre, String apellidoPaterno, String apellidoMaterno, int idUniversidad) {
        this.idPersona = idPersona;
        setNombre(nombre);
        setApellidoPaterno(apellidoPaterno);
        setApellidoMaterno(apellidoMaterno);
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
        verificarNombre(nombre);
        this.nombre = nombre;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        verificarApellido(apellidoPaterno);
        this.apellidoPaterno = apellidoPaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        verificarApellido(apellidoMaterno);
        this.apellidoMaterno = apellidoMaterno;
    }

    public int getIdUniversidad () {
        return idUniversidad;
    }

    public void setIdUniversidad (int idUniversidad) {
        this.idUniversidad = idUniversidad;
    }

    private void verificarNombre (String nombre) {
        if (!esCadenaValida(nombre)) {
            throw new ErrorDAO("El nombre no puede estar vacío o compuesto solo de espacios en blanco", ErrorDAO.Tipo.VALIDACION);
        }
        String nombreRegex = "^.{1,20}$";
        Pattern patron = Pattern.compile(nombreRegex);
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorDAO("El nombre no puede estar vacío", ErrorDAO.Tipo.VALIDACION);
        }
        Matcher matcher = patron.matcher(nombre);
        if (!matcher.matches()) {
            throw new ErrorDAO("""
                                                       El nombre no es válido
                                                       1. Su longitud debe ser máximo 20 caracteres.""", ErrorDAO.Tipo.VALIDACION);
        }
    }
    private void verificarApellido (String apellido) {
        if (!esCadenaValida(apellido)) {
            throw new ErrorDAO("El nombre no puede estar vacío o compuesto solo de espacios en blanco", ErrorDAO.Tipo.VALIDACION);
        }
        String apellidosRegex = "^.{1,20}$";
        Pattern patron = Pattern.compile(apellidosRegex);
        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorDAO("Los apellidos no pueden estar vacíos", ErrorDAO.Tipo.VALIDACION);
        }
        Matcher matcher = patron.matcher(apellido);
        if (!matcher.matches()) {
            throw new ErrorDAO("""
                                                       El apellido no es válido
                                                       1. Su longitud debe ser máximo 20 caracteres""", ErrorDAO.Tipo.VALIDACION);
        }
    }

    public abstract boolean validarNulos();

    protected boolean esCadenaValida (String cadena) {
        return cadena != null && !cadena.trim().isEmpty();
    }

}
