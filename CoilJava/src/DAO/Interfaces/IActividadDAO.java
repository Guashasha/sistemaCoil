package DAO.Interfaces;

import DTO.ActividadDTO;

import java.sql.SQLException;
import java.util.Optional;

public interface IActividadDAO extends IDAO<ActividadDTO, Integer> {
    public Optional<ActividadDTO> getPorTitulo (String titulo) throws SQLException ;
}
