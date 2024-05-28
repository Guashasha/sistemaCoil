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
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
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
        checarNombre(nombre);
        this.nombre = nombre;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        checarApellido(apellidoPaterno);
        this.apellidoPaterno = apellidoPaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        checarApellido(apellidoMaterno);
        this.apellidoMaterno = apellidoMaterno;
    }

    public int getIdUniversidad () {
        return idUniversidad;
    }

    public void setIdUniversidad (int idUniversidad) {
        this.idUniversidad = idUniversidad;
    }

    private void checarNombre (String nombre) {
        String nombreRegex = "^[A-Za-záéíóúÁÉÍÓÚñÑ][A-Za-záéíóúÁÉÍÓÚñÑ\\\\s]{0,48}[A-Za-záéíóúÁÉÍÓÚñÑ]$";
        Pattern patron = Pattern.compile(nombreRegex);
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorDAO("El nombre no puede estar vacío", ErrorDAO.Tipo.VALIDACION);
        }
        Matcher matcher = patron.matcher(nombre);
        if (!matcher.matches()) {
            throw new ErrorDAO("""
                                                       El nombre no es válido
                                                       1. Solo debe tener letras.
                                                       2. No debe tener espacios.
                                                       3. Para esta versión, solo puede tener caracteres permitidos en el español.
                                                       4. Su longitud debe ser máximo 20 caracteres.""", ErrorDAO.Tipo.VALIDACION);
        }
    }
    private void checarApellido (String apellido) {
        String apellidosRegex = "^[A-Za-záéíóúÁÉÍÓÚñÑ][A-Za-záéíóúÁÉÍÓÚñÑ\\s]*[A-Za-záéíóúÁÉÍÓÚñÑ]$";
        Pattern patron = Pattern.compile(apellidosRegex);
        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorDAO("Los apellidos no pueden estar vacíos", ErrorDAO.Tipo.VALIDACION);
        }
        Matcher matcher = patron.matcher(apellido);
        if (!matcher.matches()) {
            throw new ErrorDAO("""
                                                       El apellido no es válido
                                                       1. Solo debe tener letras.
                                                       2. Para esta versión, solo puede tener caracteres permitodos en el español
                                                       2. Su longitud debe ser máximo 20 caracteres""", ErrorDAO.Tipo.VALIDACION);
        }
    }

    public abstract boolean validarNulos();

    protected boolean cadenaValida(String cadena) {
        return cadena != null && !cadena.isBlank();
    }

}
