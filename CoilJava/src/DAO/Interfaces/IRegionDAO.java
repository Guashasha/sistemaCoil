package DAO.Interfaces;

import DTO.RegionDTO;
import Utilidades.ErrorDAO;
import java.util.List;

public interface IRegionDAO {
    List<RegionDTO> getTodasAlfabeticamente () throws ErrorDAO;
}
