package Logica.Interfaces;

import Logica.Dominio.Persona;
import Logica.Dominio.Colaboracion;
import Logica.Dominio.RetroalimentacionColaboracion;
import Logica.ErrorDAO;

import java.util.Optional;

public interface IRetroalimentacionColaboracionDAO extends IDAO<RetroalimentacionColaboracion, Integer> {
    public Optional<RetroalimentacionColaboracion> getPorPersonaYColaboracion (int idAcademico, int idColaboracion);
}
