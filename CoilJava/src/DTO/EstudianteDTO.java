package DTO;

import Utilidades.ErrorDAO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * La clase EstudianteDTO funciona como transfer object, para transferir la información desde la base de datos a capas superiores dentro de la aplicación.
 */
public class EstudianteDTO extends PersonaDTO {
    private int idEstudiante;
    private String matricula;

    public EstudianteDTO() {
        super();
    }
    public EstudianteDTO(int idPersona, String nombre, String apellidos, int idUniversidad, int idEstudiante, String matricula) {
        super(idPersona, nombre, apellidos, idUniversidad);
        this.idEstudiante = idEstudiante;
        setMatricula(matricula);
    }

    public int getIdEstudiante () {
        return idEstudiante;
    }

    public void setIdEstudiante (int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getMatricula () {
        return matricula;
    }

    public void setMatricula (String matricula) {
        checarMatricula(matricula);
        this.matricula = matricula;
    }

    /**
     * Verifica la validez de la matrícula.
     *
     * @param matricula la matrícula.
     * @throws ErrorDAO si la matrícula no es válida.
     */
    private void checarMatricula (String matricula) {
        String matriculaRegex = "^[A-Za-z0-9]{8,10}$";
        Pattern patron = Pattern.compile(matriculaRegex);
        if (matricula == null || matricula.isEmpty()) {
            throw new ErrorDAO("La matricula no puede estar vacía", ErrorDAO.Tipo.VALIDACION);
        }
        Matcher matcher = patron.matcher(matricula);
        if (!matcher.matches()) {
            throw new ErrorDAO("""
                                                       La matrícula no es valida.
                                                       1. Su longitud debe ser entre 8 y 10 caracteres.
                                                       2. No debe tener espacios.""", ErrorDAO.Tipo.VALIDACION);
        }
    }

    @Override
    public boolean validarNulos() {
        return esCadenaValida(getNombre()) && esCadenaValida(getApellidos()) && esCadenaValida(getMatricula());
    }

    @Override
    public boolean equals (Object obj) {
        boolean igual;
        if (this == obj) {
            igual = true;
        }
        else if (!(obj instanceof EstudianteDTO)) {
            igual = false;
        }
        else {
            EstudianteDTO estudianteDTO = (EstudianteDTO) obj;
            igual = this.getIdPersona() == estudianteDTO.getIdPersona()
                    && this.getNombre().equals(estudianteDTO.getNombre())
                    && this.getApellidos().equals(estudianteDTO.getApellidos())
                    && this.getIdUniversidad() == estudianteDTO.getIdUniversidad()
                    && this.idEstudiante == estudianteDTO.getIdEstudiante()
                    && this.matricula.equals(estudianteDTO.getMatricula());
        }
        return igual;
    }
}
