package Logica.Interfaces;

import Logica.Dominio.Retroalimentacion;
import Logica.ErrorDAO;

public interface IRetroalimentacionActividadDAO extends IRetroalimentacionDAO {
    Retroalimentacion getPorPersonaYActividad (int idPersona) throws ErrorDAO;
}
