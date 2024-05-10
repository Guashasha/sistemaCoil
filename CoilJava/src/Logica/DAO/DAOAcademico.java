package Logica.DAO;

import AccesoADatos.AcademicoDB;
import Logica.Dominio.Academico;
import Utilidades.ErrorDAO;
import Logica.Interfaces.IAcademicoDAO;
import javafx.beans.property.StringProperty;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DAOAcademico implements IAcademicoDAO {
    //fixme reducir redundancia;

    private static final Logger BITACORA = Logger.getLogger(DAOAcademico.class);

    @Override
    public List<Academico> getAcademicosPorFacultad (String nombrefacultad) throws ErrorDAO {
        List<Academico>  listaAcademicos= null;
        if (!cadenaValida(nombrefacultad)) {
            throw new ErrorDAO ("El nombre de la facultad es incorrecto", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            listaAcademicos = AcademicoDB.getListaAcademicoPorCampos("facultad",nombrefacultad);

        }
        catch (ErrorDAO error) {
            BITACORA.error(error);
            throw new ErrorDAO("Error en la base de datos", ErrorDAO.Tipo.CONEXION);
        }

        return listaAcademicos;
    }

    @Override
    public Optional<Academico> getAcademicoPorCedula (String cedula) throws ErrorDAO {
        Academico academico = null;
        if (!cadenaValida(cedula)) {
            throw new ErrorDAO ("La cedula profesional esta incorrecta", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            academico = AcademicoDB.getAcademicoPorCedula(cedula);

        }
        catch (ErrorDAO error) {
            BITACORA.fatal(error);
            throw new ErrorDAO("Error en la base de datos", ErrorDAO.Tipo.CONEXION);
        }

        return Optional.ofNullable(academico);
    }

    @Override
    public List<Academico> getAcademicosPorUniversidad (String nombreUniversidad) throws ErrorDAO {
        List<Academico> listaAcademicos = null;
        if (!cadenaValida(nombreUniversidad)) {
            throw new ErrorDAO ("El nombre de la univesidad esta incorrecto", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            listaAcademicos = AcademicoDB.getListaAcademicoPorCampos("universidad", nombreUniversidad);
        }
        catch (ErrorDAO error) {
            BITACORA.fatal(error);
            throw new ErrorDAO("Error en la base de datos", ErrorDAO.Tipo.CONEXION);
        }

        return listaAcademicos;
    }

    @Override
    public List<Academico> getAcademicosPorAreaEstudios (String areaEstudios) throws ErrorDAO {
        List<Academico> listaAcademicos = null;
        if (!cadenaValida(areaEstudios)) {
            throw new ErrorDAO ("El nombre del area de estudios es incorrecto", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            listaAcademicos = AcademicoDB.getListaAcademicoPorCampos("area", areaEstudios);
        }
        catch (ErrorDAO error) {
            BITACORA.fatal(error);
            throw new ErrorDAO("Error en la base de datos", ErrorDAO.Tipo.CONEXION);
        }
        return listaAcademicos;
    }



    @Override
    public List<Academico> getAcademicosPorCategoriaContratacion (String categoriaContratacion) throws ErrorDAO {
        List<Academico> listaAcademicos = null;
        if (!cadenaValida(categoriaContratacion)) {
            throw new ErrorDAO ("El nombre de la categoria de contratacion es incorrecta", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            listaAcademicos = AcademicoDB.getListaAcademicoPorCampos("categoria", categoriaContratacion);
        }
        catch (ErrorDAO error) {
            BITACORA.fatal(error);
            throw new ErrorDAO("Error en la base de datos", ErrorDAO.Tipo.CONEXION);
        }

        return listaAcademicos;
    }

    @Override
    public List<Academico> getAcademicosPorRegion (String region) throws ErrorDAO {
        List<Academico> listaAcademicos = null;
        if (!cadenaValida(region)) {
            throw new ErrorDAO ("El nombre de la region es incorrecto", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            listaAcademicos = AcademicoDB.getListaAcademicoPorCampos("region", region);
        }
        catch (ErrorDAO error) {
            BITACORA.error(error);
            throw new ErrorDAO("Error en la base de datos", ErrorDAO.Tipo.CONEXION);
        }

        return listaAcademicos;
    }

    @Override
    public Optional<Academico> getAcademicoPorIdPersona (int idPersona) throws ErrorDAO {
        Academico academico = null;
        if (!idValido(idPersona)) {
            throw new ErrorDAO ("id de la persona invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            academico = AcademicoDB.getAcademicoPorId(idPersona);
        }
        catch (ErrorDAO error) {
            BITACORA.error(error);
            throw new ErrorDAO("Error en la base de datos", ErrorDAO.Tipo.CONEXION);
        }

        return Optional.ofNullable(academico);
    }

    @Override
    public int agregarAcademicoExterno (Academico academico) throws ErrorDAO {
        int filasAfectadas = -1;

        if (!academico.validarNulos()) {
            throw new ErrorDAO ("Existe al menos un campo vacio en el academico", ErrorDAO.Tipo.VALIDACION);
        }
        if (!academico.esLongitudValidad()) {
            throw new ErrorDAO ("El numero telefonico es menor o mayor a 11 caracteres", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            filasAfectadas = AcademicoDB.agregarAcademicoExterno(academico);

        }
        catch (ErrorDAO error) {
            BITACORA.error(error);
            throw new ErrorDAO("Error en la base de datos", ErrorDAO.Tipo.CONEXION);
        }

        return filasAfectadas;
    }

    @Override
    public int agregar (Academico academico) throws ErrorDAO {
        int filasAfectadas = -1;
        if (!academico.esLongitudValidad()) {
            throw new ErrorDAO("Al menos un campo sobrepasa el limite de caracteres establecio", ErrorDAO.Tipo.VALIDACION);
        }
        if (!academico.validarNulos()) {
            throw new ErrorDAO ("Existe al menos un campo vacio en el academico", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            filasAfectadas = AcademicoDB.agregarAcademicoUV(academico);

        }
        catch (ErrorDAO error) {
            BITACORA.error(error);
            throw new ErrorDAO("Error en la base de datos", ErrorDAO.Tipo.CONEXION);
        }

        return filasAfectadas;
    }

    @Override
    public int modificar (Academico academico) throws ErrorDAO {
        int filasAfectadas = -1;

        if (!academico.validarNulos()) {
            throw new ErrorDAO ("Existe al menos un campo vacio en el academico", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            filasAfectadas = AcademicoDB.editarAcademico(academico);

        }
        catch (ErrorDAO error) {
            BITACORA.error(error);
        }

        return filasAfectadas;
    }


    @Override
    public Optional<Academico> getPorId (String y) throws ErrorDAO {
        return Optional.empty();
    }

    @Override
    public List<Academico> getTodos () throws ErrorDAO {
        List<Academico> listaAcademicos = null;
        try {
            listaAcademicos = AcademicoDB.getTodos();

        }
        catch (ErrorDAO error) {
            BITACORA.error(error);
        }

        return listaAcademicos;
    }

    @Override
    public Academico resultSetAObjeto (ResultSet resultados) {
        return null;
    }

    public static boolean cadenaValida (String cadena) {
        return cadena != null && !cadena.isBlank();
    }
    public static boolean esNulo (Object objeto) {
        return Optional.ofNullable(objeto)
                       .isEmpty();
    }

    public static boolean esCadenaValidaProperty(StringProperty cadenaProperty) {
        return cadenaProperty != null && cadenaProperty.get() != null && !cadenaProperty.get().isBlank();
    }
    public static boolean esCorreoValido (StringProperty correoProperty) {
        Pattern pattern = Pattern.compile("[A-z0-9./+-]+@[A-z]+\\.[A-z]{1,3}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(correoProperty.get());
        return matcher.find();
    }
    public static boolean esLongitudNumeroTelefonoValida (StringProperty numeroTelefono) {
        return numeroTelefono.get().length() == 12;
    }

    public static boolean idValido (int id) {
        return id > 0;
    }

}
