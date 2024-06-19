package DTO;

import Utilidades.ErrorDAO;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * La clase abstracta PersonaDTO funciona como la generalización de EstudianteSTO y AcademicoDTO. Contiene los datos esenciales de ambos objetos.
 */
public abstract class PersonaDTO {
    private int idPersona;
    private String nombre;
    private String apellidos;
    private int idUniversidad;

    public PersonaDTO() {}

    public PersonaDTO(int idPersona, String nombre, String apellidos, int idUniversidad) {
        this.idPersona = idPersona;
        setNombre(nombre);
        setApellidos(apellidos);
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

    public String getApellidos () {
        return this.apellidos;
    }

    public void setNombre(String nombre) {
        verificarNombre(nombre);
        this.nombre = nombre;
    }

    public void setApellidos (String apellidos) {
        verificarApellido(apellidos);
        this.apellidos = apellidos;
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
        String apellidosRegex = "^.{1,80}$";
        Pattern patron = Pattern.compile(apellidosRegex);
        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorDAO("Los apellidos no pueden estar vacíos", ErrorDAO.Tipo.VALIDACION);
        }
        Matcher matcher = patron.matcher(apellido);
        if (!matcher.matches()) {
            throw new ErrorDAO("""
                                                       El apellido no es válido
                                                       1. Su longitud debe ser máximo 80 caracteres""", ErrorDAO.Tipo.VALIDACION);
        }
    }

    public abstract boolean validarNulos();

    protected boolean esCadenaValida (String cadena) {
        return cadena != null && !cadena.trim().isEmpty();
    }

}
