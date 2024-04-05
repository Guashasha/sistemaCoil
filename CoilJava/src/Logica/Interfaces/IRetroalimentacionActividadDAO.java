package Logica.Interfaces;

import Logica.Dominio.Retroalimentacion;
import Logica.Dominio.RetroalimentacionActividad;
import Logica.ErrorDAO;

import java.util.Optional;

public interface IRetroalimentacionActividadDAO extends IDAO<RetroalimentacionActividad, Integer> {
    public Optional<RetroalimentacionActividad> getPorPersonaYActividad (int idPersona) throws ErrorDAO;
    public boolean validarRetroalimentacion (RetroalimentacionActividad retroalimentacion) throws ErrorDAO;
    public boolean calificacionCorrecta (int calificacion);
}
