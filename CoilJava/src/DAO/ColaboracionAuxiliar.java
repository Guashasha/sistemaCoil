package DAO;

import DTO.AcademicoDTO;
import DTO.ColaboracionDTO;
import DTO.EstudianteDTO;
import DTO.PeriodoDTO;
import Utilidades.ErrorDAO;
import DAO.Interfaces.IColaboracionDAO;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public class ColaboracionAuxiliar implements IColaboracionDAO {
    private static final Logger BITACORA = Logger.getLogger(ColaboracionAuxiliar.class);

    @Override
    public Optional<ColaboracionDTO> getColaboracionPorAcademicosParticipantes (AcademicoDTO academicoDTO1, AcademicoDTO academicoDTO2) throws ErrorDAO {
        ColaboracionDTO colaboracionDTO = null;
        if (esCadaInvalida(academicoDTO1.getCedulaProfesional()) && esCadaInvalida(academicoDTO2.getCedulaProfesional())) {
            throw new ErrorDAO("Error en los academicos de la colaboracionDTO", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            colaboracionDTO = ColaboracionDAO.getColaboracionPorAcademicosParticipantes(academicoDTO1, academicoDTO2);
        }
        catch (ErrorDAO error) {
            BITACORA.error(error);
        }

        return Optional.ofNullable(colaboracionDTO);
    }

    @Override
    public Optional<ColaboracionDTO> getColaboracionPorId (int idColaboracion) throws ErrorDAO {
        if (esIdInvalido(idColaboracion)) {
            throw new ErrorDAO("Error en el identificador de la colaboracion", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return Optional.ofNullable(ColaboracionDAO.getColaboracionPorId(idColaboracion));
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public List<EstudianteDTO> getListaDeEstudiantes (ColaboracionDTO colaboracionDTO) throws ErrorDAO {
        if (esIdInvalido(colaboracionDTO.getIdColaboracion())) {
            throw new ErrorDAO("Error en el id de la colaboracionDTO", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return ColaboracionDAO.getListaDeEstudiantes(colaboracionDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public List<AcademicoDTO> getAcademicosParticipantes (ColaboracionDTO colaboracionDTO) throws ErrorDAO {
        if (esIdInvalido(colaboracionDTO.getIdColaboracion())) {
            throw new ErrorDAO("Error en el id de la colaboracionDTO", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return ColaboracionDAO.getAcademicosParticipantes(colaboracionDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(),error.getTipo());
        }
    }

    @Override
    public List<ColaboracionDTO> getColaboracionPorPeriodo (PeriodoDTO periodoDTO) {
        if (!periodoDTO.validarNulo()) {
            throw new ErrorDAO("PeriodoDTO con fecha de inicio o de fin vacia", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return ColaboracionDAO.getColaboracionPorPeriodo(periodoDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public List<ColaboracionDTO> getColaboracionPorIdioma (String idioma) {
        if (esCadaInvalida(idioma)) {
            throw new ErrorDAO("Idioma invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return ColaboracionDAO.getColaboracionPorIdioma(idioma);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public List<ColaboracionDTO> getColaboracionPorEstado (String estado) throws ErrorDAO {
        if (esCadaInvalida(estado)) {
            throw new ErrorDAO("PaisDTO invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return ColaboracionDAO.getColaboracionPorEstado(estado);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public int cambiarEstadoColaboracion (ColaboracionDTO colaboracionDTO) {

        if (esIdInvalido(colaboracionDTO.getIdColaboracion())) {
            throw new ErrorDAO("id de la colaboracionDTO invalido", ErrorDAO.Tipo.VALIDACION);
        }
        if (esCadaInvalida(colaboracionDTO.getEstado()
                                       .toString())) {
            throw new ErrorDAO("Error en el estado de la colaboracionDTO", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return ColaboracionDAO.cambiarEstadoColaboracion(colaboracionDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public int agregarEstudianteAColaboracion (ColaboracionDTO colaboracionDTO, EstudianteDTO estudianteDTO) throws ErrorDAO {
        if (esIdInvalido(colaboracionDTO.getIdColaboracion())) {
            throw new ErrorDAO("Error en el id de la colaboracionDTO", ErrorDAO.Tipo.VALIDACION);
        }
        if (esIdInvalido(estudianteDTO.getIdEstudiante())) {
            throw new ErrorDAO("Error en el id del estudianteDTO", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return ColaboracionDAO.agregarEstudianteAColaboracion(colaboracionDTO, estudianteDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }


    @Override
    public int agregarAcademicoAColaboracion (ColaboracionDTO colaboracionDTO, AcademicoDTO academicoDTO) throws ErrorDAO {
        if (esIdInvalido(colaboracionDTO.getIdColaboracion())) {
            throw new ErrorDAO("Error en el id de la colaboracionDTO", ErrorDAO.Tipo.VALIDACION);
        }
        if (esCadaInvalida(academicoDTO.getCedulaProfesional())) {
            throw new ErrorDAO("Error en la cedula del academicoDTO", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return ColaboracionDAO.agregarAcademicoAColaboracion(colaboracionDTO, academicoDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public Optional<ColaboracionDTO> getActivaPorAcademico (AcademicoDTO academicoDTO) throws ErrorDAO {
        return Optional.empty();
    }

    @Override
    public int agregar (ColaboracionDTO colaboracionDTO) throws ErrorDAO {
        if (!colaboracionDTO.esValido()) {
            throw new ErrorDAO("Al menos un dato de la colaboracionDTO esta vacia", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return ColaboracionDAO.registrarColaboracion(colaboracionDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public int modificar (ColaboracionDTO colaboracionDTO) throws ErrorDAO {
        if (!colaboracionDTO.esValido()) {
            throw new ErrorDAO("Al menos un dato de la colaboracionDTO esta vacio", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return ColaboracionDAO.actualizarColaboracion(colaboracionDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public Optional<ColaboracionDTO> getPorId (Integer id) throws ErrorDAO {
        if (esIdInvalido(id)) {
            throw new ErrorDAO("Id invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return Optional.ofNullable(ColaboracionDAO.getPorId(id));
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public List<ColaboracionDTO> getTodos () throws ErrorDAO {
        try {
            return ColaboracionDAO.getTodos();
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public ColaboracionDTO resultSetAObjeto (ResultSet resultados) {
        throw new NotImplementedException("No esta implementada esta función");
    }

    private boolean esCadaInvalida (String cadena) {
        return cadena == null || cadena.isBlank();
    }

    private boolean esIdInvalido (int id) {
        return id <= 0;
    }
}
