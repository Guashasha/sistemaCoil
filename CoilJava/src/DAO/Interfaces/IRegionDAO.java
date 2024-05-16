package DAO.Interfaces;

import Utilidades.ErrorDAO;
import DTO.RegionDTO;

import java.util.List;

public interface IRegionDAO {
    public List<RegionDTO> getTodasAlfabeticamente () throws ErrorDAO;
}
