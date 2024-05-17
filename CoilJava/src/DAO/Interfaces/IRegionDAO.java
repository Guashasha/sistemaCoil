package DAO.Interfaces;

import DTO.RegionDTO;
import java.sql.SQLException;
import java.util.List;

public interface IRegionDAO {
    List<RegionDTO> getTodasAlfabeticamente () throws SQLException;
}
