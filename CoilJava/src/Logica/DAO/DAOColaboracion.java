package Logica.DAO;

import AccesoADatos.ColaboracionDB;
import Logica.Dominio.Academico;
import Logica.Dominio.Colaboracion;
import Logica.Dominio.Estudiante;
import Logica.Dominio.Periodo;
import Utilidades.ErrorDAO;
import Logica.Interfaces.IColaboracionDAO;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public class DAOColaboracion implements IColaboracionDAO {
    private static final Logger BITACORA = Logger.getLogger(DAOColaboracion.class);

    @Override
    public Optional<Colaboracion> getColaboracionPorAcademicosParticipantes (Academico academico1, Academico academico2) throws ErrorDAO {
        Colaboracion colaboracion = null;
        if (esCadaInvalida(academico1.getCedulaProfesional()) && esCadaInvalida(academico2.getCedulaProfesional())) {
            throw new ErrorDAO("Error en los academicos de la colaboracion", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            colaboracion = ColaboracionDB.getColaboracionPorAcademicosParticipantes(academico1, academico2);
        }
        catch (ErrorDAO error) {
            BITACORA.error(error);
        }

        return Optional.ofNullable(colaboracion);
    }

    @Override
    public Optional<Colaboracion> getColaboracionPorId (int idColaboracion) throws ErrorDAO {
        if (esIdInvalido(idColaboracion)) {
            throw new ErrorDAO("Error en el identificador de la colaboracion", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return Optional.ofNullable(ColaboracionDB.getColaboracionPorId(idColaboracion));
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public List<Estudiante> getListaDeEstudiantes (Colaboracion colaboracion) throws ErrorDAO {
        if (esIdInvalido(colaboracion.getIdColaboracion())) {
            throw new ErrorDAO("Error en el id de la colaboracion", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return ColaboracionDB.getListaDeEstudiantes(colaboracion);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public List<Academico> getAcademicosParticipantes (Colaboracion colaboracion) throws ErrorDAO {
        if (esIdInvalido(colaboracion.getIdColaboracion())) {
            throw new ErrorDAO("Error en el id de la colaboracion", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return ColaboracionDB.getAcademicosParticipantes(colaboracion);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(),error.getTipo());
        }
    }

    @Override
    public List<Colaboracion> getColaboracionPorPeriodo (Periodo periodo) {
        if (!periodo.validarNulo()) {
            throw new ErrorDAO("Periodo con fecha de inicio o de fin vacia", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return ColaboracionDB.getColaboracionPorPeriodo(periodo);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public List<Colaboracion> getColaboracionPorIdioma (String idioma) {
        if (esCadaInvalida(idioma)) {
            throw new ErrorDAO("Idioma invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return ColaboracionDB.getColaboracionPorIdioma(idioma);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public List<Colaboracion> getColaboracionPorEstado (String estado) throws ErrorDAO {
        if (esCadaInvalida(estado)) {
            throw new ErrorDAO("Pais invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return ColaboracionDB.getColaboracionPorEstado(estado);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public int cambiarEstadoColaboracion (Colaboracion colaboracion) {

        if (esIdInvalido(colaboracion.getIdColaboracion())) {
            throw new ErrorDAO("id de la colaboracion invalido", ErrorDAO.Tipo.VALIDACION);
        }
        if (esCadaInvalida(colaboracion.getEstado()
                                       .toString())) {
            throw new ErrorDAO("Error en el estado de la colaboracion", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return ColaboracionDB.cambiarEstadoColaboracion(colaboracion);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public int agregarEstudianteAColaboracion (Colaboracion colaboracion, Estudiante estudiante) throws ErrorDAO {
        if (esIdInvalido(colaboracion.getIdColaboracion())) {
            throw new ErrorDAO("Error en el id de la colaboracion", ErrorDAO.Tipo.VALIDACION);
        }
        if (esIdInvalido(estudiante.getIdEstudiante())) {
            throw new ErrorDAO("Error en el id del estudiante", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return ColaboracionDB.agregarEstudianteAColaboracion(colaboracion, estudiante);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }


    @Override
    public int agregarAcademicoAColaboracion (Colaboracion colaboracion, Academico academico) throws ErrorDAO {
        if (esIdInvalido(colaboracion.getIdColaboracion())) {
            throw new ErrorDAO("Error en el id de la colaboracion", ErrorDAO.Tipo.VALIDACION);
        }
        if (esCadaInvalida(academico.getCedulaProfesional())) {
            throw new ErrorDAO("Error en la cedula del academico", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return ColaboracionDB.agregarAcademicoAColaboracion(colaboracion, academico);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public Optional<Colaboracion> getActivaPorAcademico (Academico academico) throws ErrorDAO {
        return Optional.empty();
    }

    @Override
    public int agregar (Colaboracion colaboracion) throws ErrorDAO {
        if (!colaboracion.esValido()) {
            throw new ErrorDAO("Al menos un dato de la colaboracion esta vacia", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return ColaboracionDB.registrarColaboracion(colaboracion);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public int modificar (Colaboracion colaboracion) throws ErrorDAO {
        if (!colaboracion.esValido()) {
            throw new ErrorDAO("Al menos un dato de la colaboracion esta vacio", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return ColaboracionDB.actualizarColaboracion(colaboracion);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public Optional<Colaboracion> getPorId (Integer id) throws ErrorDAO {
        if (esIdInvalido(id)) {
            throw new ErrorDAO("Id invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return Optional.ofNullable(ColaboracionDB.getPorId(id));
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public List<Colaboracion> getTodos () throws ErrorDAO {
        try {
            return ColaboracionDB.getTodos();
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public Colaboracion resultSetAObjeto (ResultSet resultados) {
        throw new NotImplementedException("No esta implementada esta función");
    }

    private boolean esCadaInvalida (String cadena) {
        return cadena == null || cadena.isBlank();
    }

    private boolean esIdInvalido (int id) {
        return id <= 0;
    }
}
