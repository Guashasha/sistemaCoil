package Logica.Dominio;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Academico extends Persona {
    private String cedulaProfesional;
    private String numeroPersonal;
    private String areaEstudios;
    private String correoElectronico;
    private String numeroTelefonico;
    private String categoriaContratacion;
    private Integer idFacultad;

    public Academico () {
        super();
    }

    public Academico (int idPersona, String nombre, String apellidoPaterno, String apellidoMaterno, int idUniversidad, String cedulaProfesional, String numeroPersonal, String areaEstudios, String correoElectronico, String numeroTelefonico, String categoriaContratacion, Integer idFacultad) {
        super(idPersona, nombre, apellidoPaterno, apellidoMaterno, idUniversidad);
        this.cedulaProfesional = cedulaProfesional;
        this.numeroPersonal = numeroPersonal;
        this.areaEstudios = areaEstudios;
        this.correoElectronico = correoElectronico;
        this.numeroTelefonico = numeroTelefonico;
        this.categoriaContratacion = categoriaContratacion;
        this.idFacultad = idFacultad;
    }

    public String getCedulaProfesional () {
        return cedulaProfesional;
    }

    public void setCedulaProfesional (String cedulaProfesional) {
        checharCedula(cedulaProfesional);
        this.cedulaProfesional = cedulaProfesional;
    }

    public String getNumeroPersonal () {
        return numeroPersonal;
    }

    public void setNumeroPersonal (String numeroPersonal) {
        checharNumeroPersonal(numeroPersonal);
        this.numeroPersonal = numeroPersonal;
    }

    public String getAreaEstudios () {
        return areaEstudios;
    }

    public void setAreaEstudios (String areaEstudios) {
        checarArea(areaEstudios);
        this.areaEstudios = areaEstudios;
    }

    public String getCorreoElectronico () {
        return correoElectronico;
    }

    public void setCorreoElectronico (String correoElectronico) {
        checharCorreo(correoElectronico);
        this.correoElectronico = correoElectronico;
    }

    public String getNumeroTelefonico () {
        return numeroTelefonico;
    }

    public void setNumeroTelefonico (String numeroTelefonico) {
        checarNumeroTelefono(numeroTelefonico);
        this.numeroTelefonico = numeroTelefonico;
    }

    public String getCategoriaContratacion () {
        return categoriaContratacion;
    }

    public void setCategoriaContratacion (String categoriaContratacion) {
        checharCategoria(categoriaContratacion);
        this.categoriaContratacion = categoriaContratacion;
    }

    public int getIdFacultad () {
        return idFacultad;
    }

    public void setIdFacultad (Integer idFacultad) {
        this.idFacultad = idFacultad;
    }

    @Override
    public boolean validarNulos () {
        return cadenaValida(getNombre()) &&
                cadenaValida(getApellidoPaterno()) &&
                cadenaValida(getApellidoMaterno()) &&
                cadenaValida(cedulaProfesional) &&
                cadenaValida(numeroPersonal) &&
                cadenaValida(areaEstudios) &&
                cadenaValida(correoElectronico) &&
                cadenaValida(numeroTelefonico);
    }

    private void checharCedula (String cedulaProfesional) {
        String CEDULA_REGEX = "^(?!0)[1-9]\\\\d{0,30}$";
        Pattern patron = Pattern.compile(CEDULA_REGEX);
        Matcher matcher = patron.matcher(cedulaProfesional);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("""
                                                       La cedula profesional no es válida
                                                       1. Solo debe tener números
                                                       2. Su longitud debe ser máximo 30 caracteres""");
        }
    }

    private void checharCorreo (String correoElectronico) {
        String CORREO_REGEX = "^(?=.{1,30}$)[A-Za-z0-9./+-]+@[A-Za-z]+\\\\.[A-Za-z]{1,3}$";
        Pattern patron = Pattern.compile(CORREO_REGEX);
        Matcher matcher = patron.matcher(correoElectronico);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("El correo electrónico no es válido\n" +
                                                       "1.No debe tener espacios en blanco");
        }
    }

    private void checharNumeroPersonal (String numeroPersonal) {
        String NUMERO_P_REGEX = "(?!0)[1-9]\\\\d{0,39}$";
        Pattern patron = Pattern.compile(NUMERO_P_REGEX);
        Matcher matcher = patron.matcher(numeroPersonal);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("""
                                                       El número de personal no es válido
                                                       1.No debe tener espacios en blanco
                                                       2. Solo debe contener números y estos deben ser positivos
                                                       3. No debe ser un número mayor a 40 digitos""");
        }
    }

    private void checharCategoria (String categoriaContratacion) {
        String CATEGORIA_REGEX = "^[a-zA-Z][a-zA-Z\\\\s]{0,39}$";
        Pattern patron = Pattern.compile(CATEGORIA_REGEX);
        Matcher matcher = patron.matcher(categoriaContratacion);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("""
                                                       La categoria de contración no es válida
                                                       1. Solo debe contener letras
                                                       2. No debe ser mayor a 40 caracteres""");
        }
    }

    private void checarArea (String areaEstudios) {
        String AREA_REGEX = "^[a-zA-Z][a-zA-Z\\\\s]{0,39}$";
        Pattern patron = Pattern.compile(AREA_REGEX);
        Matcher matcher = patron.matcher(areaEstudios);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("""
                                                       El area de estudios no es válida
                                                       1. Solo debe contener letras
                                                       2. No debe ser mayor a 40 caracteres""");
        }
    }

    private void checarNumeroTelefono (String numeroTelefonico) {
        String NUMERO_TELEFONO_REGEX = "^(?!0)[1-9]\\d{11}$";
        Pattern patron = Pattern.compile(NUMERO_TELEFONO_REGEX);
        Matcher matcher = patron.matcher(numeroTelefonico);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("""
                                                       El numero telefónico no es válido
                                                       1. Debe ser de 12 dígitos
                                                       2. Debe contener la lada al inicio:
                                                       +xxzzccvvbbnn""");
        }
    }

    public boolean esLongitudValidad () {
        return cedulaProfesional.length() <= 30 &&
                numeroPersonal.length() <= 40 &&
                areaEstudios.length() <= 40 &&
                correoElectronico.length() <= 30 &&
                numeroPersonal.length() == 12;
        //categoriaContratacion.length() <= 40;
    }

    @Override
    public boolean equals (Object obj) {
        boolean igual;
        if (this == obj) {
            igual = true;
        }
        else if (!(obj instanceof Academico)) {
            igual = false;
        }
        else {
            Academico academico = (Academico) obj;
            igual = this.getIdPersona() == academico.getIdPersona() && this.getNombre()
                                                                           .equals(academico.getNombre())
                    && this.getApellidoPaterno()
                           .equals(academico.getApellidoPaterno()) && this.getApellidoMaterno()
                                                                          .equals(academico.getApellidoMaterno())
                    && this.getIdUniversidad() == academico.getIdUniversidad() && this.cedulaProfesional.equals(academico.getCedulaProfesional())
                    && this.numeroPersonal.equals(academico.getNumeroPersonal()) && this.areaEstudios.equals(academico.getAreaEstudios())
                    && this.correoElectronico.equals(academico.getCorreoElectronico()) && this.numeroTelefonico.equals(academico.getNumeroTelefonico())
                    && this.categoriaContratacion.equals(academico.getCategoriaContratacion()) && this.idFacultad == academico.getIdFacultad();
        }
        return igual;
    }
}
