package Logica.Interfaces;

import Logica.Dominio.Academico;
import Logica.Dominio.Colaboracion;
import Logica.Dominio.Estudiante;
import Logica.ErrorDAO;

import java.util.List;

public interface IColaboracionDAO extends IDAO<Colaboracion> {
    public Colaboracion getColaboracionPorAcademicosParticipantes (Academico academico1, Academico academico2) throws ErrorDAO;
    public Colaboracion getColaboracionPorId (int idColaboracion) throws ErrorDAO;
    public List<Estudiante> getListaDeEstudiantes (Colaboracion colaboracion) throws ErrorDAO;
    public List<Academico> getAcademicosParticipantes (Colaboracion colaboracion) throws ErrorDAO;
}
