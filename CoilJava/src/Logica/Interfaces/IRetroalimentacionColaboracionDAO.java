package Logica.Interfaces;

import Logica.Dominio.RetroalimentacionColaboracion;

import java.util.Optional;

public interface IRetroalimentacionColaboracionDAO extends IDAO<RetroalimentacionColaboracion, Integer> {
    public Optional<RetroalimentacionColaboracion> getPorPersonaYColaboracion (int idAcademico, int idColaboracion);
}
