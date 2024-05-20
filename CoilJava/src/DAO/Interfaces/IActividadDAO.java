package DAO.Interfaces;

import DTO.ActividadDTO;
import Utilidades.ErrorDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IActividadDAO extends IDAO<ActividadDTO, Integer> {
    public Optional<ActividadDTO> getPorTitulo (String titulo) throws ErrorDAO;
    public List<ActividadDTO> getPorIdColaboracion (Integer idColaboracion) throws ErrorDAO;
}
