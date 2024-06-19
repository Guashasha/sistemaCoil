package DAO.Interfaces;

import DTO.AcademicoDTO;
import DTO.ColaboracionDTO;
import DTO.EstudianteDTO;
import DTO.PeriodoDTO;
import Utilidades.ErrorDAO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;



public interface IColaboracionDAO extends IDAO<ColaboracionDTO, Integer> {
    public Optional<ColaboracionDTO> getColaboracionPorId (int idColaboracion) throws ErrorDAO;

    Optional<ColaboracionDTO> getColaboracionActualPorAcademico (String idAcademico) throws ErrorDAO;

    public List<EstudianteDTO> getListaDeEstudiantes (ColaboracionDTO colaboracionDTO) throws ErrorDAO;

    public int cambiarEstadoColaboracion (String nuevoEstado, int idColaboracion) throws ErrorDAO;

    public int agregarEstudianteAColaboracion (ColaboracionDTO colaboracionDTO, EstudianteDTO estudianteDTO) throws ErrorDAO;

    public int registrarSolicitudParticipacion (ColaboracionDTO colaboracionDTO, AcademicoDTO academicoDTO) throws ErrorDAO;

    public Optional<ColaboracionDTO> getActivaPorAcademico (AcademicoDTO academicoDTO) throws ErrorDAO;

    public int registrarPropuestaColaboracion (ColaboracionDTO colaboracionDTO, AcademicoDTO academicoDTO) throws ErrorDAO;

    public List<ColaboracionDTO> getPropuestasColaboracion () throws ErrorDAO;

    public List<AcademicoDTO> getSolicitudAcademicoColaboracion (int idColaboracion) throws ErrorDAO;

    public List<ColaboracionDTO> getColaboracionesDisponibles (String cedulaProfesional, int idUniversidad) throws ErrorDAO;

    public int actualizarEstadoSolicitudDeParticipacion (int idColaboracion, String idAcademico, String nuevoEstado) throws ErrorDAO;

    public boolean existeUnaSolicitudPrevia (ColaboracionDTO colaboracionDTO, AcademicoDTO academicoDTO) throws ErrorDAO;

    public List<ColaboracionDTO> getSolicitudesDeAcademico (AcademicoDTO academicoDTO) throws ErrorDAO;

    public Optional<ColaboracionDTO> getPropuestaPorAcademico (AcademicoDTO academicoDTO) throws ErrorDAO;

    public int eliminarSolicitudDeParticipacion (ColaboracionDTO colaboracionDTO, AcademicoDTO academicoDTO) throws ErrorDAO;

    public int agregarPeriodoAColaboracion (ColaboracionDTO colaboracionDTO) throws ErrorDAO;

    public Optional<ColaboracionDTO> getColaboracionDisponiblePorAcademico (String cedulaProfesional) throws ErrorDAO;

    public Optional<AcademicoDTO> getAcademicoPar (ColaboracionDTO colaboracionDTO) throws ErrorDAO;

    public int rechazarOtrasSolicitudesDeParticipacion (int idColaboracion, String cedulaProfesional) throws ErrorDAO;

    public int retirarEstudianteDeColaboracion (ColaboracionDTO colaboracion, EstudianteDTO estudiante) throws ErrorDAO;

    Optional<ColaboracionDTO> getEnRevisionPorAcademico(AcademicoDTO academicoDTO) throws ErrorDAO;

    public Optional<ColaboracionDTO> getVinculadaPorAcademico (AcademicoDTO academicoDTO) throws ErrorDAO;

    public Optional<LocalDate> getFechaColaboracionMasAntigua () throws ErrorDAO;

    public Map<String, int[]> getNumeraliaAreaAcademica (PeriodoDTO periodo) throws ErrorDAO;

    public Map<String, int[]> getNumeraliaRegion (PeriodoDTO periodo) throws ErrorDAO;

}
