package Logica.Interfaces;

import Logica.Dominio.RetroalimentacionActividad;
import Utilidades.ErrorDAO;

import java.util.Optional;

public interface IRetroalimentacionActividadDAO extends IDAO<RetroalimentacionActividad, Integer> {
    public Optional<RetroalimentacionActividad> getPorPersonaYActividad (int idPersona, int idActividad) throws ErrorDAO;
}
