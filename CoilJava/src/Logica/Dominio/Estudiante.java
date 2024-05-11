package Logica.Dominio;

import Utilidades.ErrorDAO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Estudiante extends Persona {
    private int idEstudiante;
    private String matricula;

    public Estudiante () {
        super();
    }
    public Estudiante(int idPersona, String nombre, String apellidoPaterno, String apellidoMaterno, int idUniversidad, int idEstudiante, String matricula) {
        super(idPersona, nombre, apellidoPaterno, apellidoMaterno, idUniversidad);
        this.idEstudiante = idEstudiante;
        this.matricula = matricula;
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
        String matriculaRegex = "^[A-Za-z0-9]{10}$";
        Pattern patron = Pattern.compile(matriculaRegex);
        if (matricula == null || matricula.isEmpty()) {
            throw new ErrorDAO("La matricula no puede estar vacía", ErrorDAO.Tipo.VALIDACION);
        }
        Matcher matcher = patron.matcher(matricula);
        if (!matcher.matches()) {
            throw new ErrorDAO("""
                                                       La matrícula no es valida.
                                                       1. Su longitud debe ser exactamente de 10 caracteres.
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
        else if (!(obj instanceof Estudiante)) {
            igual = false;
        }
        else {
            Estudiante estudiante = (Estudiante) obj;
            igual = this.getIdPersona() == estudiante.getIdPersona() && this.getNombre()
                                                                            .equals(estudiante.getNombre()) &&
                    this.getApellidoPaterno()
                        .equals(estudiante.getApellidoPaterno()) && this.getApellidoMaterno()
                                                                        .equals(estudiante.getApellidoMaterno()) &&
                    this.getIdUniversidad() == estudiante.getIdUniversidad() && this.idEstudiante == estudiante.getIdEstudiante() &&
                    this.matricula.equals(estudiante.getMatricula());
        }
        return igual;
    }

}
