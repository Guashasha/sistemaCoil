package DAO;

import DTO.RegionDTO;
import Utilidades.ErrorDAO;
import org.apache.log4j.Logger;
import java.sql.SQLException;
import java.util.List;

public class RegionAuxiliar {
    private final Logger BITACORA = Logger.getLogger(RegionAuxiliar.class);
    private final RegionDAO REGION_DAO = new RegionDAO();

    public List<RegionDTO> getTodasAlfabeticamente () throws ErrorDAO {
        try {
            return REGION_DAO.getTodasAlfabeticamente();
        }
        catch (SQLException error) {
            BITACORA.info(error.getMessage());
            throw new ErrorDAO("Error en la conexión a la base de datos", ErrorDAO.Tipo.CONSULTA);
        }
    }
}
