package DAO;

import DTO.AcademicoDTO;
import DTO.ColaboracionDTO;
import DTO.EstudianteDTO;
import DTO.PeriodoDTO;
import Utilidades.ErrorDAO;
import org.apache.commons.lang3.NotImplementedException;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ColaboracionAuxiliar {
    private final ColaboracionDAO COLABORACION_DAO = new ColaboracionDAO();

    public Optional<ColaboracionDTO> getColaboracionPorId (int idColaboracion) throws ErrorDAO {
        if (esIdInvalido(idColaboracion)) {
            throw new ErrorDAO("Error en el identificador de la colaboracion", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return COLABORACION_DAO.getColaboracionPorId(idColaboracion);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    public List<EstudianteDTO> getListaDeEstudiantes (ColaboracionDTO colaboracionDTO) throws ErrorDAO {
        if (esIdInvalido(colaboracionDTO.getIdColaboracion())) {
            throw new ErrorDAO("Error en el id de la colaboracionDTO", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return COLABORACION_DAO.getListaDeEstudiantes(colaboracionDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    public List<AcademicoDTO> getAcademicosParticipantes (ColaboracionDTO colaboracionDTO) throws ErrorDAO {
        if (esIdInvalido(colaboracionDTO.getIdColaboracion())) {
            throw new ErrorDAO("Error en el id de la colaboracionDTO", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return COLABORACION_DAO.getAcademicosParticipantes(colaboracionDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(),error.getTipo());
        }
    }

    public List<ColaboracionDTO> getColaboracionPorPeriodo (PeriodoDTO periodoDTO) {
        if (!periodoDTO.validarNulo()) {
            throw new ErrorDAO("PeriodoDTO con fecha de inicio o de fin vacia", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return COLABORACION_DAO.getColaboracionPorPeriodo(periodoDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    public List<ColaboracionDTO> getColaboracionPorIdioma (String idioma) {
        if (esCadaInvalida(idioma)) {
            throw new ErrorDAO("Idioma invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return COLABORACION_DAO.getColaboracionPorIdioma(idioma);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    public List<ColaboracionDTO> getColaboracionPorEstado (String estado) throws ErrorDAO {
        if (esCadaInvalida(estado)) {
            throw new ErrorDAO("PaisDTO invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return COLABORACION_DAO.getColaboracionPorEstado(estado);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    public int cambiarEstadoColaboracion (ColaboracionDTO colaboracionDTO) {

        if (esIdInvalido(colaboracionDTO.getIdColaboracion())) {
            throw new ErrorDAO("id de la colaboracionDTO invalido", ErrorDAO.Tipo.VALIDACION);
        }
        if (esCadaInvalida(colaboracionDTO.getEstado()
                                       .toString())) {
            throw new ErrorDAO("Error en el estado de la colaboracionDTO", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return COLABORACION_DAO.cambiarEstadoColaboracion(colaboracionDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    public int agregarEstudianteAColaboracion (ColaboracionDTO colaboracionDTO, EstudianteDTO estudianteDTO) throws ErrorDAO {
        if (esIdInvalido(colaboracionDTO.getIdColaboracion())) {
            throw new ErrorDAO("Error en el id de la colaboracion", ErrorDAO.Tipo.VALIDACION);
        }
        if (esIdInvalido(estudianteDTO.getIdEstudiante())) {
            throw new ErrorDAO("Error en el id del estudiante", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return COLABORACION_DAO.agregarEstudianteAColaboracion(colaboracionDTO, estudianteDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    public int registrarSolicitudParticipacion (ColaboracionDTO colaboracionDTO, AcademicoDTO academicoDTO) throws ErrorDAO {
        if (COLABORACION_DAO.existeUnaSolicitudPrevia(colaboracionDTO, academicoDTO)) {
            throw new ErrorDAO("Ya has solicitado participar en esta colaboración", ErrorDAO.Tipo.VALIDACION);
        }
        if (esIdInvalido(colaboracionDTO.getIdColaboracion())) {
            throw new ErrorDAO("Error en el id de la colaboracionDTO", ErrorDAO.Tipo.VALIDACION);
        }
        if (esCadaInvalida(academicoDTO.getCedulaProfesional())) {
            throw new ErrorDAO("Error en la cedula del academicoDTO", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return COLABORACION_DAO.registrarSolicitudParticipacion(colaboracionDTO, academicoDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }
    public int registrarPropuestaColaboracion (ColaboracionDTO colaboracionDTO, AcademicoDTO academicoDTO) throws ErrorDAO {
        try {
            return COLABORACION_DAO.registrarPropuestaColaboracion(colaboracionDTO, academicoDTO);
        }
        catch (ErrorDAO errorDAO) {
            throw new ErrorDAO(errorDAO.getMessage(), errorDAO.getTipo());
        }
    }

    public List<ColaboracionDTO> obtenerPropuestasColaboracion () throws ErrorDAO {
        try {
            return COLABORACION_DAO.obtenerPropuestasColaboracion();
        }
        catch (ErrorDAO errorDAO) {
            throw new ErrorDAO(errorDAO.getMessage(), errorDAO.getTipo());
        }
    }

    public List<AcademicoDTO> obtenerSolicitudAcademicoColaboracion (int id) throws ErrorDAO {
        try {
            return COLABORACION_DAO.obtenerSolicitudAcademicoColaboracion(id);
        }
        catch (ErrorDAO errorDAO) {
            throw new ErrorDAO(errorDAO.getMessage(),errorDAO.getTipo());
        }
    }
    public List<ColaboracionDTO> obtenerColaboracionDisponible (String cedulaProfesional) throws ErrorDAO {
        try {
            return COLABORACION_DAO.obtenerColaboracionDisponible(cedulaProfesional);
        }
        catch (ErrorDAO errorDAO) {
            throw errorDAO;
        }
    }

    public Optional<ColaboracionDTO> getActivaPorAcademico (AcademicoDTO academicoDTO) throws ErrorDAO {
        try {
            return COLABORACION_DAO.getActivaPorAcademico(academicoDTO);
        }
        catch (ErrorDAO errorDAO) {
            throw errorDAO;
        }
    }

    public int agregar (ColaboracionDTO colaboracionDTO) throws ErrorDAO {
        if (!colaboracionDTO.esValido()) {
            throw new ErrorDAO("Al menos un dato de la colaboracionDTO esta vacia", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return COLABORACION_DAO.agregar(colaboracionDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    public int modificar (ColaboracionDTO colaboracionDTO) throws ErrorDAO {
        if (!colaboracionDTO.esValido()) {
            throw new ErrorDAO("Al menos un dato de la colaboracionDTO esta vacio", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return COLABORACION_DAO.modificar(colaboracionDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    public Optional<ColaboracionDTO> getPorId (Integer id) throws ErrorDAO {
        if (esIdInvalido(id)) {
            throw new ErrorDAO("Id invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return COLABORACION_DAO.getPorId(id);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    public List<ColaboracionDTO> getTodos () throws ErrorDAO {
        try {
            return COLABORACION_DAO.getTodos();
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    public ColaboracionDTO resultSetAObjeto (ResultSet resultados) {
        throw new NotImplementedException("No esta implementada esta función");
    }

    public Map<String,int[]> getNumeraliaRegion (PeriodoDTO periodo) throws ErrorDAO {
        Map<String,int[]> numeralia;

        if (periodo.validarNulo()) {
            numeralia = COLABORACION_DAO.getNumeraliaRegion(periodo);
        }
        else {
            throw new ErrorDAO("Ocurrió un error. Inténtelo de nuevo más tarde", ErrorDAO.Tipo.VALIDACION);
        }
        return numeralia;
    }

    public Map<String,int[]> getNumeraliaAreaAcademica (PeriodoDTO periodo) throws ErrorDAO {
        Map<String,int[]> numeralia;

        if (periodo.validarNulo()) {
            numeralia = COLABORACION_DAO.getNumeraliaAreaAcademica(periodo);
        }
        else {
            throw new ErrorDAO("Ocurrió un error. Inténtelo de nuevo más tarde", ErrorDAO.Tipo.VALIDACION);
        }
        return numeralia;
    }

    private boolean esCadaInvalida (String cadena) {
        return cadena == null || cadena.isBlank();
    }

    private boolean esIdInvalido (int id) {
        return id <= 0;
    }
}
