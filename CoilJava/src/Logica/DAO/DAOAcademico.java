package Logica.DAO;

import AccesoADatos.AcademicoDB;
import Logica.Dominio.Academico;
import Utilidades.ErrorDAO;
import Logica.Interfaces.IAcademicoDAO;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DAOAcademico implements IAcademicoDAO {
    //fixme considerar hacer un metodo generico;

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
        catch (SQLException error) {
            BITACORA.error(error);

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
        catch (SQLException error) {
            BITACORA.fatal(error);

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
        catch (SQLException error) {
            BITACORA.fatal(error);

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
        catch (SQLException error) {
            BITACORA.fatal(error);
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
        catch (SQLException error) {
            BITACORA.fatal(error);
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
        catch (SQLException error) {
            BITACORA.error(error);

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
        catch (SQLException error) {
            BITACORA.error(error);
        }

        return Optional.ofNullable(academico);
    }

    @Override
    public int agregarAcademicoExterno (Academico academico) throws ErrorDAO {
        int filasAfectadas = -1;

        if (!academico.validarNulos()) {
            throw new ErrorDAO ("Existe al menos un campo vacio en el academico", ErrorDAO.Tipo.VALIDACION);
        }
        if (academico.esLongitudValidad()) {
            throw new ErrorDAO ("El numero telefonico es menor o mayor a 11 caracteres", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            filasAfectadas = AcademicoDB.agregarAcademicoExterno(academico);

        }
        catch (SQLException error) {
            BITACORA.error(error);
        }

        return filasAfectadas;
    }

    @Override
    public int agregar (Academico academico) throws ErrorDAO {
        int filasAfectadas = -1;

        if (!academico.validarNulos()) {
            throw new ErrorDAO ("Existe al menos un campo vacio en el academico", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            filasAfectadas = AcademicoDB.agregarAcademicoUV(academico);

        }
        catch (SQLException error) {
            BITACORA.error(error);
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
        catch (SQLException error) {
            BITACORA.error(error);
        }

        return filasAfectadas;
    }


    //fixme nombre nada descriptivo.
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
        catch (SQLException error) {
            BITACORA.error(error);
        }

        return listaAcademicos;
    }

    @Override
    public Academico resultSetAObjeto (ResultSet resultados) {
        return null;
    }

    private boolean cadenaValida (String cadena) {
        return cadena != null && !cadena.isBlank();
    }

    private boolean idValido (int id) {
        return id > 0;
    }

}
