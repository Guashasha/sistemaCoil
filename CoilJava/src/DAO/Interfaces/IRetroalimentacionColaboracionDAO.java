package DAO.Interfaces;

import DTO.RetroalimentacionColaboracionDTO;

import java.util.Optional;

public interface IRetroalimentacionColaboracionDAO extends IDAO<RetroalimentacionColaboracionDTO, Integer> {
    public Optional<RetroalimentacionColaboracionDTO> getPorPersonaYColaboracion (int idAcademico, int idColaboracion);
}
