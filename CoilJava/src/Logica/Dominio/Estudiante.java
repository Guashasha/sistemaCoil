package Logica.Dominio;

public class Estudiante extends Persona {
    private int idEstudiante;
    private String matricula;

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
        this.matricula = matricula;
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
