package DAO.Interfaces;

import DTO.AcademicoDTO;
import DTO.ColaboracionDTO;
import DTO.EstudianteDTO;
import DTO.PeriodoDTO;
import Utilidades.ErrorDAO;

import java.util.List;
import java.util.Optional;

public interface IColaboracionDAO extends IDAO<ColaboracionDTO, Integer> {
    public Optional<ColaboracionDTO> getColaboracionPorAcademicosParticipantes (AcademicoDTO academicoDTO1, AcademicoDTO academicoDTO2) throws ErrorDAO;
    public Optional<ColaboracionDTO> getColaboracionPorId (int idColaboracion) throws ErrorDAO;
    public List<EstudianteDTO> getListaDeEstudiantes (ColaboracionDTO colaboracionDTO) throws ErrorDAO;
    public List<AcademicoDTO> getAcademicosParticipantes (ColaboracionDTO colaboracionDTO) throws ErrorDAO;
    public List<ColaboracionDTO> getColaboracionPorPeriodo (PeriodoDTO periodoDTO) throws ErrorDAO;
    public List<ColaboracionDTO> getColaboracionPorIdioma (String idioma) throws ErrorDAO;
    public List<ColaboracionDTO> getColaboracionPorEstado (String estado) throws ErrorDAO;
    public int cambiarEstadoColaboracion (ColaboracionDTO colaboracionDTO) throws ErrorDAO;
    public int agregarEstudianteAColaboracion (ColaboracionDTO colaboracionDTO, EstudianteDTO estudianteDTO) throws ErrorDAO;
    public int agregarAcademicoAColaboracion (ColaboracionDTO colaboracionDTO, AcademicoDTO academicoDTO) throws ErrorDAO;
    public Optional<ColaboracionDTO> getActivaPorAcademico (AcademicoDTO academicoDTO) throws ErrorDAO;
    public int registrarPropuestaColaboracion (ColaboracionDTO colaboracionDTO, AcademicoDTO academicoDTO) throws ErrorDAO;
    public List<ColaboracionDTO> obtenerPropuestasColaboracion () throws ErrorDAO;
    public List<AcademicoDTO> obtenerSolicitudAcademicoColaboracion (int idColaboracion) throws ErrorDAO;
}
