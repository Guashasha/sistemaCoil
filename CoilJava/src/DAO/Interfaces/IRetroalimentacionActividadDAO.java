package DAO.Interfaces;

import DTO.RetroalimentacionActividadDTO;
import Utilidades.ErrorDAO;

import java.util.Optional;

public interface IRetroalimentacionActividadDAO extends IDAO<RetroalimentacionActividadDTO, Integer> {
    public Optional<RetroalimentacionActividadDTO> getPorPersonaYActividad (int idPersona, int idActividad) throws ErrorDAO;
}
