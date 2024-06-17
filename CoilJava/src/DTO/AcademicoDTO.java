package DTO;

import Utilidades.ErrorDAO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase que representa a un académico, extendiendo de PersonaDTO.
 */
public class AcademicoDTO extends PersonaDTO {
    private String cedulaProfesional;
    private String numeroPersonal;
    private String areaEstudios;
    private String correoElectronico;
    private String numeroTelefonico;
    private String categoriaContratacion;
    private Integer idFacultad;

    public AcademicoDTO () {
        super();
    }

    /**
     * Constructor que inicializa todos los campos del académico.
     *
     * @param idPersona             el ID de la persona.
     * @param nombre                el nombre del académico.
     * @param apellidos             el apellido paterno del académico.
     * @param idUniversidad         el ID de la universidad.
     * @param cedulaProfesional     la cédula profesional del académico.
     * @param numeroPersonal        el número de personal del académico.
     * @param areaEstudios          el área de estudios del académico.
     * @param correoElectronico     el correo electrónico del académico.
     * @param numeroTelefonico      el número telefónico del académico.
     * @param categoriaContratacion la categoría de contratación del académico.
     * @param idFacultad            el ID de la facultad.
     */
    public AcademicoDTO (int idPersona, String nombre, String apellidos, int idUniversidad, String cedulaProfesional, String numeroPersonal, String areaEstudios, String correoElectronico, String numeroTelefonico, String categoriaContratacion, Integer idFacultad) {
        super(idPersona, nombre, apellidos, idUniversidad);
        this.cedulaProfesional = cedulaProfesional;
        this.numeroPersonal = numeroPersonal;
        this.areaEstudios = areaEstudios;
        this.correoElectronico = correoElectronico;
        this.numeroTelefonico = numeroTelefonico;
        this.categoriaContratacion = categoriaContratacion;
        this.idFacultad = idFacultad;
    }

    /**
     * Obtiene la cédula profesional del académico.
     *
     * @return la cédula profesional.
     */
    public String getCedulaProfesional () {
        return cedulaProfesional;
    }

    /**
     * Establece la cédula profesional del académico.
     *
     * @param cedulaProfesional la cédula profesional.
     */
    public void setCedulaProfesional (String cedulaProfesional) {
        verificarCedula(cedulaProfesional);
        this.cedulaProfesional = cedulaProfesional;
    }

    /**
     * Obtiene el número de personal del académico.
     *
     * @return el número de personal.
     */
    public String getNumeroPersonal () {
        return numeroPersonal;
    }

    /**
     * Obtiene el área de estudios del académico.
     *
     * @return el área de estudios.
     */
    public void setNumeroPersonal (String numeroPersonal) {
        verificarNumeroPersonal(numeroPersonal);
        this.numeroPersonal = numeroPersonal;
    }

    public String getAreaEstudios () {
        return areaEstudios;
    }

    public void setAreaEstudios (String areaEstudios) {
        this.areaEstudios = areaEstudios;
    }

    public String getCorreoElectronico () {
        return correoElectronico;
    }

    public void setCorreoElectronico (String correoElectronico) {
        verificarCorreo(correoElectronico);
        this.correoElectronico = correoElectronico;
    }

    public String getNumeroTelefonico () {
        return numeroTelefonico;
    }


    public void setNumeroTelefonico (String numeroTelefonico) {
        verificarNumeroTelefonico(numeroTelefonico);
        this.numeroTelefonico = numeroTelefonico;
    }

    public String getCategoriaContratacion () {
        return categoriaContratacion;
    }

    public void setCategoriaContratacion (String categoriaContratacion) {
        verificarCategoriaContratacion(categoriaContratacion);
        this.categoriaContratacion = categoriaContratacion;
    }

    public Integer getIdFacultad () {
        return idFacultad;
    }

    public void setIdFacultad (Integer idFacultad) {
        this.idFacultad = idFacultad;
    }

    @Override
    public boolean validarNulos () {
        return esCadenaValida(getNombre()) &&
                esCadenaValida(getApellidos()) &&
                esCadenaValida(cedulaProfesional) &&
                esCadenaValida(numeroPersonal) &&
                esCadenaValida(areaEstudios) &&
                esCadenaValida(correoElectronico) &&
                esCadenaValida(numeroTelefonico);
    }

    /**
     * Verifica la validez de la cédula profesional.
     *
     * @param cedulaProfesional la cédula profesional.
     * @throws ErrorDAO si la cédula no es válida.
     */
    private void verificarCedula (String cedulaProfesional) {
        String CEDULA_REGEX = "^[0-9]{1,30}$";
        Pattern patron = Pattern.compile(CEDULA_REGEX);
        if (cedulaProfesional == null || cedulaProfesional.isEmpty()) {
            throw new ErrorDAO("La cédula profesional no puede estar vacía", ErrorDAO.Tipo.VALIDACION);
        }
        Matcher matcher = patron.matcher(cedulaProfesional);
        if (!matcher.matches()) {
            throw new ErrorDAO("La cédula profesional no es válida. Debe contener solo números y tener como máximo 30 caracteres.", ErrorDAO.Tipo.VALIDACION);
        }
    }

    /**
     * Verifica la validez del correo electrónico.
     *
     * @param correoElectronico el correo electrónico.
     * @throws ErrorDAO si el correo no es válido.
     */
    private void verificarCorreo (String correoElectronico) {
        String CORREO_REGEX = "[A-z0-9./+-]+@[A-z]+\\.[A-z]{1,3}";
        Pattern patron = Pattern.compile(CORREO_REGEX);
        if (correoElectronico == null || correoElectronico.isEmpty()) {
            throw new ErrorDAO("El correo electrónico no puede estar vacío", ErrorDAO.Tipo.VALIDACION);
        }
        Matcher matcher = patron.matcher(correoElectronico);
        if (!matcher.matches()) {
            throw new ErrorDAO("El correo electrónico no es válido\n" +
                                       "1.No debe tener espacios en blanco\n"
                                       + "Ejemplo:\n correoEjemplo@dominio.com", ErrorDAO.Tipo.VALIDACION);
        }
    }

    /**
     * Verifica la validez del número de personal.
     *
     * @param numeroPersonal el número de personal.
     * @throws ErrorDAO si el número no es válido.
     */
    private void verificarNumeroPersonal (String numeroPersonal) {
        String NUMERO_P_REGEX = "^[1-9][0-9]{0,39}$";
        Pattern patron = Pattern.compile(NUMERO_P_REGEX);
        if (numeroPersonal != null) {
            Matcher matcher = patron.matcher(numeroPersonal);
            if (!matcher.find()) {
                throw new ErrorDAO("""
                                           El número de personal no es válido
                                           1. No debe tener espacios en blanco
                                           2. Solo debe contener números y estos deben ser positivos
                                           3. No debe ser un número mayor a 40 digitos
                                           4. No debe empzar con 0""", ErrorDAO.Tipo.VALIDACION);
            }
        }
    }

    /**
     * Verifica la validez de la categoría de contratación.
     *
     * @param categoriaContratacion la categoría de contratación.
     * @throws ErrorDAO si la categoría no es válida.
     */
    private void verificarCategoriaContratacion (String categoriaContratacion) {
        String categoriaRex = "^.{1,40}";
        Pattern patron = Pattern.compile(categoriaRex);
        if (categoriaContratacion != null && !categoriaContratacion.isEmpty()) {
            Matcher matcher = patron.matcher(categoriaContratacion);
            if (!matcher.matches()) {
                throw new ErrorDAO("""
                                           La categoria de contratación no es válida
                                           1. Solo debe contener letras
                                           2. No debe ser mayor a 40 caracteres""", ErrorDAO.Tipo.VALIDACION);
            }
        }
    }

    /**
     * Verifica la validez del número telefónico.
     *
     * @param numeroTelefonico el número telefónico.
     * @throws ErrorDAO si el número no es válido.
     */
    private void verificarNumeroTelefonico (String numeroTelefonico) {
        String NUMERO_TELEFONO_REGEX = "^(?!0)[1-9]\\d{11,13}$";
        Pattern patron = Pattern.compile(NUMERO_TELEFONO_REGEX);
        if (numeroTelefonico != null) {
            Matcher matcher = patron.matcher(numeroTelefonico);
            if (!matcher.matches()) {
                throw new ErrorDAO("""
                                           El numero telefónico no es válido:
                                           1. Su longitud debe ser de 11 a 13 números, sin espacios ni guiones.
                                           2. Debe contener la lada de su país al inicio.
                                           Ejemplo de número telefónico de México: 522283412789.""", ErrorDAO.Tipo.VALIDACION);
            }
        }
    }

    /**
     * Verifica si dos objetos AcademicoDTO son iguales.
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
        else if (!(obj instanceof AcademicoDTO)) {
            igual = false;
        }
        else {
            AcademicoDTO academico = (AcademicoDTO) obj;
            igual = this.getIdPersona() == academico.getIdPersona() && this.getNombre()
                                                                           .equals(academico.getNombre())
                    && this.getApellidos()
                           .equals(academico.getApellidos())
                    && this.getIdUniversidad() == academico.getIdUniversidad() && this.cedulaProfesional.equals(academico.getCedulaProfesional())
                    && this.numeroPersonal.equals(academico.getNumeroPersonal()) && this.areaEstudios.equals(academico.getAreaEstudios())
                    && this.correoElectronico.equals(academico.getCorreoElectronico()) && this.numeroTelefonico.equals(academico.getNumeroTelefonico())
                    && this.categoriaContratacion.equals(academico.getCategoriaContratacion()) && this.idFacultad == academico.getIdFacultad();
        }
        return igual;
    }
}