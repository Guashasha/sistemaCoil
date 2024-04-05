package Logica.Interfaces;

import Logica.Dominio.Academico;
import Logica.Dominio.Colaboracion;
import Logica.Dominio.Estudiante;
import Logica.Dominio.Periodo;
import Logica.ErrorDAO;

import java.util.List;
import java.util.Optional;

public interface IColaboracionDAO extends IDAO<Colaboracion, String> {
    public Optional<Colaboracion> getColaboracionPorAcademicosParticipantes (Academico academico1, Academico academico2) throws ErrorDAO;
    public Optional<Colaboracion> getColaboracionPorId (int idColaboracion) throws ErrorDAO;
    public List<Estudiante> getListaDeEstudiantes (Colaboracion colaboracion) throws ErrorDAO;
    public List<Academico> getAcademicosParticipantes (Colaboracion colaboracion) throws ErrorDAO;
    public List<Periodo> getColaboracionPorPeriodo (Colaboracion colaboracion) throws ErrorDAO;
    public List<Colaboracion> getColaboracionPorIdioma (Colaboracion colaboracion) throws ErrorDAO;
    public int cambiarEstadoColaboracion (Colaboracion colaboracion) throws ErrorDAO;
    public int agregarEstudianteAColaboracion (Colaboracion colaboracion, Estudiante estudiante) throws ErrorDAO;
    public int agregarAcademicoAColaboracion (Colaboracion colaboracion, Academico academico) throws ErrorDAO;
}
