package DAO.Interfaces;

import DTO.FacultadDTO;
import Utilidades.ErrorDAO;
import java.util.List;
import java.util.Optional;

public interface IFacultadDAO {
    Optional<FacultadDTO> getFacultadPorNombre (String nombre) throws ErrorDAO;
    List<FacultadDTO> getFacultadesPorRegion (String region) throws ErrorDAO;
}
