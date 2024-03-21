package Logica.Interfaces;

import Logica.Dominio.Retroalimentacion;
import Logica.ErrorDAO;

public interface IRetroalimentacionDAO extends IDAO<Retroalimentacion, Integer> {
    Retroalimentacion getPorIdAcademico(String idAcademico) throws ErrorDAO;
    Retroalimentacion getPorIdEstudiante(int idEstudiante) throws ErrorDAO;
    Retroalimentacion getPorIdPersona (int idPersona) throws ErrorDAO;
}
