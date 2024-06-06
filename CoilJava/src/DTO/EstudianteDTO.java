package DTO;

import Utilidades.ErrorDAO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EstudianteDTO extends PersonaDTO {
    private int idEstudiante;
    private String matricula;

    public EstudianteDTO() {
        super();
    }
    public EstudianteDTO(int idPersona, String nombre, String apellidoPaterno, String apellidoMaterno, int idUniversidad, int idEstudiante, String matricula) {
        super(idPersona, nombre, apellidoPaterno, apellidoMaterno, idUniversidad);
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
        return cadenaValida(getNombre()) && cadenaValida(getApellidoPaterno()) &&
                cadenaValida(getApellidoMaterno()) && cadenaValida(getMatricula());
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
                    && this.getApellidoPaterno().equals(estudianteDTO.getApellidoPaterno())
                    && this.getApellidoMaterno().equals(estudianteDTO.getApellidoMaterno())
                    && this.getIdUniversidad() == estudianteDTO.getIdUniversidad()
                    && this.idEstudiante == estudianteDTO.getIdEstudiante()
                    && this.matricula.equals(estudianteDTO.getMatricula());
        }
        return igual;
    }
}
