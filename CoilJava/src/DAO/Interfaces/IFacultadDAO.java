package DAO.Interfaces;

import DTO.FacultadDTO;
import Utilidades.ErrorDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IFacultadDAO {
    Optional<FacultadDTO> getFacultadPorNombre (String nombre) throws SQLException;
    List<FacultadDTO> getFacultadPorRegion (String region) throws ErrorDAO;
    List<FacultadDTO> getTodasAlfabeticamente () throws ErrorDAO;
}
