package Logica.Interfaces;

import Logica.Dominio.Academico;
import Logica.Dominio.Colaboracion;
import Logica.Dominio.Estudiante;
import Logica.ErrorDAO;

import java.util.List;

public interface IColaboracionDAO {
    public List<Estudiante> getListaEstudiantes (Colaboracion colaboracion) throws ErrorDAO;
    public List<Colaboracion> getColaboracionesTerminadas () throws ErrorDAO;
    public List<Colaboracion> getColaboracionesVigentes () throws ErrorDAO;
    public void registrarColaboracion (Colaboracion colaboracion) throws ErrorDAO;
    public Colaboracion getColaboracionPorId (int idColaboracion) throws ErrorDAO;
    public Colaboracion getColaboracionPorAcademicosParticipantes (Academico academico1, Academico academico2) throws ErrorDAO;
}
