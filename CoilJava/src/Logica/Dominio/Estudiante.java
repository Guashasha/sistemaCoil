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

}
